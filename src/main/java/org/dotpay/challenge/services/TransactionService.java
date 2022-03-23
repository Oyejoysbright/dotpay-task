package org.dotpay.challenge.services;

import java.text.DecimalFormat;
import java.util.List;

import org.dotpay.challenge.dtos.TransactionRequest;
import org.dotpay.challenge.entities.Customer;
import org.dotpay.challenge.entities.Transaction;
import org.dotpay.challenge.enums.TransactionStatus;
import org.dotpay.challenge.enums.TransferType;
import org.dotpay.challenge.repos.CustomerRepo;
import org.dotpay.challenge.repos.TransactionRepo;
import org.dotpay.challenge.utils.Helper;
import org.dotpay.challenge.utils.ServerResponse;
import org.dotpay.challenge.utils.ServerResponse.ResponseMessage;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepo transactionRepo;
    private final CustomerRepo customerRepo;

    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    static double getTransactionFee(double amount) {
        String fee = String.valueOf(((0.5 / 100) * amount));
        // Format the decimal result
        fee = decimalFormat.format(fee);
        return Double.parseDouble(fee);
    }

    void moveFundToReserve(Customer customer, double amount) {
        customer.setBalance(customer.getBalance() - amount);
        customer.setReservedBalance(customer.getReservedBalance() + amount);
    }

    void debitAccount(Customer customer, double amount) {
        customer.setReservedBalance(customer.getReservedBalance() - amount);
        customerRepo.save(customer);
    }

    void creditAccount(Customer customer, double amount) {
        customer.setReservedBalance(customer.getReservedBalance() - amount);
        customer.setBalance(customer.getBalance() + amount);
        customerRepo.save(customer);
    }

    public ResponseEntity<ResponseMessage<Object>> performTransfer(TransactionRequest payload) {
        try {
            Transaction transaction = new Transaction();
            BeanUtils.copyProperties(payload, transaction);
            transaction.setTransactionRef(Helper.randomString(20));

            try {
                Customer senderAccount = customerRepo.findByAccountNumber(transaction.getSenderAccountNumber());
                Customer beneficiaryAccount = customerRepo
                        .findByAccountNumber(transaction.getBeneficiaryAccountNumber());
                moveFundToReserve(senderAccount, transaction.getAmount());
                moveFundToReserve(beneficiaryAccount, transaction.getAmount());
                if (senderAccount.getBankCode().equalsIgnoreCase(beneficiaryAccount.getBankCode())) {
                    transaction.setTransferType(TransferType.INTRA);
                } else {
                    transaction.setTransferType(TransferType.INTER);
                }
                debitAccount(senderAccount, transaction.getAmount());
                creditAccount(beneficiaryAccount, transaction.getAmount());
                transaction.setTransactionFee(getTransactionFee(transaction.getAmount()));
                transaction.setBilledAmount(transaction.getAmount() - transaction.getTransactionFee());
                transaction.setStatus(TransactionStatus.SUCCESSFUL);

            } catch (Exception e) {
                transaction.setStatus(TransactionStatus.FAILED);
                return ServerResponse.failedResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Transaction failed");
            } finally {
                transactionRepo.save(transaction);
            }

            return ServerResponse.successfulResponse("Transaction successful", transaction.getTransactionRef());
        } catch (Exception e) {
            return ServerResponse.failedResponse(e);
        }
    }

    public ResponseEntity<ResponseMessage<Object>> getTransactionHistory(
            TransactionStatus status, String senderAccountNumber, String receiverAccountNumber, String startDate,
            String endDate, TransferType type) {
        try {
            List<Transaction> transactions = transactionRepo
                    .findByStatusAndSenderAccountNumberAndReceiverAccountNumberAndTransferTypeAndCreatedAtBetween(
                            status, senderAccountNumber, receiverAccountNumber, type, startDate, endDate);
            return ServerResponse.successfulResponse("Transactions fetched", transactions);
        } catch (Exception e) {
            return ServerResponse.failedResponse(e);
        }
    }
}
