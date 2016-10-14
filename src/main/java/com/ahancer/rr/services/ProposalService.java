package com.ahancer.rr.services;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.custom.type.WalletStatus;
import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.daos.PostDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.daos.WalletDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.models.User;
import com.ahancer.rr.models.Wallet;
import com.ahancer.rr.response.CartResponse;
import com.ahancer.rr.response.ProposalCountResponse;
import com.ahancer.rr.response.ProposalDashboardResponse;
import com.ahancer.rr.response.ProposalResponse;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.response.WalletResponse;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.GZIPCompressionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Transactional(rollbackFor=Exception.class)
public class ProposalService {
	private final static Long timeout = 60000L;
	@Autowired
	private ProposalDao proposalDao;
	@Autowired
	private ProposalMessageDao proposalMessageDao;
	private final int activeDay = 21;
	@Autowired
	private CampaignDao campaignDao;
	@Autowired
	private RobotService robotService;
	@Autowired
	private MessageSource messageSource;
	@Autowired
	private CartService cartService;
	@Autowired
	private WalletDao walletDao;
	@Autowired
	private PostDao postDao;
	@Autowired
	private EmailService emailService;
	@Autowired
	private CacheUtil cacheUtil;
	@Value("${ui.host}")
	private String uiHost;
	private Map<Long,ConcurrentLinkedQueue<DeferredProposal>> proposalPollingMap;
	public class PollingCounter implements Serializable{
		private static final long serialVersionUID = 1L;
		private ProposalCountResponse working;
		private ProposalCountResponse selection;
		private ProposalCountResponse complete;
		private Long cart;
		public ProposalCountResponse getWorking() {
			return working;
		}
		public void setWorking(ProposalCountResponse working) {
			this.working = working;
		}
		public ProposalCountResponse getSelection() {
			return selection;
		}
		public void setSelection(ProposalCountResponse selection) {
			this.selection = selection;
		}
		public ProposalCountResponse getComplete() {
			return complete;
		}
		public void setComplete(ProposalCountResponse complete) {
			this.complete = complete;
		}
		public Long getCart() {
			return cart;
		}
		public void setCart(Long cart) {
			this.cart = cart;
		}
	}
	public static class DeferredProposal extends DeferredResult<PollingCounter> {
		private Role role;
		public DeferredProposal(Role role) {
			super(timeout, null);
			this.role = role;
		}
		public Role getRole() {
			return role;
		}
		public void setRole(Role role) {
			this.role = role;
		}
	}
	public ProposalService() {
		proposalPollingMap =  new ConcurrentHashMap<>();
	}

