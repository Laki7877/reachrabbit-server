package com.ahancer.rr.services.impl;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import com.ahancer.rr.constants.ApplicationConstant;
import com.ahancer.rr.custom.type.DocumentType;
import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.custom.type.TransactionStatus;
import com.ahancer.rr.custom.type.TransactionType;
import com.ahancer.rr.custom.type.WalletStatus;
import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.daos.InfluencerDao;
import com.ahancer.rr.daos.PostDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.daos.ReferralTransactionDocumentDao;
import com.ahancer.rr.daos.TransactionDao;
import com.ahancer.rr.daos.WalletDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.Cart;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.models.ReferralTransactionDocument;
import com.ahancer.rr.models.Transaction;
import com.ahancer.rr.models.User;
import com.ahancer.rr.models.Wallet;
import com.ahancer.rr.response.ProposalCountResponse;
import com.ahancer.rr.response.ProposalDashboardResponse;
import com.ahancer.rr.response.ProposalResponse;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.services.CartService;
import com.ahancer.rr.services.EmailService;
import com.ahancer.rr.services.ProposalService;
import com.ahancer.rr.services.RobotService;
import com.ahancer.rr.utils.CacheUtil;
import com.ahancer.rr.utils.EncodeUtil;
import com.ahancer.rr.utils.GZIPCompressionUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Transactional(rollbackFor=Exception.class)
public class ProposalServiceImpl implements ProposalService {
	private final static Long timeout = 60000L;
	@Autowired
	private InfluencerDao influencerDao;
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
	private TransactionDao transactionDao;
	@Autowired
	private ReferralTransactionDocumentDao referralTransactionDocumentDao;
	@Autowired
	private PostDao postDao;
	@Autowired
	private EmailService emailService;
	@Autowired
	private CacheUtil cacheUtil;
	@Value("${ui.host}")
	private String uiHost;
	@Value("${app.proposal.commission.percent}")
	private Double commission;
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
	
	public ProposalServiceImpl() {
		proposalPollingMap =  new ConcurrentHashMap<>();
	}

