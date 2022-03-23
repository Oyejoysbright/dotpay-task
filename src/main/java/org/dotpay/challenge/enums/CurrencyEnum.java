package org.dotpay.challenge.enums;

public enum CurrencyEnum {
    NGN("NGN"), USD("USD"), EUR("EUR");
    public final String text;

    private CurrencyEnum(String val) {
        this.text = val;
    }
}
