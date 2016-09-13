package com.ahancer.rr.services;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

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
import com.google.api.client.util.Lists;

@Service
@Transactional(rollbackFor=Exception.class)
public class ProposalMessageService {

	private final static Long timeout = 60000L;

	public static class DeferredProposalMessage extends DeferredResult<Date> {
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
	}

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
	
	public List<ProposalMessage> getNewProposalMessage(Long proposalId, Role role, Date timestamp) {
		List<ProposalMessage> pm = proposalMessageDao.findByProposalProposalIdAndCreatedAtAfterOrderByCreatedAtDesc(proposalId, timestamp);
		for(ProposalMessage p : pm) {
			if(role.equals(Role.Influencer)) {
				p.setIsInfluencerRead(true);
			} else if(role.equals(Role.Brand)){
				p.setIsBrandRead(true);
			}
		}
		return Lists.newArrayList(proposalMessageDao.save(pm));
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
		message = proposalMessageDao.save(message);
		message.setUser(userDao.findOne(userId));
		message.setProposal(proposalDao.findOne(proposalId));
		return message;
	}

	public Page<ProposalMessage> findByProposalForBrand(Long proposalId, Long brandId, Date before, Pageable pageable) {
		Page<ProposalMessage> page = null;
		if(before == null) {
			page = proposalMessageDao.findByProposalProposalIdAndProposalCampaignBrandId(proposalId, brandId, pageable);
		} else {
			page = proposalMessageDao.findByProposalProposalIdAndProposalCampaignBrandIdAndCreatedAtBefore(proposalId, brandId, before, pageable);
		}
		//Update reading message
		List<ProposalMessage> pm = page.getContent();
		for(ProposalMessage p : pm) {
			p.setIsBrandRead(true);
		}
		proposalMessageDao.save(pm);
		return page;
	}

	public Page<ProposalMessage> findByProposalForInfluencer(Long proposalId, Long influencerId, Date before, Pageable pageable) {
		Page<ProposalMessage> page = null;
		if(before == null) {
			page = proposalMessageDao.findByProposalProposalIdAndProposalInfluencerId(proposalId, influencerId, pageable);
		} else {
			page = proposalMessageDao.findByProposalProposalIdAndProposalInfluencerIdAndCreatedAtBefore(proposalId, influencerId, before, pageable);
		}
		//Update reading message
		List<ProposalMessage> pm = page.getContent();
		for(ProposalMessage p : pm) {
			p.setIsInfluencerRead(true);
		}
		proposalMessageDao.save(pm);
		return page;
	}
}