	public void addInboxPolling(Long userId, DeferredProposal p) throws Exception {
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
	
	public void processInboxPolling(Long userId) throws Exception {
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
	
	public List<Proposal> findAllByBrand(Long brandId, Long campaignId) throws Exception {
		return proposalDao.findByCampaignBrandIdAndCampaignCampaignId(brandId,campaignId);
	}
	
	public Long countByUnreadProposalForBrand(Long brandId) throws Exception {
		return proposalMessageDao.countByProposalCampaignBrandIdAndIsBrandReadFalse(brandId);
	}
	
	public Long countByUnreadProposalForInfluencer(Long influencerId) throws Exception {
		return proposalMessageDao.countByProposalInfluencerIdAndIsInfluencerReadFalse(influencerId);
	}
	
	public Long countByUnreadProposalMessageForBrand(Long proposalId, Long brandId) throws Exception {
		return proposalMessageDao.countByProposalProposalIdAndProposalCampaignBrandIdAndIsBrandReadFalse(proposalId, brandId);
	}
	
	public Long countByUnreadProposalMessageForInfluencer(Long proposalId, Long influencerId) throws Exception {
		return proposalMessageDao.countByProposalProposalIdAndProposalInfluencerIdAndIsInfluencerReadFalse(proposalId, influencerId);
	}
	
	public ProposalCountResponse countByBrand(Long brandId, ProposalStatus status) throws Exception {
		Date date = Date.from(LocalDate.now().minusDays(activeDay).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Long proposalCount = proposalDao.countByCampaignBrandIdAndStatusAndMessageUpdatedAtAfter(brandId, status,date);
		Long unreadBrand = proposalMessageDao.countByProposalCampaignBrandIdAndIsBrandReadFalseAndProposalStatusAndProposalMessageUpdatedAtAfter(brandId, status, date);
		return new ProposalCountResponse(proposalCount,unreadBrand);
	}
	
	public ProposalCountResponse countByInfluencer(Long influencerId, ProposalStatus status) throws Exception {
		Date date = Date.from(LocalDate.now().minusDays(activeDay).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		Long proposalCount = proposalDao.countByInfluencerInfluencerIdAndStatusAndMessageUpdatedAtAfter(influencerId, status,date);
		Long unreadInfluencer = proposalMessageDao.countByProposalInfluencerIdAndIsInfluencerReadFalseAndProposalStatusAndProposalMessageUpdatedAtAfter(influencerId,status,date);
		return new ProposalCountResponse(proposalCount,unreadInfluencer);
	}
	
	public ProposalCountResponse countByAdmin(ProposalStatus status) throws Exception {
		Long proposalCount = proposalDao.countByStatus(status);
		return new ProposalCountResponse(proposalCount,0L);
	}
	
	public Page<ProposalResponse> findAllByBrand(Long brandId,ProposalStatus status, String search,Pageable pageable) throws Exception {
		Page<ProposalResponse> response = null;
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
		ProposalResponse response = new ProposalResponse(proposal,Role.Admin.toString());
		return response;
	}
	
	public ProposalResponse findOneByBrand(Long proposalId,Long brandId) throws Exception {
		Proposal proposal =  proposalDao.findByProposalIdAndCampaignBrandId(proposalId,brandId);

		if(null == proposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		ProposalResponse response = new ProposalResponse(proposal,Role.Brand.toString());
		return response;
	}
	
	public ProposalResponse findOneByInfluencer(Long proposalId,Long influencerId) throws Exception {
		Proposal proposal =  proposalDao.findByProposalIdAndInfluencerId(proposalId,influencerId);
		if(null == proposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		ProposalResponse response = new ProposalResponse(proposal,Role.Influencer.toString());
		return response;
	}
	
	public List<ProposalResponse> findAllActiveByInfluencer(Long influencerId) throws Exception {
		return proposalDao.findByInfluencerId(influencerId);
	}
	
	public Page<ProposalResponse> findAllByInfluencer(Long influencerId,ProposalStatus status, String search,Pageable pageable) throws Exception {
		Page<ProposalResponse> response = null;
		Date date = Date.from(LocalDate.now().minusDays(activeDay).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
		if(StringUtils.isEmpty(search)){
			response = proposalDao.findByInfluencerIdAndStatusAndMessageUpdatedAtAfter(influencerId,status, date, pageable);
		} else {
			response = proposalDao.findByInfluencerIdAndStatusAndCampaignTitleContainingAndMessageUpdatedAtAfter(influencerId, status, search, date, pageable);
		}

		return response;
	}
	
	public Page<ProposalResponse> findAllByAdmin(ProposalStatus status, String search, Pageable pageable) throws Exception {
		Page<ProposalResponse> response = null;
		if(StringUtils.isEmpty(search)) {
			response = proposalDao.findByStatus(status, pageable);
		} else {
			response = proposalDao.findByStatusAndCampaignTitleContaining(status, search, pageable);
		}

		return response;
	}
	
	public Page<Proposal> findAll(Pageable pageable) throws Exception {
		return proposalDao.findAll(pageable);
	}
	
	public Proposal createCampaignProposalByInfluencer(Long campaignId, Proposal proposal,UserResponse user,Locale locale) throws Exception {
		Campaign campaign = campaignDao.findOne(campaignId);
		Long influencerId = user.getInfluencer().getInfluencerId();
		long count = proposalDao.countByInfluencerInfluencerIdAndCampaignCampaignId(influencerId, campaign.getCampaignId());
		if(0 < count){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.already.proposal");
		}
		Date today = new Date();
		if(campaign.getProposalDeadline().compareTo(today) < 0) {
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.proposal.expire");
		}
		proposal.setCampaign(campaign);
		proposal.setInfluencerId(influencerId);
		proposal.setMessageUpdatedAt(new Date());
		proposal.setStatus(ProposalStatus.Selection);
		Double influencerCommission = influencerDao.findCommissionByInfluencerId(influencerId);
		Double com = influencerCommission == null 
				||  influencerCommission <= 0 
				|| influencerCommission > 100 ? this.commission : influencerCommission;
		proposal.setFee(Math.floor(proposal.getPrice()*com/100));
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
		firstMessage.setUserId(influencerId);
		firstMessage.setReferenceId(UUID.randomUUID().toString());
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
	
	public ProposalResponse updateCampaignProposalByInfluencer(Long proposalId, Proposal proposal,UserResponse user, Locale local) throws Exception {
		Long influencerId = user.getInfluencer().getInfluencerId();
		Proposal oldProposal = proposalDao.findByProposalIdAndInfluencerId(proposalId,influencerId);
		if(null == oldProposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		oldProposal.setMedia(proposal.getMedia());
		oldProposal.setCompletionTime(proposal.getCompletionTime());
		oldProposal.setPrice(proposal.getPrice());
		Double influencerCommission = influencerDao.findCommissionByInfluencerId(influencerId);
		Double com = influencerCommission == null 
				||  influencerCommission <= 0 
				|| influencerCommission > 100 ? this.commission : influencerCommission;
		oldProposal.setFee(Math.floor(proposal.getPrice()*com/100));
		oldProposal.setDescription(proposal.getDescription());
		//Insert robot message
		ProposalMessage rebotMessage = new ProposalMessage();
		rebotMessage.setIsBrandRead(false);
		rebotMessage.setIsInfluencerRead(true);
		String message = messageSource.getMessage("robot.proposal.message", null, local)
				.replace("{{Influencer Name}}", oldProposal.getInfluencer().getUser().getName());
		rebotMessage.setMessage(message);
		rebotMessage.setReferenceId(UUID.randomUUID().toString());
		rebotMessage.setProposalId(proposal.getProposalId());
		User robotUser = robotService.getRobotUser();
		rebotMessage.setUserId(robotUser.getUserId());
		rebotMessage = proposalMessageDao.save(rebotMessage);
		oldProposal = proposalDao.save(oldProposal);
		rebotMessage.setUser(robotUser);
		ProposalResponse response = new ProposalResponse(oldProposal, Role.Influencer.toString());
		return response;
	}
	
	public ProposalResponse updateProposalStatusByBrand(Long proposalId,ProposalStatus status, Long brandId, Locale locale) throws Exception {
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
		rebotMessage.setReferenceId(UUID.randomUUID().toString());
		rebotMessage = proposalMessageDao.save(rebotMessage);
		rebotMessage.setUser(robotUser);
		oldProposal = proposalDao.save(oldProposal);
		ProposalResponse response = new ProposalResponse(oldProposal, Role.Brand.toString());
		return response;
	}
	
	public Proposal getAppliedProposal(Long influencerId, Long campaignId) throws Exception {
		return proposalDao.findByInfluencerIdAndCampaignCampaignId(influencerId,campaignId);
	}
	
	public void dismissProposalNotification(Long proposalId, Long influencerId) throws Exception {
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
	
	public Page<Proposal> getReferralProposal(String search, Pageable pageable) throws Exception {
		Page<Proposal> page = null;
		List<ProposalStatus> statues = new ArrayList<ProposalStatus>();
		statues.add(ProposalStatus.Working);
		statues.add(ProposalStatus.Complete);
		if(StringUtils.isEmpty(search)){
			page = proposalDao.findAllByCampaignBrandUserReferralReferralIdNotNull(statues,pageable);
		} else {
			page = proposalDao.findAllByCampaignBrandUserReferralReferralIdNotNullAndSearch(statues,search, pageable);
		}
		
		return page;
	}
	
	public Proposal payReferralProposal(Long proposalId) throws Exception{
		Proposal proposal = proposalDao.findOne(proposalId);
		if(null == proposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		//create transaction
		Transaction transaction = new Transaction();
		transaction.setAmount(Math.floor(proposal.getFee() * proposal.getCampaign().getBrand().getUser().getReferral().getCommission() / 100 ));
		transaction.setUserId(proposal.getCampaign().getBrand().getUser().getReferral().getPartnerId());
		transaction.setStatus(TransactionStatus.Complete);
		transaction.setType(TransactionType.Referral);
		transaction = transactionDao.save(transaction);
		transaction.setTransactionNumber(EncodeUtil.encodeLong(transaction.getTransactionId()));
		transaction = transactionDao.save(transaction);
		//create document
		ReferralTransactionDocument document = new ReferralTransactionDocument();
		document.setProposalId(proposalId);
		document.setReferralId(proposal.getCampaign().getBrand().getUser().getReferral().getReferralId());
		document.setTransactionId(transaction.getTransactionId());
		document.setType(DocumentType.Referral);
		document.setAmount(transaction.getAmount());
		document = referralTransactionDocumentDao.save(document);
		//update flag
		proposalDao.updateIsReferralPay(true, proposalId);
		proposal.setIsReferralPay(true);		
		return proposal;
	}

}
