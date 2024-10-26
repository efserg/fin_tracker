package com.skillbox.exception;

public class AccountReadException extends RuntimeException {

    public AccountReadException(Exception exception) {
        super("Ошибка чтения файла счетов", exception);
    }
}
