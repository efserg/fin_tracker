package com.skillbox.exception;

public class UnknownTransactionTypeException extends RuntimeException {

    public UnknownTransactionTypeException(String type) {
        super("Неизвестный тип транзакции: " + type);
    }
}
