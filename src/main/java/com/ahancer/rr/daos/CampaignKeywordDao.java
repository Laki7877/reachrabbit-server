package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.CampaignKeyword;

public interface CampaignKeywordDao extends CrudRepository<CampaignKeyword, Long> {
	
	
//	@Query("DELETE campaignKeyword ck WHERE ck.campaignId=:campaignId")
//	public void deleteByCampaign(@Param("campaignId") Long campaignId);

}
