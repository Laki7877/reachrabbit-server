package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.BrandTransactionDocument;

@Repository
public interface BrandTransactionDocumentDao extends CrudRepository<BrandTransactionDocument, Long> {

}
