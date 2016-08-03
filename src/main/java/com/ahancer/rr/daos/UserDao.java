package com.ahancer.rr.daos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.ahancer.rr.models.User;


public interface UserDao extends CrudRepository<User, Long> {
	 public User findByEmail(String email);
	 
	 @Query("SELECT COUNT(u) FROM user u WHERE u.email=:email")
	 public int countByEmail(@Param("email") String email);
	 
}
