package com.ahancer.rr.services;

import java.util.Collections;
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

@Service
@Transactional(rollbackFor=Exception.class)
public class ProposalMessageService {
	
	public static class DeferredProposalMessage extends DeferredResult<List<ProposalMessage>> {
		private Date timestamp;
		private Long proposalId;
		private final static long timeout = 5000;
		public DeferredProposalMessage(Long proposalId, Date timestamp) {
			super(timeout, Collections.emptyList());
			this.timestamp = timestamp;
			this.proposalId = proposalId;
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
	
	private Map<Long,ConcurrentLinkedQueue<DeferredProposalMessage>> pollingQueue;
	
	public ProposalMessageService() {
		pollingQueue = new ConcurrentHashMap<>();
	}
	
	public void addPollingQueue(Long proposalId, DeferredProposalMessage p) {
		//TODO: optimize
		if(pollingQueue.get(proposalId) == null) {
			pollingQueue.put(proposalId, new ConcurrentLinkedQueue<>());
		}
		pollingQueue.get(proposalId).add(p);
		
		//Remove when done
		p.onCompletion(new Runnable() {
			public void run() {
				pollingQueue.get(proposalId).remove(p);
			}
		});
	}
	
	public void processPollingQueue(Long proposalId) {
		
		if(pollingQueue.get(proposalId) == null) {
			return;
		}
		//Force queue update
		for(DeferredProposalMessage m : pollingQueue.get(proposalId)) {
			m.setResult(proposalMessageDao.findByProposalProposalIdAndCreatedAtAfterOrderByCreatedAtDesc(proposalId, m.getTimestamp()));
		}
	}
	
	public ProposalMessage createProposalMessage(Long proposalId, ProposalMessage message,Long userId,Role userRole) throws Exception {
		int updateCount = 0;
		if(Role.Influencer == userRole){
			updateCount = proposalDao.updateMessageUpdatedAtByInfluencer(proposalId, userId,new Date());
		}else if(Role.Brand == userRole){
			int porposalCount = proposalDao.countByBrand(proposalId,userId);
			if(0 >= porposalCount){
				throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
			}
			updateCount = proposalDao.updateMessageUpdatedAtByBrand(proposalId,new Date());
		}
		if(0 >= updateCount) {
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		message.setIsInfluencerRead(false);
		message.setIsBrandRead(false);
		if(Role.Influencer == userRole){
			message.setIsInfluencerRead(true);
			message.setIsBrandRead(false);
		}
		else if(Role.Brand == userRole){
			message.setIsInfluencerRead(false);
			message.setIsBrandRead(true);
		}
		message.setUserId(userId);
		message = proposalMessageDao.save(message);
		message.setUser(userDao.findOne(userId));
		return message;
	}
	public Page<ProposalMessage> findByProposal(Long proposalId, Date before, Pageable pageable) {
		if(before == null) {
			return findByProposal(proposalId, pageable);
		} else {
			return proposalMessageDao.findByProposalProposalIdAndCreatedAtBefore(proposalId, before, pageable);
		}
	}
	public Page<ProposalMessage> findByProposal(Long proposalId, Pageable pageable) {
		Page<ProposalMessage> messages = proposalMessageDao.findByProposalProposalId(proposalId, pageable);
		return messages;
	}

}
