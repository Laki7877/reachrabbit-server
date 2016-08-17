package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
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

}
