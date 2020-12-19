package ru.otus;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cache.MyCache;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.core.service.DBServiceUser;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.hibernate.HibernateSessionManager;
import ru.otus.hibernate.UserDaoHibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class HomeworkCache {
    private static final Logger logger = LoggerFactory.getLogger(HomeworkCache.class);

    private static final String URL = "jdbc:h2:mem:testDB;DB_CLOSE_DELAY=-1";
    private static final int QUANTITY = 30;
    private static final int ITERATIONS_NUMBER = 3;

    public static void main(String[] args) {
        var sessionFactory = hibernateConfig(URL);
        var sessionManager = new HibernateSessionManager(sessionFactory);

        var userDao = new UserDaoHibernate(sessionManager);
        var cache = new MyCache<Long, User>();

        var dbServiceUser = new DbServiceUserImpl(userDao);
        var dbServiceUserCached = new DbServiceUserImpl(userDao, cache);

        List<Long> usersIdList = new ArrayList<>();
        for (int i = 0; i < QUANTITY; i++) {
            usersIdList.add(generateUser(dbServiceUser));
        }

        List<Long> cachedUsersIdList = new ArrayList<>();
        for (int i = QUANTITY; i < 2 * QUANTITY; i++) {
            cachedUsersIdList.add(generateUser(dbServiceUserCached));
        }

        long timeElapsed;
        for (int i = 0; i < ITERATIONS_NUMBER; i++) {
            timeElapsed = getUsersByIdList(dbServiceUser, usersIdList);
            logger.info("\nNo cache used:\nUsers quantity {}\nIteration {}\nTimeElapsed {} ns.\n", QUANTITY, i + 1, timeElapsed);

            timeElapsed = getUsersByIdList(dbServiceUserCached, cachedUsersIdList);
            logger.info("\nCached data:\nUsers quantity {}\nIteration {}\nTimeElapsed {} ns.\n", QUANTITY, i + 1, timeElapsed);
        }
    }

    private static long generateUser(DBServiceUser dbServiceUser) {
        User user = new User(null, "user", (int) (Math.random() * 100));
        AddressDataSet address = new AddressDataSet(user, "Krasnaya st., " + (int) (Math.random() * 400));
        List<PhoneDataSet> phones = Arrays.asList(
                new PhoneDataSet(user, "89180000000"),
                new PhoneDataSet(user, "89281111111"),
                new PhoneDataSet(user, "89092222222")
        );
        user.setAddress(address);
        user.setPhones(phones);

        return dbServiceUser.saveUser(user);
    }

    private static long getUsersByIdList(DBServiceUser dbServiceUser, List<Long> idList) {
        long startTime = System.nanoTime();
        for (var id : idList) {
            Optional<User> userFromDB = dbServiceUser.getUser(id);
        }
        long stopTime = System.nanoTime();

        return stopTime - startTime;
    }

    private static SessionFactory hibernateConfig(String URL) {
        var configuration = new Configuration()
                .setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")
                .setProperty("hibernate.connection.driver_class", "org.h2.Driver")
                .setProperty("hibernate.connection.url", URL)
                .setProperty("hibernate.show_sql", "false")
                .setProperty("hibernate.hbm2ddl.auto", "create")
                .setProperty("hibernate.generate_statistics", "false");

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
