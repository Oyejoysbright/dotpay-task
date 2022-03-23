package org.dotpay.challenge.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "transaction_summaries")
@Entity
@Setter
@Getter
@NoArgsConstructor
public class TransactionSummary {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private int totalTransactions;
    private int totalSuccessful;
    private int totalFailed;
    private double amountTransacted;
    private double amountCommissioned;
    @Column(updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	@CreationTimestamp
	private Date createdAt;
    
    public TransactionSummary(int totalTransactions, int totalSuccessful, int totalFailed, double amountTransacted,
            double amountCommissioned) {
        this.totalTransactions = totalTransactions;
        this.totalSuccessful = totalSuccessful;
        this.totalFailed = totalFailed;
        this.amountTransacted = amountTransacted;
        this.amountCommissioned = amountCommissioned;
    }

}
