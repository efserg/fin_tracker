package com.skillbox.exception;

public class TransactionReadException extends RuntimeException {

    public TransactionReadException(Exception exception) {
        super("Ошибка чтения файла транзакций", exception);
    }
}
