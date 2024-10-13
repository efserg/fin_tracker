package com.skillbox.exception;

public class AnalyticWriteException extends RuntimeException {

    public AnalyticWriteException(Exception exception) {
        super("Ошибка записи аналитики в файл", exception);
    }
}
