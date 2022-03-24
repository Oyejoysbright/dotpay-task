package org.dotpay.challenge.services;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.dotpay.challenge.dtos.TransactionRequest;
import org.dotpay.challenge.entities.Customer;
import org.dotpay.challenge.entities.Transaction;
import org.dotpay.challenge.enums.TransactionStatus;
import org.dotpay.challenge.enums.TransferType;
import org.dotpay.challenge.repos.CustomerRepo;
import org.dotpay.challenge.repos.TransactionRepo;
import org.dotpay.challenge.utils.Helper;
import org.dotpay.challenge.utils.MockData;
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

    @PostConstruct
    void loadCustomers() {
        if (customerRepo.findAll().size() == 0) {
            customerRepo.saveAll(MockData.getCustomers());
        }
    }

    static double getTransactionFee(double amount) {
        double fee = (0.5 * amount) / 100;
        // Format the decimal result
        String formatted = Helper.decimalFormat.format(fee);
        return Double.parseDouble(formatted);
    }

    void moveFundToReserve(Customer customer, double amount) {
        customer.setBalance(customer.getBalance() - amount);
        customer.setReservedBalance(customer.getReservedBalance() + amount);
    }

    void debitAccount(Customer customer, double amount) {
        customer.setReservedBalance(customer.getReservedBalance() - amount);
    }

    void creditAccount(Customer customer, double amount) {
        customer.setBalance(customer.getBalance() + amount);
    }

    public ResponseEntity<ResponseMessage<Object>> performTransfer(TransactionRequest payload) {
        try {
            Transaction transaction = new Transaction();
            BeanUtils.copyProperties(payload, transaction);
            transaction.setTransactionRef(Helper.randomString(20));

            try {
                Customer senderAccount = customerRepo.findByAccountNumber(transaction.getSenderAccountNumber());
                if (senderAccount == null) {
                    return ServerResponse.failedResponse(HttpStatus.NOT_FOUND, "Sender account not found");
                } else if (senderAccount.getBalance() < transaction.getAmount()) {
                    transaction.setStatus(TransactionStatus.INSUFFICIENT_FUND);
                    return ServerResponse.failedResponse(HttpStatus.BAD_REQUEST, "Insufficient Fund");
                }
                Customer beneficiaryAccount = customerRepo
                        .findByAccountNumber(transaction.getBeneficiaryAccountNumber());
                if (beneficiaryAccount == null) {
                    return ServerResponse.failedResponse(HttpStatus.NOT_FOUND, "Sender account not found");
                }
                moveFundToReserve(senderAccount, transaction.getAmount());
                if (senderAccount.getBankCode() == beneficiaryAccount.getBankCode()) {
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

    private List<String> getTransactionsIds(List<Transaction> transactions) {
        List<String> res = new ArrayList<>();
        for (int i = 0; i < transactions.size(); i++) {
            res.add(transactions.get(i).getId());
        }
        return res;
    }

    public ResponseEntity<ResponseMessage<Object>> getTransactionHistory(
            TransactionStatus status, Integer senderAccountNumber, Integer beneficiaryAccountNumber, String startDate,
            String endDate, TransferType type) {
        try {
            List<Transaction> transactions = new ArrayList<>();
            boolean fetched = false;

            if (Helper.areNull(status, senderAccountNumber, beneficiaryAccountNumber, type, startDate, endDate)) {
                transactions = transactionRepo.findAll();
            } else {

                if (senderAccountNumber != null) {
                    if (!fetched) {
                        transactions = transactionRepo.findBySenderAccountNumber(senderAccountNumber);
                        fetched = true;
                    } else {
                        transactions = transactionRepo.findBySenderAccountNumberAndIdIn(senderAccountNumber,
                                        getTransactionsIds(transactions));
                    }
                }

                if (beneficiaryAccountNumber != null) {
                    if (!fetched) {
                        transactions = transactionRepo.findByBeneficiaryAccountNumber(beneficiaryAccountNumber);
                        fetched = true;
                    } else {
                        transactions = transactionRepo.findByBeneficiaryAccountNumberAndIdIn(beneficiaryAccountNumber,
                                        getTransactionsIds(transactions));
                    }
                }

                if (type != null) {
                    if (!fetched) {
                        transactions = transactionRepo.findByTransferType(type);
                        fetched = true;
                    } else {
                        transactions = transactionRepo.findByTransferTypeAndIdIn(type, getTransactionsIds(transactions));
                    }
                }
                if ((startDate != null) && (endDate != null)) {
                    if (!fetched) {
                        transactions = transactionRepo.findByCreatedAtBetween(
                                Helper.parseDate(startDate),
                                Helper.parseDate(endDate));
                        fetched = true;
                    } else {
                        transactions = transactionRepo.findByIdInAndCreatedAtBetween(
                                getTransactionsIds(transactions),
                                Helper.parseDate(startDate),
                                Helper.parseDate(endDate));
                    }
                }

                if (status != null) {
                    if (!fetched) {
                        transactions = transactionRepo.findByStatus(status);
                    } else {
                        transactions = transactionRepo.findByStatusAndIdIn(status, getTransactionsIds(transactions));
                    }
                }

            }

            return ServerResponse.successfulResponse("Transactions fetched", transactions);
        } catch (Exception e) {
            e.printStackTrace();
            return ServerResponse.failedResponse(e);
        }
    }

    /**
     * Though this is not required of me but it's added because of the test
     * 
     * @param customers
     * @return
     */
    public boolean addCustomers(LinkedList<Customer> customers) {
        try {
            customerRepo.saveAll(customers);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
