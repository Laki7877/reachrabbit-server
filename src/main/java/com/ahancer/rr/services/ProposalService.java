package com.ahancer.rr.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.Influencer;
import com.ahancer.rr.models.Proposal;

@Service
@Transactional(rollbackFor=Exception.class)
public class ProposalService {
	
	@Autowired
	private ProposalDao proposalDao;
	
	public Page<Proposal> findByBrand(Brand brand,Pageable pageable) {
		return proposalDao.findByCampaignBrand(brand, pageable);
	}
	
	public Page<Proposal> findByInfluencer(Influencer influencer,Pageable pageable) {
		return proposalDao.findByInfluencer(influencer, pageable);
	}
	
	
	
	public Page<Proposal> findAll(Pageable pageable) {
		return proposalDao.findAll(pageable);
	}
	
	public Proposal createCampaignProposalByInfluencer(Proposal proposal,Influencer influencer) throws Exception {
		Campaign campaign = proposal.getCampaign();
		int count = proposalDao.countByInfluencerAndCampaign(influencer.getInfluencerId(), campaign.getCampaignId());
		if(0 < count){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.already.proposal");
		}
		proposal.setInfluencer(influencer);
		proposal = proposalDao.save(proposal);
		return proposal;
	}

}
