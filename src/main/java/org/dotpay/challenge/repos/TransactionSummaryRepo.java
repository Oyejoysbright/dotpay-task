package org.dotpay.challenge.repos;

import org.dotpay.challenge.entities.TransactionSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionSummaryRepo extends JpaRepository<TransactionSummary, String> {

    TransactionSummary findByCreatedAt(String date);
    
}
