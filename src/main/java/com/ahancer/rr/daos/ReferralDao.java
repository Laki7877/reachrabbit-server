package com.ahancer.rr.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.Referral;
import com.ahancer.rr.response.ReferralResponse;

public interface ReferralDao extends CrudRepository<Referral, String> {
	public Long countByReferralId(String referralId);
	
	@Query("SELECT new com.ahancer.rr.response.ReferralResponse(r,'Admin') "
			+ "FROM referral r ")
	public Page<ReferralResponse> findAll(Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.ReferralResponse(r,'Admin') "
			+ "FROM referral r "
			+ "WHERE r.referralId LIKE CONCAT('%' , :search , '%') "
			+ "OR r.description   LIKE CONCAT('%' , :search , '%') "
			+ "OR r.user.email    LIKE CONCAT('%' , :search , '%') ")
	public Page<ReferralResponse> findAllBySearch(@Param("search") String search, Pageable pageable);
	
	
	
}
