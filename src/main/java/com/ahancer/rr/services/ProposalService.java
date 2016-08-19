package com.ahancer.rr.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ahancer.rr.custom.type.ProposalStatus;
import com.ahancer.rr.daos.CampaignDao;
import com.ahancer.rr.daos.ProposalDao;
import com.ahancer.rr.daos.ProposalMessageDao;
import com.ahancer.rr.exception.ResponseException;
import com.ahancer.rr.models.Campaign;
import com.ahancer.rr.models.Proposal;
import com.ahancer.rr.models.ProposalMessage;

@Service
@Transactional(rollbackFor=Exception.class)
public class ProposalService {

	@Autowired
	private ProposalDao proposalDao;

	@Autowired
	private ProposalMessageDao proposalMessageDao;


	@Autowired
	private CampaignDao campaignDao;

	public Page<Proposal> findAllByBrand(Long brandId,Pageable pageable) {
		return proposalDao.findByCampaignBrandId(brandId, pageable);
	}

	public Proposal findOneByBrand(Long proposalId,Long brandId) {
		return proposalDao.findByProposalIdAndCampaignBrandId(proposalId,brandId);
	}

	public Proposal findOneByInfluencer(Long proposalId,Long influencerId) {
		return proposalDao.findByProposalIdAndInfluencerId(proposalId,influencerId);
	}

	public Page<Proposal> findAllByInfluencer(Long influencerId,Pageable pageable) {
		return proposalDao.findByInfluencerId(influencerId, pageable);
	}

	public Page<Proposal> findAll(Pageable pageable) {
		return proposalDao.findAll(pageable);
	}

	public Proposal createCampaignProposalByInfluencer(Long campaignId, Proposal proposal,Long influencerId) throws Exception {
		Campaign campaign = campaignDao.findOne(campaignId);
		int count = proposalDao.countByInfluencerAndCampaign(influencerId, campaign.getCampaignId());
		if(0 < count){
			throw new ResponseException(HttpStatus.BAD_REQUEST,"error.campaign.already.proposal");
		}
		proposal.setCampaign(campaign);
		proposal.setInfluencerId(influencerId);
		proposal.setMessageUpdatedAt(new Date());
		proposal.setStatus(ProposalStatus.Selection);
		proposal = proposalDao.save(proposal);
		//Insert first message
		ProposalMessage firstMessage = new ProposalMessage();
		firstMessage.setIsBrandRead(false);
		firstMessage.setIsInfluencerRead(true);
		firstMessage.setMessage(proposal.getDescription());
		firstMessage.setProposal(proposal);
		firstMessage.setUserId(influencerId);
		firstMessage = proposalMessageDao.save(firstMessage);
		return proposal;
	}

	public Proposal updateCampaignProposalByInfluencer(Long proposalId, Proposal proposal,Long influencerId) throws Exception {
		Proposal oldProposal = findOneByInfluencer(proposalId,influencerId);
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
