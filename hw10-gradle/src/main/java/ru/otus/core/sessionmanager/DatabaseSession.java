package ru.otus.core.sessionmanager;

import org.hibernate.Session;

import java.sql.Connection;

public interface DatabaseSession {
    public Session getSession();
}
