package com.ahancer.rr.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.custom.type.WalletStatus;
import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.daos.WalletDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.models.User;
import com.ahancer.rr.models.Wallet;
import com.ahancer.rr.response.CartResponse;
import com.ahancer.rr.response.ProposalResponse;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.response.WalletResponse;

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
	private WalletDao walletDao;
	
	@Autowired
	private EmailService emailService;
	

	private Map<Long,ConcurrentLinkedQueue<DeferredProposal>> proposalPollingMap;

	public static class DeferredProposal extends DeferredResult<Long> {
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
				Long count = countByUnreadProposalForBrand(userId);
				m.setResult(count);
			} else if(m.getRole() == Role.Influencer) {
				Long count = countByUnreadProposalForInfluencer(userId);
				m.setResult(count);
			}

		}
	}
	
	public void processInboxPollingByOne(Long userId) {
		if(proposalPollingMap.get(userId) == null) {
			return;
		}
		for(DeferredProposal m : proposalPollingMap.get(userId)) {
			if(m.getRole() == Role.Brand) {
				Long count = countByUnreadProposalForBrand(userId) + 1;
				m.setResult(count);
			} else if(m.getRole() == Role.Influencer) {
				Long count = countByUnreadProposalForInfluencer(userId) + 1;
				m.setResult(count);
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

	public Long countByBrand(Long brandId, ProposalStatus status) {
		return proposalDao.countByCampaignBrandIdAndStatus(brandId, status);
	}
	public Long countByInfluencer(Long influencerId, ProposalStatus status) {
		return proposalDao.countByInfluencerInfluencerIdAndStatus(influencerId, status);
	}

	public Page<Proposal> findAllByBrand(Long brandId,ProposalStatus status,Pageable pageable) {
		return proposalDao.findByCampaignBrandIdAndStatusAndMessageUpdatedAtAfter(brandId, status,  Date.from(LocalDate.now().minusDays(activeDay).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), pageable);
	}

	public ProposalResponse findOneByBrand(Long proposalId,Long brandId) throws Exception{
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
		//response.setWallet(proposal.getWallet());
		//response.setWalletId(proposal.getWalletId());
		
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
		//response.setWallet(proposal.getWallet());
		//response.setWalletId(proposal.getWalletId());
		
		return response;
		
		
	}

	public List<Proposal> findAllActiveByInfluencer(Long influencerId) {
		return proposalDao.findByInfluencerId(influencerId);
	}

	public Page<Proposal> findAllByInfluencer(Long influencerId,ProposalStatus status,Pageable pageable) {
		return proposalDao.findByInfluencerIdAndStatusAndMessageUpdatedAtAfter(influencerId,status, Date.from(LocalDate.now().minusDays(activeDay).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), pageable);
	}

//	public Page<Proposal> findAllByInfluencer(Long influencerId, Long campaignId, Pageable pageable) {
//		if( null != campaignId ) {
//			return proposalDao.findByInfluencerIdAndCampaignCampaignIdAndMessageUpdatedAtAfter(influencerId,  campaignId, Date.from(LocalDate.now().minusDays(activeDay).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), pageable);
//		} else {
//			return findAllByInfluencer(influencerId, pageable);
//		}
//	}

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
		proposal = proposalDao.save(proposal);
		//Insert first message
		ProposalMessage firstMessage = new ProposalMessage();
		firstMessage.setIsBrandRead(false);
		firstMessage.setIsInfluencerRead(true);
		firstMessage.setMessage(proposal.getDescription());
		firstMessage.setProposal(proposal);
		firstMessage.setUserId(influecnerId);
		firstMessage = proposalMessageDao.save(firstMessage);
		String to = campaign.getBrand().getUser().getEmail();
		String subject = messageSource.getMessage("email.brand.new.proposal.subject",null,locale);
		String body = messageSource.getMessage("email.brand.new.proposal.message",null,locale).replace("{{Influencer Name}}", user.getName());
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
		rebotMessage.setMessage(oldProposal.getInfluencer().getUser().getName() + " " + messageSource.getMessage("robot.proposal.message", null, local));
		rebotMessage.setProposal(proposal);
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
		rebotMessage.setIsBrandRead(true);
		rebotMessage.setIsInfluencerRead(true);
		Calendar cal = Calendar.getInstance();
		if(ProposalStatus.Working.equals(oldProposal.getStatus())
				|| ProposalStatus.Selection.equals(oldProposal.getStatus())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.invalid.status");
		} else if(ProposalStatus.Complete.equals(oldProposal.getStatus())){
			//set robot message
			rebotMessage.setMessage(messageSource.getMessage("robot.proposal.complete.status.message", null, locale));
			oldProposal.setCompleteDate(cal.getTime());
			//add wallet
			Wallet wallet = walletDao.findByInfluencerIdAndStatus(oldProposal.getInfluencerId(), WalletStatus.Pending);
			if(null == wallet){
				wallet = new Wallet();
				wallet.setStatus(WalletStatus.Pending);
				wallet.setInfluencerId(oldProposal.getInfluencerId());
				wallet = walletDao.save(wallet);
			}
			oldProposal.setWalletId(wallet.getWalletId());
			
			//send email to influencer
			String to = oldProposal.getInfluencer().getUser().getEmail();
			String subject = messageSource.getMessage("email.influencer.brand.confirm.proposal.subject", null, locale);
			String body = messageSource.getMessage("email.influencer.brand.confirm.proposal.message", null, locale).replace("{{Brand Name}}", oldProposal.getCampaign().getBrand().getBrandName());
			emailService.send(to, subject, body);
		}
		User robotUser = robotService.getRobotUser();
		rebotMessage.setProposal(oldProposal);
		rebotMessage.setUserId(robotUser.getUserId());
		rebotMessage = proposalMessageDao.save(rebotMessage);
		rebotMessage.setUser(robotUser);
		oldProposal = proposalDao.save(oldProposal);
		return oldProposal;
	}
	
	public Proposal getAppliedProposal(Long influencerId, Long campaignId) {
		return proposalDao.findByInfluencerIdAndCampaignCampaignId(influencerId,campaignId);
	}

}
