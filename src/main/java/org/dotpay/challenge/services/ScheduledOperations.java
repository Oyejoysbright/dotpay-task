package org.dotpay.challenge.services;

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

    static double getCommission(double transactionFee) {
        double commission = (20 * transactionFee) / 100;
        String formatted = Helper.decimalFormat.format(commission);
        Helper.logInfo(transactionFee + ">>>>>> " + commission);
        return Double.parseDouble(formatted);
    }

    public boolean transactionAnalysisOperation() {
        try {
            List<Transaction> transactions = transactionRepo.findByCreatedAtBetween(Helper.getYesterdayDate(), Helper.parseDate(null));
            for (Transaction transaction : transactions) {
                if (transaction.getStatus().equals(TransactionStatus.SUCCESSFUL)) {
                    transaction.setCommissionWorthy(true);
                    transaction.setCommission(getCommission(transaction.getTransactionFee()));
                    Helper.logInfo("Commission \t" + transaction.getCommission());
                }
            }
            transactionRepo.saveAll(transactions);
            Helper.logInfo("Transactions successfully analyzed.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Helper.logInfo("Transactions failed to analyze.");
            return false;
        }
    }

    public boolean summarizeTransactions() {
        try {
            List<Transaction> transactions = transactionRepo.findByCreatedAtBetween(Helper.getYesterdayDate(), Helper.getTodayDate());
            int totalSuccessful = 0;
            int totalFailed = 0;
            double amountTransacted = 0.0;
            double amountCommissioned = 0.0;
            for (Transaction transaction : transactions) {
                if (transaction.getStatus().equals(TransactionStatus.SUCCESSFUL)) {
                    totalSuccessful++;
                    amountTransacted += transaction.getAmount();
                    amountCommissioned += transaction.getCommission();
                } else {
                    totalFailed++;
                }
            }
            int totalTransactions = totalSuccessful + totalFailed;
            TransactionSummary summary = new TransactionSummary(totalTransactions, totalSuccessful, totalFailed,
                    amountTransacted, amountCommissioned);
            transactionSummaryRepo.save(summary);
            Helper.logInfo("Transaction summary successfully generated.");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Helper.logInfo("Transaction Summary failed to generate.");
            return false;
        }
    }

    @Scheduled(cron = "00 06 17 * * *")
    public void twelveAmJobs() {
        transactionAnalysisOperation();
    }

    @Scheduled(cron = "0 59 16 * * *")
    public void threeAmJobs() {
        summarizeTransactions();
    }
}
