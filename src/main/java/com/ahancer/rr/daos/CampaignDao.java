package com.ahancer.rr.daos;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.web.PageableDefault;

import com.ahancer.rr.models.Campaign;

@Transactional
public interface CampaignDao extends CrudRepository<Campaign, Long>{
	@Query("SELECT c from campaign c WHERE c.brandId = :brandId")
	Page<Campaign> findAllByBrand(Long brandId, @PageableDefault(page=0, value=10) Pageable pageable);
}
