package com.ahancer.rr.daos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.User;


public interface UserDao extends CrudRepository<User, Long> {
	
	public User findByEmail(String email);
	
	@Query("SELECT u FROM user u "
			+ "LEFT JOIN u.profilePicture r "
			+ "JOIN u.influencer i "
			+ "JOIN i.influencerMedias im "
			+ "LEFT JOIN i.categories ic "
			+ "WHERE im.influencerMediaId.mediaId=:mediaId "
			+ "AND im.socialId=:socialId")
	public User findBySocialIdAndMediaId(@Param("mediaId") String mediaId,@Param("socialId") String socialId);

	@Query("SELECT COUNT(u) "
			+ "FROM user u "
			+ "WHERE u.email=:email")
	public int countByEmail(@Param("email") String email);

}
