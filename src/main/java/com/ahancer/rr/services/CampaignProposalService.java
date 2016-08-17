package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.CampaignProposalDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.CampaignProposal;
import com.ahancer.rr.models.Influencer;

@Service
@Transactional(rollbackFor=Exception.class)
public class CampaignProposalService {
	
	@Autowired
	private CampaignProposalDao campaignProposalDao;
	
	public Page<CampaignProposal> findAll(Pageable pageable) {
		return campaignProposalDao.findAll(pageable);
	}
	
	public CampaignProposal createCampaignProposalByInfluencer(CampaignProposal proposal,Influencer influencer) throws Exception {
		Campaign campaign = proposal.getCampaign();
		int count = campaignProposalDao.countByInfluencerAndCampaign(influencer.getInfluencerId(), campaign.getCampaignId());
		if(0 < count){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.already.proposal");
		}
		proposal.setInfluencer(influencer);
		proposal = campaignProposalDao.save(proposal);
		return proposal;
	}

}
