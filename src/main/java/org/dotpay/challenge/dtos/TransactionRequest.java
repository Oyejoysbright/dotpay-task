package org.dotpay.challenge.dtos;

import org.dotpay.challenge.enums.CurrencyEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequest {
    private double amount;
    private String description;
    private CurrencyEnum currency = CurrencyEnum.NGN;
    private Integer senderAccountNumber;
    private Integer beneficiaryAccountNumber;
}
