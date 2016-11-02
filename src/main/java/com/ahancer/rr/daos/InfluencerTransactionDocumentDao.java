package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.InfluencerTransactionDocument;

@Repository
public interface InfluencerTransactionDocumentDao  extends CrudRepository<InfluencerTransactionDocument, Long> {

}
