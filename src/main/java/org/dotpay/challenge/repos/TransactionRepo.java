package org.dotpay.challenge.repos;

import java.time.LocalDate;
import java.util.List;

import org.dotpay.challenge.entities.Transaction;
import org.dotpay.challenge.enums.TransactionStatus;
import org.dotpay.challenge.enums.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String> {

    List<Transaction> findByStatusAndSenderAccountNumberAndReceiverAccountNumberAndTransferTypeAndCreatedAtBetween(
            TransactionStatus status, String senderAccountNumber, String receiverAccountNumber, TransferType type,
            String startDate, String endDate);

    List<Transaction> findByCreatedAt(LocalDate date);
    
}
