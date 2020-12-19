package ru.otus.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.HwCache;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;

import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);

    private final UserDao userDao;
    private final HwCache<Long, User> hwCache;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
        this.hwCache = null;
    }

    public DbServiceUserImpl(UserDao userDao, HwCache<Long, User> hwCache) {
        this.userDao = userDao;
        this.hwCache = hwCache;
    }

    @Override
    public long saveUser(User user) {
        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                var userId = userDao.insertUser(user);
                sessionManager.commitSession();

                putUser(user);

                logger.debug("created user: {}", userId);
                return userId;
            } catch (Exception e) {
                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        if (hwCache != null) {
            var cachedValue = hwCache.get(id);
            if (cachedValue != null) {
                return Optional.of(cachedValue);
            }
        }

        try (var sessionManager = userDao.getSessionManager()) {
            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);

                userOptional.ifPresent(this::putUser);

                logger.debug("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }

    private void putUser(User user) {
        try {
            if (hwCache != null) {
                hwCache.put(user.getId(), user);
            }
        } catch (NullPointerException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
