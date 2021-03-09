package ru.otus.repostory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.otus.domain.User;

import java.util.List;

@Repository
public class HibernateUserRepository implements UserRepository {
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
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.persist(user);
        transaction.commit();
        session.close();

        return user;
    }

    @Override
    public User findById(long id) {
        Session session = this.sessionFactory.openSession();
        List<User> userList = session.createQuery("FROM User U WHERE U.id = " + id).list();
        session.close();

        return userList.get(0);
    }

    @Override
    public User findByName(String name) {
        Session session = this.sessionFactory.openSession();
        List<User> userList = session.createQuery("FROM User U WHERE U.name = " + name).list();
        session.close();

        return userList.get(0);
    }
}
