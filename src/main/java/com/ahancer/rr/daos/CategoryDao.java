package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Category;

public interface CategoryDao extends CrudRepository<Category, Long>{

}
