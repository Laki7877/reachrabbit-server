package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.User;


public interface UserDao extends CrudRepository<User, Long> {
	 public User findByEmail(String email);
	 
}
