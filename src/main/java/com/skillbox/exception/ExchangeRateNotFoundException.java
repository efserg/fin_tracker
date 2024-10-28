package com.skillbox.exception;

public class ExchangeRateNotFoundException extends RuntimeException {

    public ExchangeRateNotFoundException(int transactionId) {
        super("Exchange rate not found for transaction " + transactionId);
    }
}
