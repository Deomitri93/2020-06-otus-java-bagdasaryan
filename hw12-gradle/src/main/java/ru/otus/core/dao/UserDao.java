package ru.otus.core.dao;

import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);
    Optional<User> findRandomUser();
    Optional<User> findByLogin(String login);
    Optional<User> findByName(String name);
    List<User> findAllUsers();
    long insertUser(User user);
    void updateUser(User user);
    void insertOrUpdate(User user);
    SessionManager getSessionManager();
}