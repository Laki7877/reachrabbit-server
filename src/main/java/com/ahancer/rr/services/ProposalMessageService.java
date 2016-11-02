package com.ahancer.rr.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.response.ProposalMessageResponse;
import com.ahancer.rr.services.impl.ProposalMessageServiceImpl.DeferredProposalMessage;

@Service
public interface ProposalMessageService {
	public void addMessagePolling(Long proposalId, DeferredProposalMessage p) throws Exception;
	public void processMessagePolling(Long proposalId) throws Exception;
	public Long countNewProposalMessage(Long proposalId, Date timestamp) throws Exception;
	public List<ProposalMessageResponse> getNewProposalMessageByBrand(Long proposalId, Long brandId, Date timestamp) throws Exception;
	public List<ProposalMessageResponse> getNewProposalMessageByInfluencer(Long proposalId, Long influencerId, Date timestamp) throws Exception;
	public ProposalMessage createProposalMessage(Long proposalId, ProposalMessage message,Long userId,Role userRole) throws Exception;
	public Page<ProposalMessageResponse> findByProposalForBrand(Long proposalId, Long brandId, Date before, Pageable pageable) throws Exception;
	public Page<ProposalMessageResponse> findByProposalForInfluencer(Long proposalId, Long influencerId, Date before, Pageable pageable) throws Exception;
	public Page<ProposalMessageResponse> findByAdmin(Long proposalId, Date before, Pageable pageable) throws Exception;

}
