package com.ahancer.rr.daos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ahancer.rr.models.ReferralTransactionDocument;

@Repository
public interface ReferralTransactionDocumentDao extends CrudRepository<ReferralTransactionDocument, Long> {

}
