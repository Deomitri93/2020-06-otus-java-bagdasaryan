package ru.otus.repository;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.otus.domain.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryHibernateImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    public UserRepositoryHibernateImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User save(User user) {
        try (Session session = this.sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            try {
                session.persist(user);
                transaction.commit();

                return user;
            } catch (Exception e) {
                transaction.rollback();

                return null;
            }
        }
    }

    @Override
    public List<User> findAll() {

        return executeSelectQuery("from User");
    }

    @Override
    public User findById(long id) {

        return executeSelectQuery("from User U where U.id = " + id).get(0);
    }

    @Override
    public User findByName(String name) {

        return executeSelectQuery("from User U where U.name = '" + name + "'").get(0);
    }

    private List<User> executeSelectQuery(String queryString) {
        try (Session session = this.sessionFactory.openSession()) {

            return session.createQuery(queryString, User.class).list();
        } catch (Exception e) {
            e.printStackTrace();

            return new ArrayList<>();
        }
    }
}
