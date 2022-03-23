package org.dotpay.challenge.enums;

public enum TransactionStatus {
    SUCCESSFUL("SUCCESSFUL"),
    INSUFFICIENT_FUND("INSUFFICIENT FUND"),
    FAILED("FAILED");

    public final String value;
    private TransactionStatus(String value) {
        this.value = value;
    }
}
