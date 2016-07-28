package com.ahancer.rr.daos;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.ahancer.rr.models.Brand;

@Transactional
public interface BrandDao extends CrudRepository<Brand, Long> {

}
