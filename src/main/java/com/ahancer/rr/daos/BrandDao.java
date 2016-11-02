package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.Brand;

@Repository
public interface BrandDao extends CrudRepository<Brand, Long> {

}
