package org.dotpay.challenge.services;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import org.dotpay.challenge.entities.Transaction;
import org.dotpay.challenge.entities.TransactionSummary;
import org.dotpay.challenge.enums.TransactionStatus;
import org.dotpay.challenge.repos.TransactionRepo;
import org.dotpay.challenge.repos.TransactionSummaryRepo;
import org.dotpay.challenge.utils.Helper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduledOperations {
    private final TransactionRepo transactionRepo;
    private final TransactionSummaryRepo transactionSummaryRepo;
    

    double getCommission (double transactionFee) {
        String commission = String.valueOf(((20 / 100) * transactionFee));
        commission = Helper.decimalFormat.format(commission);
        return Double.parseDouble(commission);
    }

    void transactionAnalysisOperation() throws ParseException {
        LocalDate date = LocalDate.now().minusDays(1);
        List<Transaction> transactions = transactionRepo.findByCreatedAt(Helper.dateFormatter.parse(date.toString()));
        for (Transaction transaction : transactions) {
            if(transaction.getStatus().equals(TransactionStatus.SUCCESSFUL)) {
                transaction.setCommissionWorthy(true);
                transaction.setCommission(getCommission(transaction.getTransactionFee()));
            }
        }
        transactionRepo.saveAll(transactions);
    }

    void summarizeTransactions() throws ParseException {
        LocalDate date = LocalDate.now().minusDays(1);
        List<Transaction> transactions = transactionRepo.findByCreatedAt(Helper.dateFormatter.parse(date.toString()));
        int totalSuccessful = 0;
        int totalFailed = 0;
        double amountTransacted = 0.0;
        double amountCommissioned = 0.0;
        for (Transaction transaction : transactions) {
            if(transaction.getStatus().equals(TransactionStatus.SUCCESSFUL)) {
                totalSuccessful++;
                amountTransacted += transaction.getAmount();
                amountCommissioned += transaction.getCommission();
            } else {
                totalFailed++;
            }
        }
        int totalTransactions = totalSuccessful + totalFailed;
        TransactionSummary summary = new TransactionSummary(totalTransactions, totalSuccessful, totalFailed, amountTransacted, amountCommissioned);
        transactionSummaryRepo.save(summary);
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void twelveAmJobs () {
        try {
            transactionAnalysisOperation();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 3 * * *")
    public void threeAmJobs () {
        try {
            summarizeTransactions();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
