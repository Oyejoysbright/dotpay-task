package org.dotpay.challenge.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.dotpay.challenge.enums.CurrencyEnum;
import org.dotpay.challenge.enums.TransactionStatus;
import org.dotpay.challenge.enums.TransferType;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private String id;
    private String transactionRef;
    private double amount;
    private double transactionFee;
    private double billedAmount;
    private String description;
    private TransactionStatus status;
    private CurrencyEnum currency = CurrencyEnum.NGN;
    private String senderAccountNumber;
    private String beneficiaryAccountNumber;
    private boolean commissionWorthy = false;
    private double commission;
    @Column(nullable = false)
    private TransferType transferType;
    
    @Column(updatable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	@CreationTimestamp
	private Date createdAt;

	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
	@UpdateTimestamp
	private Date updatedAt;
}
