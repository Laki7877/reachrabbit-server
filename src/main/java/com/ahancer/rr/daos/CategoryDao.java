package com.ahancer.rr.daos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.Category;

@Repository
public interface CategoryDao extends CrudRepository<Category, Long>{
	public List<Category> findAllByIsActiveTrueOrderByCategoryId();
}
