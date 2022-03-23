package org.dotpay.challenge.dtos;

import org.dotpay.challenge.enums.CurrencyEnum;

import lombok.Data;

@Data
public class TransactionRequest {
    private double amount;
    private String description;
    private CurrencyEnum currency = CurrencyEnum.NGN;
    private String senderBankAccountNumber;
    private String receiverBankAccountNumber;
}
