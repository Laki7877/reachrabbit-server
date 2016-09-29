package com.ahancer.rr.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.daos.UserDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.response.ProposalMessageResponse;

@Service
@Transactional(rollbackFor=Exception.class)
public class ProposalMessageService {

	private final static Long timeout = 60000L;

	public static class DeferredProposalMessage extends DeferredResult<List<Date>> {
		private Date timestamp;
		private Long proposalId;
		private Role role;
		public DeferredProposalMessage(Long proposalId, Date timestamp, Role role) {
			super(timeout, null);
			this.role = role;
			this.timestamp = timestamp;
			this.proposalId = proposalId;
		}
		public Role getRole() {
			return role;
		}
		public void setRole(Role role) {
			this.role = role;
		}
		public Long getProposalId() {
			return proposalId;
		}
		public void setProposalId(Long proposalId) {
			this.proposalId = proposalId;
		}
		public Date getTimestamp() {
			return timestamp;
		}
		public void setTimestamp(Date timestamp) {
			this.timestamp = timestamp;
		}
		public void setResult(Date timestamp) {
			ArrayList<Date> array = new ArrayList<>();
			array.add(this.getTimestamp());
			array.add(new Date());
			super.setResult(array);
			
		}
	}

	@Autowired
	private ProposalService proposalService;
	
	@Autowired
	private ProposalMessageDao proposalMessageDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private ProposalDao proposalDao;

	private Map<Long,ConcurrentLinkedQueue<DeferredProposalMessage>> proposalMessagePollingMap;

	public ProposalMessageService() {
		proposalMessagePollingMap = new ConcurrentHashMap<>();
	}

	public void addMessagePolling(Long proposalId, DeferredProposalMessage p) {
		//TODO: optimize
		if(proposalMessagePollingMap.get(proposalId) == null) {
			proposalMessagePollingMap.put(proposalId, new ConcurrentLinkedQueue<>());
		}
		proposalMessagePollingMap.get(proposalId).add(p);
		//Remove when done
		p.onCompletion(new Runnable() {
			public void run() {
				proposalMessagePollingMap.get(proposalId).remove(p);
			}
		});
	}
	public void processMessagePolling(Long proposalId) {
		if(proposalMessagePollingMap.get(proposalId) == null) {
			return;
		}
		//Force queue update
		for(DeferredProposalMessage m : proposalMessagePollingMap.get(proposalId)) {
			m.setResult(m.getTimestamp());
		}
	}
	
	public Long countNewProposalMessage(Long proposalId, Date timestamp) {
		return proposalMessageDao.countByProposalProposalIdAndCreatedAtAfterOrderByCreatedAtDesc(proposalId, timestamp);
	}
	
	
	public List<ProposalMessageResponse> getNewProposalMessageByBrand(Long proposalId, Long brandId, Date timestamp) {
		List<ProposalMessageResponse> response = proposalMessageDao.findByProposalProposalIdAndProposalCampaignBrandIdAndCreatedAtAfterOrderByCreatedAtDesc(proposalId, brandId, timestamp);
		Function<ProposalMessageResponse, Long> transform = ProposalMessageResponse::getMessageId;
		List<Long> messageIds = response.stream().map(transform).collect(Collectors.toList());
		proposalMessageDao.updateBrandReadNewMessage(proposalId, messageIds, true);
		return response;
	}
	
	public List<ProposalMessageResponse> getNewProposalMessageByInfluencer(Long proposalId, Long influencerId, Date timestamp) {
		List<ProposalMessageResponse> response = proposalMessageDao.findByProposalProposalIdAndProposalInfluencerIdAndCreatedAtAfterOrderByCreatedAtDesc(proposalId, influencerId, timestamp);
		Function<ProposalMessageResponse, Long> transform = ProposalMessageResponse::getMessageId;
		List<Long> messageIds = response.stream().map(transform).collect(Collectors.toList());
		proposalMessageDao.updateInfluencerReadNewMessage(proposalId, messageIds, true);
		return response;
	}

	public ProposalMessage createProposalMessage(Long proposalId, ProposalMessage message,Long userId,Role userRole) throws Exception {
		int updateCount = 0;
		message.setIsInfluencerRead(false);
		message.setIsBrandRead(false);
		if(Role.Influencer.equals(userRole)){
			updateCount = proposalDao.updateMessageUpdatedAtByInfluencer(proposalId, userId,new Date());
			message.setIsInfluencerRead(true);
			message.setIsBrandRead(false);
		}else if(Role.Brand.equals(userRole)){
			long porposalCount = proposalDao.countByProposalIdAndCampaignBrandId(proposalId,userId);
			if(0 >= porposalCount){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
			}
			updateCount = proposalDao.updateMessageUpdatedAtByProposal(proposalId,new Date());
			message.setIsInfluencerRead(false);
			message.setIsBrandRead(true);
		}else if(Role.Bot.equals(userRole)){
			updateCount = proposalDao.updateMessageUpdatedAtByProposal(proposalId,new Date());
		}
		if(0 >= updateCount) {
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		message.setUserId(userId);
		message.setProposalId(proposalId);
		message = proposalMessageDao.save(message);
		message.setUser(userDao.findOne(userId));
		message.setProposal(proposalDao.findOne(proposalId));
		return message;
	}

	public Page<ProposalMessageResponse> findByProposalForBrand(Long proposalId, Long brandId, Date before, Pageable pageable) {
		Page<ProposalMessageResponse> response = null;
		if(null == before) {
			response = proposalMessageDao.findByProposalProposalIdAndProposalCampaignBrandId(proposalId, brandId, pageable);
		} else {
			response = proposalMessageDao.findByProposalProposalIdAndProposalCampaignBrandIdAndCreatedAtBefore(proposalId, brandId, before, pageable);
		}
		proposalMessageDao.updateBrandReadMessage(proposalId, true);
		proposalService.processInboxPolling(brandId);
		return response;
	}

	public Page<ProposalMessageResponse> findByProposalForInfluencer(Long proposalId, Long influencerId, Date before, Pageable pageable) {
		Page<ProposalMessageResponse> response = null;
		if(null == before) {
			response = proposalMessageDao.findByProposalProposalIdAndProposalInfluencerId(proposalId, influencerId, pageable);
		} else {
			response = proposalMessageDao.findByProposalProposalIdAndProposalInfluencerIdAndCreatedAtBefore(proposalId, influencerId, before, pageable);
		}
		proposalMessageDao.updateInfluencerReadMessage(proposalId, true);
		proposalService.processInboxPolling(influencerId);
		return response;
	}
}
