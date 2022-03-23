package org.dotpay.challenge.services;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;

import org.dotpay.challenge.entities.Transaction;
import org.dotpay.challenge.enums.TransactionStatus;
import org.dotpay.challenge.repos.TransactionRepo;
import org.dotpay.challenge.repos.TransactionSummaryRepo;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ScheduledOperations {
    private final TransactionRepo transactionRepo;
    private final TransactionSummaryRepo transactionSummaryRepo;
    private static DecimalFormat decimalFormat = new DecimalFormat("0.00");

    double getCommission (double transactionFee) {
        String commission = String.valueOf(((20 / 100) * transactionFee));
        commission = decimalFormat.format(commission);
        return Double.parseDouble(commission);
    }

    void transactionAnalysisOperation() {
        LocalDate date = LocalDate.now().minusDays(1);
        List<Transaction> transactions = transactionRepo.findByCreatedAt(date);
        for (Transaction transaction : transactions) {
            if(transaction.getStatus().equals(TransactionStatus.SUCCESSFUL)) {
                transaction.setCommissionWorthy(true);
                transaction.setCommission(getCommission(transaction.getTransactionFee()));
            }
        }
        transactionRepo.saveAll(transactions);
    }

    @Scheduled(cron = "0 0 * * *")
    public void twelveAmJobs () {
        transactionAnalysisOperation();
    }
}
