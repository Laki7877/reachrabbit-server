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
	
	public ProposalMessage createProposalMessage(Long proposalId, ProposalMessage messaage,Long userId,Role userRole) throws Exception {
		int updateCount = 0;
		if(Role.Influencer == userRole){
			updateCount = proposalDao.updateMessageUpdatedAt(proposalId, userId, -1L,new Date());
		}else if(Role.Brand == userRole){
			updateCount = proposalDao.updateMessageUpdatedAt(proposalId, -1L, userId,new Date());
		}
		if(0 >= updateCount) {
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		messaage.setIsInfluencerRead(false);
		messaage.setIsBrandRead(false);
		if(Role.Influencer == userRole){
			messaage.setIsInfluencerRead(true);
			messaage.setIsBrandRead(false);
		}
		else if(Role.Brand == userRole){
			messaage.setIsInfluencerRead(false);
			messaage.setIsBrandRead(true);
		}
		messaage.setUserId(userId);
		messaage = proposalMessageDao.save(messaage);
		return messaage;
	}
	
	public Page<ProposalMessage> findByProposal(Long proposalId,Pageable pageable) {
		Page<ProposalMessage> messages = proposalMessageDao.findByProposalProposalId(proposalId, pageable);
		return messages;
	}

}
