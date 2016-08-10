package com.ahancer.rr.daos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.User;


public interface UserDao extends CrudRepository<User, Long> {
	 public User findByEmail(String email);
	 /*
	 @Query("SELECT * FROM user u"
	 		+ " LEFT JOIN influencer i on u.userId=i.influencerId"
	 		+ " LEFT JOIN influencerMedia im on i.influencerId=im.influencerId"
	 		+ " LEFT JOIN media m on im.mediaId=m.mediaId"
	 		+ " WHERE m.mediaId=:mediaId AND im.socialId=:socialId"
	 		+ " LIMIT 1")
	 public User findWithInfluencerMedia(@Param("socialId") String socialId, @Param("email") String mediaId);
	 */
	 @Query("SELECT COUNT(u) FROM user u WHERE u.email=:email")
	 public int countByEmail(@Param("email") String email);
	 
}
