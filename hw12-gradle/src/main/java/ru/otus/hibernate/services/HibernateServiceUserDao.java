package ru.otus.hibernate.services;

import ru.otus.core.dao.UserDao;
import ru.otus.model.User;
import ru.otus.core.DBServiceException;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.services.ServiceUserDao;

public class HibernateServiceUserDao implements ServiceUserDao {
    private static final String ROLE_NAME_USER = "user";
    private static final String ROLE_NAME_ADMIN = "admin";
    private static final Logger logger = LoggerFactory.getLogger(HibernateServiceUserDao.class);

    private final UserDao userDao;

    public HibernateServiceUserDao(UserDao userDao) {
        this.userDao = userDao;
        addUsersAdmins();
    }

    @Override
    public Optional<User> findById(long id) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);

                logger.info("findById - user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findRandomUser() {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findRandomUser();

                logger.info("findRandomUser - user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByLogin(String login) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findByLogin(login);

                logger.info("findByLogin - user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByName(String name) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findByName(name);

                logger.info("findByName - user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAllUsers() {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                List<User> usersList = userDao.findAllUsers();

                for (User user : usersList) {
                    logger.info("findAllUsers - user: {}", user);
                }
                return usersList;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return new ArrayList<>();
        }
    }

    @Override
    public long saveUser(User user) {
        try (SessionManager sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                userDao.insertOrUpdate(user);
                long userId = user.getId();
                sessionManager.commitSession();

                logger.info("saveUser - created user: {}", userId);
                return userId;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
                throw new DBServiceException(e);
            }
        }
    }

    private void addUsersAdmins() {
        this.saveUser(new User("Крис Гир", "user1", "11111", new String[]{ROLE_NAME_USER, ROLE_NAME_ADMIN}));
        this.saveUser(new User("Ая Кэш", "user2", "11111", new String[]{ROLE_NAME_USER, ROLE_NAME_ADMIN}));
        this.saveUser(new User("Десмин Боргес", "user3", "11111", new String[]{ROLE_NAME_USER}));
        this.saveUser(new User("Кетер Донохью", "user4", "11111", new String[]{ROLE_NAME_USER}));
        this.saveUser(new User("Стивен Шнайдер", "user5", "11111", new String[]{ROLE_NAME_USER}));
        this.saveUser(new User("Джанет Вэрни", "user6", "11111", new String[]{ROLE_NAME_USER}));
        this.saveUser(new User("Брэндон Смит", "user7", "11111", new String[]{ROLE_NAME_USER}));
    }
}
