package com.coldlight.payment_provider.model;

public enum WebhookEventType {
    PAYMENT_SUCCESSFUL("payment_successful"),
    PAYMENT_FAILED("payment_failed"),
    BALANCE_UPDATED("balance_updated");

    private final String value;

    WebhookEventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
