package ru.otus.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.domain.User;

import java.util.List;

@Repository
public class UserRepositoryHibernateImpl implements UserRepository {
    private SessionFactory sessionFactory;

    @Autowired
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> findAll() {
        Session session = this.sessionFactory.openSession();
        List<User> userList = session.createQuery("from User").list();
        session.close();

        return userList;
    }

    @Override
    public User save(User user) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(user);
        transaction.commit();
        session.close();

        return user;
    }

    @Override
    public User findById(long id) {
        Session session = this.sessionFactory.openSession();
        List<User> userList = session.createQuery("from User U where U.id = " + id).list();
        session.close();

        return userList.get(0);
    }

    @Override
    public User findByName(String name) {
        Session session = this.sessionFactory.openSession();
        List<User> userList = session.createQuery("from User U where U.name = \'" + name + "\'").list();
        session.close();

        return userList.get(0);
    }
}
