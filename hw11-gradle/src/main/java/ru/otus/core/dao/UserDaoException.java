package ru.otus.core.dao;

public class UserDaoException extends RuntimeException {
    public UserDaoException(Exception ex) {
        super(ex);
    }

    public UserDaoException(String msg) {
        super(msg);
    }
}
