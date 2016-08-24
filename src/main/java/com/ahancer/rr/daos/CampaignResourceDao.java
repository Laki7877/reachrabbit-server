package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.CampaignResource;
import com.ahancer.rr.models.CampaignResourceId;

public interface CampaignResourceDao extends CrudRepository<CampaignResource, CampaignResourceId> {

}
