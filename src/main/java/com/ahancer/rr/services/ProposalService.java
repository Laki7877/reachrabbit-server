package com.ahancer.rr.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Brand;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.Influencer;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;
import com.ahancer.rr.models.User;

@Service
@Transactional(rollbackFor=Exception.class)
public class ProposalService {

	@Autowired
	private ProposalDao proposalDao;

	@Autowired
	private ProposalMessageDao proposalMessageDao;


	@Autowired
	private CampaignDao campaignDao;

	public Page<Proposal> findAllByBrand(Brand brand,Pageable pageable) {
		return proposalDao.findByCampaignBrand(brand, pageable);
	}

	public Proposal findOneByBrand(Long proposalId,Brand brand) {
		return proposalDao.findByProposalIdAndCampaignBrand(proposalId,brand);
	}

	public Proposal findOneByInfluencer(Long proposalId,Influencer influencer) {
		return proposalDao.findByProposalIdAndInfluencer(proposalId,influencer);
	}

	public Page<Proposal> findAllByInfluencer(Influencer influencer,Pageable pageable) {
		return proposalDao.findByInfluencer(influencer, pageable);
	}

	public Page<Proposal> findAll(Pageable pageable) {
		return proposalDao.findAll(pageable);
	}

	public Proposal createCampaignProposalByInfluencer(Long campaignId, Proposal proposal,Influencer influencer) throws Exception {
		Campaign campaign = campaignDao.findOne(campaignId);
		int count = proposalDao.countByInfluencerAndCampaign(influencer.getInfluencerId(), campaign.getCampaignId());
		if(0 < count){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.already.proposal");
		}
		proposal.setCampaign(campaign);
		proposal.setInfluencer(influencer);
		proposal.setMessageUpdatedAt(new Date());
		proposal = proposalDao.save(proposal);
		//Insert first message
		ProposalMessage firstMessage = new ProposalMessage();
		firstMessage.setIsBrandRead(false);
		firstMessage.setIsInfluencerRead(true);
		firstMessage.setMessage(proposal.getDescription());
		firstMessage.setProposal(proposal);
		User user = new User();
		user.setUserId(influencer.getInfluencerId());
		firstMessage.setUser(user);
		firstMessage = proposalMessageDao.save(firstMessage);
		return proposal;
	}

	public Proposal updateCampaignProposalByInfluencer(Long proposalId, Proposal proposal,Influencer influencer) throws Exception {
		Proposal oldProposal = findOneByInfluencer(proposalId,influencer);
		if(null == oldProposal){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.proposal.not.exist");
		}
		oldProposal.setMedia(proposal.getMedia());
		oldProposal.setCompletionTime(proposal.getCompletionTime());
		oldProposal.setPrice(proposal.getPrice());
		oldProposal.setDescription(proposal.getDescription());
		oldProposal = proposalDao.save(oldProposal);
		return oldProposal;
	}

}
