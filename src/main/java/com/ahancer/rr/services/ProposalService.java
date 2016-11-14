package com.ahancer.rr.services;

import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.response.ProposalCountResponse;
import com.ahancer.rr.response.ProposalDashboardResponse;
import com.ahancer.rr.response.ProposalResponse;
import com.ahancer.rr.response.UserResponse;
import com.ahancer.rr.services.impl.ProposalServiceImpl.DeferredProposal;

@Service
public interface ProposalService {
	public void addInboxPolling(Long userId, DeferredProposal p) throws Exception;
	public void processInboxPolling(Long userId) throws Exception;
	public List<Proposal> findAllByBrand(Long brandId, Long campaignId) throws Exception;
	public Long countByUnreadProposalForBrand(Long brandId) throws Exception;
	public Long countByUnreadProposalForInfluencer(Long influencerId) throws Exception;
	public Long countByUnreadProposalMessageForBrand(Long proposalId, Long brandId) throws Exception;
	public Long countByUnreadProposalMessageForInfluencer(Long proposalId, Long influencerId) throws Exception;
	public ProposalCountResponse countByBrand(Long brandId, ProposalStatus status) throws Exception;
	public ProposalCountResponse countByInfluencer(Long influencerId, ProposalStatus status) throws Exception;
	public ProposalCountResponse countByAdmin(ProposalStatus status) throws Exception;
	public Page<ProposalResponse> findAllByBrand(Long brandId,ProposalStatus status, String search,Pageable pageable) throws Exception;
	public ProposalResponse findOneByAdmin(Long proposalId) throws Exception;
	public ProposalResponse findOneByBrand(Long proposalId,Long brandId) throws Exception;
	public ProposalResponse findOneByInfluencer(Long proposalId,Long influencerId) throws Exception;
	public List<ProposalResponse> findAllActiveByInfluencer(Long influencerId) throws Exception;
	public Page<ProposalResponse> findAllByInfluencer(Long influencerId,ProposalStatus status, String search,Pageable pageable) throws Exception;
	public Page<ProposalResponse> findAllByAdmin(ProposalStatus status, String search, Pageable pageable) throws Exception;
	public Page<Proposal> findAll(Pageable pageable) throws Exception;
	public Proposal createCampaignProposalByInfluencer(Long campaignId, Proposal proposal,UserResponse user,Locale locale) throws Exception;
	public ProposalResponse updateCampaignProposalByInfluencer(Long proposalId, Proposal proposal,UserResponse user, Locale local) throws Exception;
	public ProposalResponse updateProposalStatusByBrand(Long proposalId,ProposalStatus status, Long brandId, Locale locale) throws Exception;
	public Proposal getAppliedProposal(Long influencerId, Long campaignId) throws Exception;
	public void dismissProposalNotification(Long proposalId, Long influencerId) throws Exception;
	public List<ProposalDashboardResponse> getProposalFromCampaignByAdmin(Long campaignId) throws Exception;
	public List<ProposalDashboardResponse> getProposalFromCampaignByBrand(Long campaignId, Long brandId) throws Exception;
	public Page<Proposal> getReferralProposal(String search, Pageable pageable) throws Exception;
	public Proposal payReferralProposal(Long proposalId) throws Exception;

}
