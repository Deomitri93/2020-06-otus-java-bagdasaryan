package ru.otus.core.sessionmanager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class SessionManagerImpl implements SessionManager {
    private Connection connection;
    private final DataSource dataSource;
    private DatabaseSession databaseSession;

    public SessionManagerImpl(DataSource dataSource) {
        if (dataSource == null) {
            throw new SessionManagerException("DataSource is null!");
        }
        this.dataSource = dataSource;
    }

    @Override
    public void beginSession() {
        try {
            connection = dataSource.getConnection();
            databaseSession = new DatabaseSessionImpl(connection);
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void commitSession() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void rollbackSession() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public DatabaseSession getCurrentSession() {
        return databaseSession;
    }
}
