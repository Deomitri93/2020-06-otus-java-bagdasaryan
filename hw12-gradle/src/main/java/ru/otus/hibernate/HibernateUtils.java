package ru.otus.hibernate;

import ru.otus.core.dao.UserDao;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.model.Role;
import ru.otus.model.User;
import ru.otus.hibernate.services.HibernateServiceUserDao;
import ru.otus.services.ServiceUserDao;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import java.util.Arrays;

public final class HibernateUtils {

    private HibernateUtils() {
    }

    public static SessionFactory buildSessionFactory(String configResourceFileName,
                                                     Class<?>... annotatedClasses) {
        Configuration configuration = new Configuration().configure(configResourceFileName);
        MetadataSources metadataSources = new MetadataSources(createServiceRegistry(configuration));
        Arrays.stream(annotatedClasses).forEach(metadataSources::addAnnotatedClass);

        Metadata metadata = metadataSources.getMetadataBuilder().build();
        return metadata.getSessionFactoryBuilder().build();
    }

    public static ServiceUserDao createServiceUserDao(String hibernateConfigurationFile) {
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(hibernateConfigurationFile, User.class, Role.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        return new HibernateServiceUserDao(userDao);
    }

    private static StandardServiceRegistry createServiceRegistry(Configuration configuration) {
        return new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();
    }
}
