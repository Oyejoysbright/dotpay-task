package org.dotpay.challenge.repos;

import java.util.Date;
import java.util.List;

import org.dotpay.challenge.entities.Transaction;
import org.dotpay.challenge.enums.TransactionStatus;
import org.dotpay.challenge.enums.TransferType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String> {

    List<Transaction> findByStatusContainsAndSenderAccountNumberContainsAndBeneficiaryAccountNumberContainsAndTransferTypeContainsAndCreatedAtBetween(
            TransactionStatus status, Integer senderAccountNumber, Integer receiverAccountNumber, TransferType type,
            Date startDate, Date endDate);

    List<Transaction> findByCreatedAt(Date date);

    List<Transaction> findByStatusContainsAndSenderAccountNumberContainsAndBeneficiaryAccountNumberContainsAndTransferTypeContains(
            TransactionStatus status, Integer senderAccountNumber, Integer receiverAccountNumber, TransferType type);

    List<Transaction> findByStatusAndIdIn(TransactionStatus status, List<String> transactionsIds);

    List<Transaction> findBySenderAccountNumberAndIdIn(Integer senderAccountNumber,
            List<String> transactionsIds);

    List<Transaction> findByTransferTypeAndIdIn(TransferType type, List<String> transactionsIds);

    List<Transaction> findByBeneficiaryAccountNumberAndIdIn(Integer receiverAccountNumber,
            List<String> transactionsIds);

    List<Transaction> findByIdInAndCreatedAtBetween(List<String> transactionsIds, Date parseDate,
            Date parseDate2);

    List<Transaction> findByStatus(TransactionStatus status);

    List<Transaction> findByTransferType(TransferType type);

    List<Transaction> findByCreatedAtBetween(Date parseDate, Date parseDate2);

    List<Transaction> findByBeneficiaryAccountNumber(Integer beneficiaryAccountNumber);

    List<Transaction> findBySenderAccountNumber(Integer senderAccountNumber);
}
