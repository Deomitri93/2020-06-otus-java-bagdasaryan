package ru.otus.hibernate;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoException;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.Optional;

public class UserDaoHibernate implements UserDao {
    private static Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);

    private final HibernateSessionManager sessionManager;

    public UserDaoHibernate(HibernateSessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public Optional<User> findById(long id) {
        try {
            return Optional.ofNullable(sessionManager.getCurrentSession().getSession().find(User.class, id));
        } catch (Exception e) {
            String method = "UserDaoHibernate findById() exception:\n";
            logger.error(method + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public long insertUser(User user) {
        try {
            var session = sessionManager.getCurrentSession().getSession();
            session.persist(user);
            session.flush();

            return user.getId();
        } catch (Exception e) {
            String method = "UserDaoHibernate insertUser() exception:\n";
            logger.error(method + e.getMessage(), e);
            throw new UserDaoException(method + e);
        }
    }

    @Override
    public void updateUser(User user) {
        try {
            sessionManager.getCurrentSession().getSession().merge(user);
        } catch (Exception e) {
            String method = "UserDaoHibernate updateUser() exception:\n";
            logger.error(method + e.getMessage(), e);
            throw new UserDaoException(method + e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
        var currentSession = sessionManager.getCurrentSession();
        try {
            Session session = sessionManager.getCurrentSession().getSession();
            if (user.getId() > 0) {
                session.merge(user);
            } else {
                session.persist(user);
                session.flush();
            }
        } catch (Exception e) {
            String method = "UserDaoHibernate insertOrUpdate() exception:\n";
            logger.error(method + e.getMessage(), e);
            throw new UserDaoException(method +  e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
