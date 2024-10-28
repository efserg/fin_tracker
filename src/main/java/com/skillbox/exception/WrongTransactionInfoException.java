package com.skillbox.exception;

public class WrongTransactionInfoException extends RuntimeException {

    public WrongTransactionInfoException(int transactionId) {
        super("Wrong transaction info for transaction " + transactionId);
    }
}
