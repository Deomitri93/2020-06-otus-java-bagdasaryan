package ru.otus.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ru.otus.core.sessionmanager.DatabaseSession;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.core.sessionmanager.SessionManagerException;

public class HibernateSessionManager implements SessionManager {
    private final SessionFactory sessionFactory;

    private HibernateDatabaseSession hibernateDatabaseSession;

    public HibernateSessionManager(SessionFactory sessionFactory) {
        if (sessionFactory == null) {
            throw new SessionManagerException("HibernateSessionManager constructor exception: parameter SessionFactory is null");
        }
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void beginSession() {
        try {
            hibernateDatabaseSession = new HibernateDatabaseSession(sessionFactory.openSession());
        } catch (Exception e) {
            throw new SessionManagerException("HibernateSessionManager beginSession() exception:\n" + e);
        }
    }

    @Override
    public void commitSession() {
        String method = "HibernateSessionManager commitSession() exception";
        checkSessionAndTransaction(method);
        try {
            hibernateDatabaseSession.getTransaction().commit();
            hibernateDatabaseSession.getSession().close();
        } catch (Exception e) {
            throw new SessionManagerException(method + ":\n" + e);
        }
    }

    @Override
    public void rollbackSession() {
        String method = "HibernateSessionManager rollbackSession() exception";
        checkSessionAndTransaction(method);
        try {
            hibernateDatabaseSession.getTransaction().rollback();
            hibernateDatabaseSession.getSession().close();
        } catch (Exception e) {
            throw new SessionManagerException(e);
        }
    }

    @Override
    public void close() {
        if (hibernateDatabaseSession == null) {
            return;
        }
        Session session = hibernateDatabaseSession.getSession();
        if (session == null || !session.isConnected()) {
            return;
        }

        Transaction transaction = hibernateDatabaseSession.getTransaction();
        if (transaction == null || !transaction.isActive()) {
            return;
        }

        try {
            hibernateDatabaseSession.close();
            hibernateDatabaseSession = null;
        } catch (Exception e) {
            throw new SessionManagerException("HibernateSessionManager rollbackSession() exception:\n" + e);
        }
    }

    @Override
    public DatabaseSession getCurrentSession() {
        String method = "HibernateSessionManager getCurrentSession() exception";
        checkSessionAndTransaction(method);
        return hibernateDatabaseSession;
    }

    private void checkSessionAndTransaction(String method) {
        method += ":\n";
        if (hibernateDatabaseSession == null) {
            throw new SessionManagerException(method + "HibernateDatabaseSession is null");
        }

        Session session = hibernateDatabaseSession.getSession();
        if ((session == null) || !(session.isConnected())) {
            throw new SessionManagerException(method + "Session is null or disconnected");
        }

        Transaction transaction = hibernateDatabaseSession.getTransaction();
        if ((transaction == null) || !(transaction.isActive())) {
            throw new SessionManagerException(method + "Transaction is null or closed");
        }
    }
}
