package com.ahancer.rr.services;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.async.DeferredResult;

import com.ahancer.rr.daos.ProposalMessageDao;
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
	
	public ProposalMessage createProposalMessage(ProposalMessage messaage,Long userId) throws Exception {
		messaage.setUserId(userId);
		messaage = proposalMessageDao.save(messaage);
		return messaage;
	}
	
	public Page<ProposalMessage> findByProposal(Long proposalId,Pageable pageable) {
		Page<ProposalMessage> messages = proposalMessageDao.findByProposalProposalId(proposalId, pageable);
		return messages;
	}

}
