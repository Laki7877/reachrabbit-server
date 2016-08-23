package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Category;

public interface CategoryDao extends CrudRepository<Category, Long>{
	
	public List<Category> findAllByOrderByCategoryId();

}
