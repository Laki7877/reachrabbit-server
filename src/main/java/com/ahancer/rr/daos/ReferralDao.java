package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Referral;

public interface ReferralDao extends CrudRepository<Referral, String> {
	public Long countByReferralId(String referralId);
}
