package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.ProposalMessage;

public interface ProposalMessageDao extends CrudRepository<ProposalMessage, Long> {
	
}
