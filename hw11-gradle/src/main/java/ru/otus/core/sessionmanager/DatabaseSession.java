package ru.otus.core.sessionmanager;

import org.hibernate.Session;

public interface DatabaseSession {
    Session getSession();
}
