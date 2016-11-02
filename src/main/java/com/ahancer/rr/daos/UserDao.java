package com.ahancer.rr.daos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.custom.type.Role;
import com.ahancer.rr.models.User;
import com.ahancer.rr.response.UserResponse;

@Repository
public interface UserDao extends CrudRepository<User, Long> {
	
	public User findByEmailAndRole(String email,Role role);
	
	@Query("SELECT u FROM user u "
			+ "LEFT JOIN u.profilePicture r "
			+ "JOIN u.influencer i "
			+ "JOIN i.influencerMedias im "
			+ "LEFT JOIN i.categories ic "
			+ "WHERE im.influencerMediaId.mediaId=:mediaId "
			+ "AND im.socialId=:socialId")
	public User findBySocialIdAndMediaId(@Param("mediaId") String mediaId,@Param("socialId") String socialId);

	public int countByEmailAndRole(@Param("email") String email,@Param("role") Role role);
	
	@Query("SELECT new com.ahancer.rr.response.UserResponse(u, 'Admin') " 
			+ "FROM user u "
			+ "WHERE u.role='Brand' ")
	public Page<UserResponse> findAllBrand(Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.UserResponse(u, 'Admin') " 
			+ "FROM user u "
			+ "JOIN u.brand b "
			+ "WHERE u.role='Brand' "
			+ "AND (u.email LIKE CONCAT('%', :search, '%') OR b.brandName LIKE CONCAT('%', :search, '%'))")
	public Page<UserResponse> findAllBrand(@Param("search") String search, Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.UserResponse(u, 'Admin') " 
			+ "FROM user u "
			+ "WHERE u.role='Influencer' ")
	public Page<UserResponse> findAllInfluencer(Pageable pageable);
	
	@Query("SELECT new com.ahancer.rr.response.UserResponse(u, 'Admin') " 
			+ "FROM user u "
			+ "WHERE u.role='Influencer' "
			+ "AND (u.email LIKE CONCAT('%', :search, '%') OR u.name LIKE CONCAT('%', :search, '%'))")
	public Page<UserResponse> findAllInfluencer(@Param("search") String search, Pageable pageable);
	
	public Long countByReferralReferralId(String referralId);
	
}
