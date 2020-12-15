package ru.otus;


import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateSessionManager;
import ru.otus.hibernate.UserDaoHibernate;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class HomeWork {
    private static final String HIBERNATE_CONFIG = "hibernate.cfg.xml";
    private static SessionFactory sessionFactory;

    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Настройка Hibernate
        sessionFactory = hibernateConfig(HIBERNATE_CONFIG);
        HibernateSessionManager sessionManager = new HibernateSessionManager(sessionFactory);

// Работа с пользователем
        var userDao = new UserDaoHibernate(sessionManager);

// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao);

        User user = new User(null, "dbServiceUser", 23);
        AddressDataSet address = new AddressDataSet(user, "Krasnaya st.");
        List<PhoneDataSet> phones = Arrays.asList(
                new PhoneDataSet(user, "89180000000"),
                new PhoneDataSet(user, "89281111111"),
                new PhoneDataSet(user, "89092222222")
        );
        user.setAddress(address);
        user.setPhones(phones);

        logger.info("\nuser created: {}\n", user.toString());

        var id = dbServiceUser.saveUser(user);
        Optional<User> userFromDB = dbServiceUser.getUser(id);

        userFromDB.ifPresentOrElse((usr) -> logger.info("\nuser loaded from DB: {}\n", usr.toString()),
                () -> logger.info("\nloaded user is null\n"));
    }

    private static SessionFactory hibernateConfig(String hibernateConfigFile) {
        var configuration = new Configuration().configure(hibernateConfigFile);

        var serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        var metadata = new MetadataSources(serviceRegistry)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(AddressDataSet.class)
                .addAnnotatedClass(PhoneDataSet.class)
                .getMetadataBuilder()
                .build();

        return metadata.getSessionFactoryBuilder().build();
    }
}