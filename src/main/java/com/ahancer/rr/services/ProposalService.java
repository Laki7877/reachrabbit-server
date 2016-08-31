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
import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.models.User;

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

	public Proposal findOneByBrand(Long proposalId,Long brandId) {
		return proposalDao.findByProposalIdAndCampaignBrandId(proposalId,brandId);
	}

	public Proposal findOneByInfluencer(Long proposalId,Long influencerId) {
		return proposalDao.findByProposalIdAndInfluencerId(proposalId,influencerId);
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

	public Proposal createCampaignProposalByInfluencer(Long campaignId, Proposal proposal,Long influencerId) throws Exception {
		Campaign campaign = campaignDao.findOne(campaignId);
		long count = proposalDao.countByInfluencerInfluencerIdAndCampaignCampaignId(influencerId, campaign.getCampaignId());
		if(0 < count){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.already.proposal");
		}
		proposal.setCampaign(campaign);
		proposal.setInfluencerId(influencerId);
		proposal.setMessageUpdatedAt(new Date());
		proposal.setStatus(ProposalStatus.Selection);
		proposal = proposalDao.save(proposal);
		//Insert first message
		ProposalMessage firstMessage = new ProposalMessage();
		firstMessage.setIsBrandRead(false);
		firstMessage.setIsInfluencerRead(true);
		firstMessage.setMessage(proposal.getDescription());
		firstMessage.setProposal(proposal);
		firstMessage.setUserId(influencerId);
		firstMessage = proposalMessageDao.save(firstMessage);
		return proposal;
	}

	public Proposal updateCampaignProposalByInfluencer(Long proposalId, Proposal proposal,Long influencerId, Locale local) throws Exception {
		Proposal oldProposal = findOneByInfluencer(proposalId,influencerId);
		if(null == oldProposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		oldProposal.setMedia(proposal.getMedia());
		oldProposal.setCompletionTime(proposal.getCompletionTime());
		oldProposal.setPrice(proposal.getPrice());
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

	public Proposal updateProposalStatusByBrand(Long proposalId,ProposalStatus status, Long brandId, Locale local) throws Exception {
		Proposal oldProposal = findOneByBrand(proposalId,brandId);
		if(null == oldProposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		proposalDao.updateProposalStatus(proposalId, status);
		oldProposal.setStatus(status);
		
		ProposalMessage rebotMessage = new ProposalMessage();
		rebotMessage.setIsBrandRead(true);
		rebotMessage.setIsInfluencerRead(true);
		Calendar cal = Calendar.getInstance();
		if(ProposalStatus.Working.equals(oldProposal.getStatus())
				|| ProposalStatus.Selection.equals(oldProposal.getStatus())){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.invalid.status");
		} else if(ProposalStatus.Complete.equals(oldProposal.getStatus())){
			rebotMessage.setMessage(messageSource.getMessage("robot.proposal.complete.status.message", null, local));
			oldProposal.setCompleteDate(cal.getTime());
		}
		rebotMessage.setProposal(oldProposal);
		rebotMessage.setUserId(robotService.getRobotUser().getUserId());
		rebotMessage = proposalMessageDao.save(rebotMessage);
		oldProposal = proposalDao.save(oldProposal);
		return oldProposal;
	}
	
	public Proposal getAppliedProposal(Long influencerId, Long campaignId) {
		return proposalDao.findByInfluencerIdAndCampaignCampaignId(influencerId,campaignId);
	}

}
