package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.models.User;

@Service
@Transactional(rollbackFor=Exception.class)
public class ProposalMessageService {
	
	@Autowired
	private ProposalMessageDao proposalMessageDao;
	
	public ProposalMessage createProposalMessage(ProposalMessage messaage,User user) throws Exception {
		messaage.setUser(user);
		messaage = proposalMessageDao.save(messaage);
		return messaage;
	}
	
	public Page<ProposalMessage> findByProposal(Long proposalId,Pageable pageable) {
		Page<ProposalMessage> messages = proposalMessageDao.findByProposalProposalId(proposalId, pageable);
		return messages;
	}

}
