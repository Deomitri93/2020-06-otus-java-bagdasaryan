package ru.otus.core.sessionmanager;

import java.sql.Connection;

public class DatabaseSessionImpl implements DatabaseSession {
    private final Connection connection;

    public DatabaseSessionImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection getConnection() {
        return connection;
    }
}
