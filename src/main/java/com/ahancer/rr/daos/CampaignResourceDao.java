package com.ahancer.rr.daos;

import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.CampaignResource;
import com.ahancer.rr.models.CampaignResourceId;

@Repository
public interface CampaignResourceDao extends CrudRepository<CampaignResource, CampaignResourceId> {
	
	public Set<CampaignResource> findByIdCampaignId(Long campaignId);
	
	@Modifying
	@Query("DELETE "
			+ "FROM CampaignResource cr "
			+ "WHERE cr.id.campaignId=:campaignId")
	public void deleteByIdCampaignId(@Param("campaignId") Long campaignId);
	
	@Modifying
	@Query(value = "INSERT INTO campaignResource (campaignId, resourceId, position) "
			+ "VALUES (:campaignId, :resourceId, :position)", nativeQuery = true)
	public void insertResource(@Param("campaignId") Long campaignId, @Param("resourceId") Long resourceId, @Param("position") Integer position);
}
