package org.dotpay.challenge.services;

import org.dotpay.challenge.entities.TransactionSummary;
import org.dotpay.challenge.repos.TransactionSummaryRepo;
import org.dotpay.challenge.utils.Helper;
import org.dotpay.challenge.utils.ServerResponse;
import org.dotpay.challenge.utils.ServerResponse.ResponseMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TransactionSummaryService {
    private final TransactionSummaryRepo transactionSummaryRepo;

    public ResponseEntity<ResponseMessage<Object>> getTransactionSummary(String date) {
        try {
            TransactionSummary summary = transactionSummaryRepo.findByCreatedAt(Helper.parseDate(date));
            return ServerResponse.successfulResponse("Transaction summary for specified date fetched", summary);
        } catch (Exception e) {
            return ServerResponse.failedResponse(e);
        }
    }
}
