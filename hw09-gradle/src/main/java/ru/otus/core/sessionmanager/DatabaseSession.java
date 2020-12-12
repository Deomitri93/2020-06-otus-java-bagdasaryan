package ru.otus.core.sessionmanager;

import java.sql.Connection;

public interface DatabaseSession {
    public Connection getConnection();
}
