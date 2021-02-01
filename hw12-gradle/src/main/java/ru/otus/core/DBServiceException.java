package ru.otus.core;

public class DBServiceException extends RuntimeException {
    public DBServiceException(Exception e) {
        super(e);
    }
}
