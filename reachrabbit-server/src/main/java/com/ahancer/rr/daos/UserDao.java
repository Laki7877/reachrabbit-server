package com.ahancer.rr.daos;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import com.ahancer.rr.models.User;


@Transactional
public interface UserDao extends CrudRepository<User, Long> {
	 public User findByEmail(String email);
	 
}