	public void addInboxPolling(Long userId, DeferredProposal p) {
		if(proposalPollingMap.get(userId) == null) {
			proposalPollingMap.put(userId, new ConcurrentLinkedQueue<>());
		}
		proposalPollingMap.get(userId).add(p);

		//Remove when done
		p.onCompletion(new Runnable() {
			public void run() {
				proposalPollingMap.get(userId).remove(p);
			}
		});
	}
	public void processInboxPolling(Long userId) {
		if(proposalPollingMap.get(userId) == null) {
			return;
		}
		for(DeferredProposal m : proposalPollingMap.get(userId)) {
			if(m.getRole() == Role.Brand) {
				PollingCounter poll = new PollingCounter(); 
				poll.setSelection(countByBrand(userId, ProposalStatus.Selection));
				poll.setWorking(countByBrand(userId, ProposalStatus.Working));
				poll.setComplete(countByBrand(userId, ProposalStatus.Complete));

				Cart c = cartService.getInCartByBrand(userId);
				if(c != null) {
					poll.setCart((long) c.getProposals().size());
				} else {
					poll.setCart(0L);
				}
				m.setResult(poll);
			} else if(m.getRole() == Role.Influencer) {
				PollingCounter poll = new PollingCounter(); 
				poll.setSelection(countByInfluencer(userId, ProposalStatus.Selection));
				poll.setWorking(countByInfluencer(userId, ProposalStatus.Working));
				poll.setComplete(countByInfluencer(userId, ProposalStatus.Complete));
				m.setResult(poll);
			}

		}
	}
	public List<Proposal> findAllByBrand(Long brandId, Long campaignId) {
		return proposalDao.findByCampaignBrandIdAndCampaignCampaignId(brandId,campaignId);
	}
	public Long countByUnreadProposalForBrand(Long brandId) {
		return proposalMessageDao.countByProposalCampaignBrandIdAndIsBrandReadFalse(brandId);
	}
	public Long countByUnreadProposalForInfluencer(Long influencerId) {
		return proposalMessageDao.countByProposalInfluencerIdAndIsInfluencerReadFalse(influencerId);
	}
	public Long countByUnreadProposalMessageForBrand(Long proposalId, Long brandId) {
		return proposalMessageDao.countByProposalProposalIdAndProposalCampaignBrandIdAndIsBrandReadFalse(proposalId, brandId);
	}
	public Long countByUnreadProposalMessageForInfluencer(Long proposalId, Long influencerId) {
		return proposalMessageDao.countByProposalProposalIdAndProposalInfluencerIdAndIsInfluencerReadFalse(proposalId, influencerId);
	}
	public ProposalCountResponse countByBrand(Long brandId, ProposalStatus status) {
		Long proposalCount = proposalDao.countByCampaignBrandIdAndStatus(brandId, status);
		Long unreadBrand = proposalMessageDao.countByProposalCampaignBrandIdAndIsBrandReadFalseAndProposalStatus(brandId, status);
		return new ProposalCountResponse(proposalCount,unreadBrand);
	}
	public ProposalCountResponse countByInfluencer(Long influencerId, ProposalStatus status) {
		Long proposalCount = proposalDao.countByInfluencerInfluencerIdAndStatus(influencerId, status);
		Long unreadInfluencer = proposalMessageDao.countByProposalInfluencerIdAndIsInfluencerReadFalseAndProposalStatus(influencerId,status);
		return new ProposalCountResponse(proposalCount,unreadInfluencer);
	}
	public ProposalCountResponse countByAdmin(ProposalStatus status) {
		Long proposalCount = proposalDao.countByStatus(status);
		return new ProposalCountResponse(proposalCount,0L);
	}
	public Page<Proposal> findAllByBrand(Long brandId,ProposalStatus status, String search,Pageable pageable) {
		Page<Proposal> response = null;
		Date date = Date.from(LocalDate.now().minusDays(activeDay).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		if(StringUtils.isEmpty(search)){
			response = proposalDao.findByCampaignBrandIdAndStatusAndMessageUpdatedAtAfter(brandId, status,  date, pageable);
		} else {
			response = proposalDao.findByCampaignBrandIdAndStatusAndCampaignTitleContainingAndMessageUpdatedAtAfter(brandId, status, search, date, pageable);
		}
		return response;
	}
	public ProposalResponse findOneByAdmin(Long proposalId) throws Exception {
		Proposal proposal =  proposalDao.findOne(proposalId);
		if(null == proposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		ProposalResponse response = new ProposalResponse();
		response.setCampaign(proposal.getCampaign());
		if(null != proposal.getCart()){
			response.setCartId(proposal.getCartId());
			response.setCart(new CartResponse(proposal.getCart()));
		}
		if(null != proposal.getWallet()){
			response.setWalletId(proposal.getWalletId());
			response.setWallet(new WalletResponse(proposal.getWallet()));
		}
		response.setCompleteDate(proposal.getCompleteDate());
		response.setCompletionTime(proposal.getCompletionTime());
		response.setDueDate(proposal.getDueDate());
		response.setFee(proposal.getFee());
		response.setInfluencer(proposal.getInfluencer());
		response.setInfluencerId(proposal.getInfluencerId());
		response.setMedia(proposal.getMedia());
		response.setMessageUpdatedAt(proposal.getMessageUpdatedAt());
		response.setPrice(proposal.getPrice());
		response.setProposalId(proposal.getProposalId());
		response.setStatus(proposal.getStatus());
		response.setRabbitFlag(proposal.getRabbitFlag());
		return response;
	}
	public ProposalResponse findOneByBrand(Long proposalId,Long brandId) throws Exception {
		Proposal proposal =  proposalDao.findByProposalIdAndCampaignBrandId(proposalId,brandId);

		if(null == proposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		ProposalResponse response = new ProposalResponse();
		response.setCampaign(proposal.getCampaign());
		if(null != proposal.getCart()){
			response.setCartId(proposal.getCartId());
			response.setCart(new CartResponse(proposal.getCart()));
		}
		if(null != proposal.getWallet()){
			response.setWalletId(proposal.getWalletId());
			response.setWallet(new WalletResponse(proposal.getWallet()));
		}
		response.setCompleteDate(proposal.getCompleteDate());
		response.setCompletionTime(proposal.getCompletionTime());
		response.setDueDate(proposal.getDueDate());
		response.setFee(proposal.getFee());
		response.setInfluencer(proposal.getInfluencer());
		response.setInfluencerId(proposal.getInfluencerId());
		response.setMedia(proposal.getMedia());
		response.setMessageUpdatedAt(proposal.getMessageUpdatedAt());
		response.setPrice(proposal.getPrice());
		response.setProposalId(proposal.getProposalId());
		response.setStatus(proposal.getStatus());
		response.setRabbitFlag(proposal.getRabbitFlag());
		return response;
	}
	public ProposalResponse findOneByInfluencer(Long proposalId,Long influencerId) throws Exception {
		Proposal proposal =  proposalDao.findByProposalIdAndInfluencerId(proposalId,influencerId);
		if(null == proposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		ProposalResponse response = new ProposalResponse();
		response.setCampaign(proposal.getCampaign());
		if(null != proposal.getCart()){
			response.setCartId(proposal.getCartId());
			response.setCart(new CartResponse(proposal.getCart()));
		}
		if(null != proposal.getWallet()){
			response.setWalletId(proposal.getWalletId());
			response.setWallet(new WalletResponse(proposal.getWallet()));
		}
		response.setCompleteDate(proposal.getCompleteDate());
		response.setCompletionTime(proposal.getCompletionTime());
		response.setDueDate(proposal.getDueDate());
		response.setFee(proposal.getFee());
		response.setInfluencer(proposal.getInfluencer());
		response.setInfluencerId(proposal.getInfluencerId());
		response.setMedia(proposal.getMedia());
		response.setMessageUpdatedAt(proposal.getMessageUpdatedAt());
		response.setPrice(proposal.getPrice());
		response.setProposalId(proposal.getProposalId());
		response.setStatus(proposal.getStatus());
		response.setRabbitFlag(proposal.getRabbitFlag());
		return response;
	}
	public List<Proposal> findAllActiveByInfluencer(Long influencerId) {
		return proposalDao.findByInfluencerId(influencerId);
	}
	public Page<Proposal> findAllByInfluencer(Long influencerId,ProposalStatus status, String search,Pageable pageable) {
		Page<Proposal> response = null;
		Date date = Date.from(LocalDate.now().minusDays(activeDay).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		if(StringUtils.isEmpty(search)){
			response = proposalDao.findByInfluencerIdAndStatusAndMessageUpdatedAtAfter(influencerId,status, date, pageable);
		} else {
			response = proposalDao.findByInfluencerIdAndStatusAndCampaignTitleContainingAndMessageUpdatedAtAfter(influencerId, status, search, date, pageable);
		}

		return response;
	}
	public Page<Proposal> findAllByAdmin(ProposalStatus status, String search, Pageable pageable) {

		Page<Proposal> response = null;
		if(StringUtils.isEmpty(search)){
			response = proposalDao.findByStatus(status, pageable);
		} else {
			response = proposalDao.findByStatusAndCampaignTitleContaining(status, search, pageable);
		}

		return response;
	}
	public Page<Proposal> findAll(Pageable pageable) {
		return proposalDao.findAll(pageable);
	}
	public Proposal createCampaignProposalByInfluencer(Long campaignId, Proposal proposal,UserResponse user,Locale locale) throws Exception {
		Campaign campaign = campaignDao.findOne(campaignId);
		Long influecnerId = user.getInfluencer().getInfluencerId();
		long count = proposalDao.countByInfluencerInfluencerIdAndCampaignCampaignId(influecnerId, campaign.getCampaignId());
		if(0 < count){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.already.proposal");
		}
		proposal.setCampaign(campaign);
		proposal.setInfluencerId(influecnerId);
		proposal.setMessageUpdatedAt(new Date());
		proposal.setStatus(ProposalStatus.Selection);
		proposal.setFee(Math.floor(proposal.getPrice()*0.18));
		proposal.setRabbitFlag(false);
		proposal.setHasPost(false);
		proposal.setIsReferralPay(false);
		proposal = proposalDao.save(proposal);
		//Insert first message
		ProposalMessage firstMessage = new ProposalMessage();
		firstMessage.setIsBrandRead(false);
		firstMessage.setIsInfluencerRead(true);
		firstMessage.setMessage(proposal.getDescription());
		firstMessage.setProposal(proposal);
		firstMessage.setProposalId(proposal.getProposalId());
		firstMessage.setUserId(influecnerId);
		firstMessage = proposalMessageDao.save(firstMessage);
		String to = campaign.getBrand().getUser().getEmail();
		String subject = messageSource.getMessage("email.brand.new.proposal.subject",null,locale);
		String body = messageSource.getMessage("email.brand.new.proposal.message",null,locale)
				.replace("{{Influencer Name}}", user.getName())
				.replace("{{Campaign Name}}", campaign.getTitle())
				.replace("{{Host}}", uiHost)
				.replace("{{Proposal Id}}", String.valueOf(proposal.getProposalId()));
		emailService.send(to, subject, body);
		return proposal;
	}
	public Proposal updateCampaignProposalByInfluencer(Long proposalId, Proposal proposal,Long influencerId, Locale local) throws Exception {
		Proposal oldProposal = proposalDao.findByProposalIdAndInfluencerId(proposalId,influencerId);
		if(null == oldProposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		oldProposal.setMedia(proposal.getMedia());
		oldProposal.setCompletionTime(proposal.getCompletionTime());
		oldProposal.setPrice(proposal.getPrice());
		oldProposal.setFee(Math.floor(oldProposal.getPrice()*0.18));
		oldProposal.setDescription(proposal.getDescription());
		//Insert robot message
		ProposalMessage rebotMessage = new ProposalMessage();
		rebotMessage.setIsBrandRead(true);
		rebotMessage.setIsInfluencerRead(true);
		String message = messageSource.getMessage("robot.proposal.message", null, local).replace("{{Influencer Name}}", oldProposal.getInfluencer().getUser().getName());
		rebotMessage.setMessage(message);
		rebotMessage.setProposalId(proposal.getProposalId());
		User robotUser = robotService.getRobotUser();
		rebotMessage.setUserId(robotUser.getUserId());
		rebotMessage = proposalMessageDao.save(rebotMessage);
		oldProposal = proposalDao.save(oldProposal);
		rebotMessage.setUser(robotUser);
		return oldProposal;
	}
	public Proposal updateProposalStatusByBrand(Long proposalId,ProposalStatus status, Long brandId, Locale locale) throws Exception {
		Proposal oldProposal = proposalDao.findByProposalIdAndCampaignBrandId(proposalId,brandId);
		if(null == oldProposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		oldProposal.setStatus(status);

		ProposalMessage rebotMessage = new ProposalMessage();
		//rebotMessage.setCreatedAt(new Date());
		rebotMessage.setIsBrandRead(true);
		rebotMessage.setIsInfluencerRead(true);
		Calendar cal = Calendar.getInstance();
		if(ProposalStatus.Working.equals(oldProposal.getStatus())
				|| ProposalStatus.Selection.equals(oldProposal.getStatus())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.invalid.status");
		} else if(ProposalStatus.Complete.equals(oldProposal.getStatus())){
			//set robot message
			String message = messageSource.getMessage("robot.proposal.complete.status.message", null, locale);
			rebotMessage.setMessage(message
					.replace("{{Brand Name}}", oldProposal.getCampaign().getBrand().getBrandName())
					.replace("{{Influencer Name}}", oldProposal.getInfluencer().getUser().getName()));
			oldProposal.setCompleteDate(cal.getTime());
			//add wallet
			Wallet wallet = walletDao.findByInfluencerIdAndStatus(oldProposal.getInfluencerId(), WalletStatus.WaitForPayout);
			if(null == wallet){
				wallet = new Wallet();
				wallet.setStatus(WalletStatus.WaitForPayout);
				wallet.setInfluencerId(oldProposal.getInfluencerId());
				wallet = walletDao.save(wallet);
			}
			oldProposal.setWalletId(wallet.getWalletId());

			Double sum = oldProposal.getPrice()-oldProposal.getFee();
			for(Proposal proposal : wallet.getProposals()){
				sum = sum + proposal.getPrice() - proposal.getFee();
			}
			//send email to influencer
			String to = oldProposal.getInfluencer().getUser().getEmail();
			String subject = messageSource.getMessage("email.influencer.brand.confirm.proposal.subject", null, locale);
			String body = messageSource.getMessage("email.influencer.brand.confirm.proposal.message", null, locale)
					.replace("{{Brand Name}}", oldProposal.getCampaign().getBrand().getBrandName())
					.replace("{{Outstanding Wallet Money}}", String.valueOf(sum))
					.replace("{{Campaign Name}}", oldProposal.getCampaign().getTitle())
					.replace("{{Host}}", uiHost);
			emailService.send(to, subject, body);
		}
		User robotUser = robotService.getRobotUser();
		rebotMessage.setProposalId(oldProposal.getProposalId());
		rebotMessage.setUserId(robotUser.getUserId());
		rebotMessage = proposalMessageDao.save(rebotMessage);
		rebotMessage.setUser(robotUser);
		oldProposal = proposalDao.save(oldProposal);
		return oldProposal;
	}
	public Proposal getAppliedProposal(Long influencerId, Long campaignId) {
		return proposalDao.findByInfluencerIdAndCampaignCampaignId(influencerId,campaignId);
	}
	public void dismissProposalNotification(Long proposalId, Long influencerId){
		proposalDao.updateRabbitFlag(true,proposalId, influencerId);
	}
	public List<ProposalDashboardResponse> getProposalFromCampaignByAdmin(Long campaignId) throws Exception {
		Object obj = cacheUtil.getCacheObject(ApplicationConstant.DashboardRequestCache, campaignId);
		String jsonCache = StringUtils.EMPTY;
		List<ProposalDashboardResponse> response = null;
		ObjectMapper mapper = new ObjectMapper();
		if(null != obj) {
			byte[] bytes = (byte[]) obj;
			jsonCache =  GZIPCompressionUtil.decompress(bytes);
			response = mapper.readValue(jsonCache, new TypeReference<List<ProposalDashboardResponse>>(){});
		} else {
			List<ProposalStatus> statuses = new ArrayList<ProposalStatus>();
			statuses.add(ProposalStatus.Complete);
			statuses.add(ProposalStatus.Working);
			response = proposalDao.getListProposalByCampaignAndStatus(campaignId, statuses);
			for(ProposalDashboardResponse proposal : response){
				proposal.setPosts(postDao.getAggregatePost(proposal.getProposalId()));
				proposal.setMedia(proposalDao.getMediaFromProposal(proposal.getProposalId()));
			}
			if(null !=  response && !response.isEmpty()) {
				jsonCache = mapper.writeValueAsString(response);
				cacheUtil.putCacheObject(ApplicationConstant.DashboardRequestCache, campaignId, GZIPCompressionUtil.compress(jsonCache));
			}
		}
		return response;
	}

	public List<ProposalDashboardResponse> getProposalFromCampaignByBrand(Long campaignId, Long brandId) throws Exception {
		Object obj = cacheUtil.getCacheObject(ApplicationConstant.DashboardRequestCache, campaignId);
		String jsonCache = StringUtils.EMPTY;
		List<ProposalDashboardResponse> response = null;
		ObjectMapper mapper = new ObjectMapper();
		if(null != obj) {
			byte[] bytes = (byte[]) obj;
			jsonCache =  GZIPCompressionUtil.decompress(bytes);
			response = mapper.readValue(jsonCache, new TypeReference<List<ProposalDashboardResponse>>(){});
		} else {
			List<ProposalStatus> statuses = new ArrayList<ProposalStatus>();
			statuses.add(ProposalStatus.Complete);
			statuses.add(ProposalStatus.Working);
			response = proposalDao.getListProposalByCampaignAndBrandAndStatus(campaignId, brandId, statuses);
			for(ProposalDashboardResponse proposal : response){
				proposal.setPosts(postDao.getAggregatePost(proposal.getProposalId()));
				proposal.setMedia(proposalDao.getMediaFromProposal(proposal.getProposalId()));
			}
			if(null !=  response && !response.isEmpty()) {
				jsonCache = mapper.writeValueAsString(response);
				cacheUtil.putCacheObject(ApplicationConstant.DashboardRequestCache, campaignId, GZIPCompressionUtil.compress(jsonCache));
			}
		}
		return response;
	}
	
	public Page<Proposal> getReferralProposal(String search, Pageable pageable){
		Page<Proposal> page = null;
		if(StringUtils.isEmpty(search)){
			page = proposalDao.findAllByCampaignBrandUserReferralReferralIdNotNull(pageable);
		} else {
			
		}
		
		return page;
		
	}

}
