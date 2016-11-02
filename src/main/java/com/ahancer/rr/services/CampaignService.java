package com.ahancer.rr.services;

import java.util.List;
import java.util.Locale;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.ahancer.rr.request.CampaignRequest;
import com.ahancer.rr.response.CampaignResponse;
import com.ahancer.rr.response.InfluencerResponse;
import com.ahancer.rr.response.UserResponse;

@Service
public interface CampaignService {
	public CampaignRequest createCampaignByBrand(CampaignRequest request, UserResponse user, Locale locale) throws Exception;
	public void deleteCampaign(Long campaignId, UserResponse user) throws Exception;
	public CampaignRequest updateCampaign(Long campaignId, CampaignRequest request) throws Exception;
	public CampaignRequest updateCampaignByBrand(Long campaignId, CampaignRequest request, UserResponse user, Locale locale) throws Exception;
	public Page<CampaignResponse> findAll(Pageable pageable) throws Exception;
	public Page<CampaignResponse> findAllByAdmin(Pageable pageable) throws Exception;
	public Page<CampaignResponse> findAllByBrand(Long brandId, String statusValue, String search, Pageable pageable) throws Exception;
	public List<CampaignResponse> findAllActiveByBrand(Long brandId) throws Exception;
	public Page<CampaignResponse> findAllOpen(String mediaFilter,InfluencerResponse influencer,Pageable pageable) throws Exception;
	public CampaignResponse findOneByInfluencer(Long campaignId, Long influencerId) throws Exception;
	public CampaignResponse findOneByAdmin(Long campaignId) throws Exception;
	public CampaignResponse findOneByPublic(String publicCode) throws Exception;
	public CampaignResponse findOneByBrand(Long campaignId, Long brandId) throws Exception;
	public void dismissCampaignNotification(Long campaignId, Long brandId) throws Exception;
	public void updatePublicCode() throws Exception;

}
