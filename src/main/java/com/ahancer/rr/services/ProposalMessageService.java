package com.ahancer.rr.services;

import java.util.Date;
import java.util.Queue;
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
	
	public static class DeferredProposalMessage extends DeferredResult<Page<ProposalMessage>> {
		private Pageable pageable;
		private Long proposalId;
		public DeferredProposalMessage(Long proposalId, Pageable pageable) {
			this.pageable = pageable;
			this.proposalId = proposalId;
		}
		public Pageable getPageable() {
			return pageable;
		}
		public void setPageable(Pageable pageable) {
			this.pageable = pageable;
		}
		public Long getProposalId() {
			return proposalId;
		}
		public void setProposalId(Long proposalId) {
			this.proposalId = proposalId;
		}
	}
	
	@Autowired
	private ProposalMessageDao proposalMessageDao;
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private ProposalDao proposalDao;
	
	private Queue<DeferredProposalMessage> pollingQueue;
	
	public ProposalMessageService() {
		pollingQueue = new ConcurrentLinkedQueue<>();
	}
	
	public void addPollingQueue(DeferredProposalMessage p) {
		pollingQueue.add(p);
	}
	
	public void processPollingQueue() {
		for(DeferredProposalMessage result: pollingQueue) {
			result.setResult(findByProposal(result.getProposalId(), result.getPageable()));
			pollingQueue.remove(result);
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
	
	public Page<ProposalMessage> findByProposal(Long proposalId,Pageable pageable) {
		Page<ProposalMessage> messages = proposalMessageDao.findByProposalProposalId(proposalId, pageable);
		return messages;
	}

}
