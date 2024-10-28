package com.skillbox.exception;

public class TaxRateNotFoundException extends RuntimeException {

    public TaxRateNotFoundException(int transactionId) {
        super("Tax rate not found for transaction " + transactionId);
    }
}
