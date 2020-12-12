package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.AccountDao;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.Account;
import ru.otus.core.model.User;
import ru.otus.core.service.DbServiceAccountImpl;
import ru.otus.core.service.DbServiceUserImpl;
import ru.otus.h2.DataSourceH2;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.dao.AccountDaoJdbcMapper;
import ru.otus.jdbc.dao.UserDaoJdbcMapper;
import ru.otus.jdbc.mapper.*;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.Optional;


public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
// Общая часть
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с пользователем
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        EntityClassMetaData<User> entityClassMetaData = new EntityClassMetaDataImpl<>(User.class);
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl<>(entityClassMetaData);
        JdbcMapper<User> jdbcMapperUser = new JdbcMapperImpl<>(entityClassMetaData, entitySQLMetaData, sessionManager, dbExecutor);
        UserDao userDao = new UserDaoJdbcMapper(jdbcMapperUser);

// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao);
        var id = dbServiceUser.saveUser(new User(0, "dbServiceUser", 23));
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}, age:{}", crUser.getName(), crUser.getAge()),
                () -> logger.info("user was not created")
        );

// Работа со счетом
        DbExecutorImpl<Account> dbExecutorAccount = new DbExecutorImpl<>();
        EntityClassMetaData<Account> entityClassMetaDataAccount = new EntityClassMetaDataImpl<>(Account.class);
        EntitySQLMetaData entitySQLMetaDataAccount = new EntitySQLMetaDataImpl<>(entityClassMetaDataAccount);
        JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<>(entityClassMetaDataAccount, entitySQLMetaDataAccount, sessionManager, dbExecutorAccount);
        AccountDao accountDao = new AccountDaoJdbcMapper(jdbcMapperAccount);

        var dbServiceAccount = new DbServiceAccountImpl(accountDao);
        var idAccount = dbServiceAccount.saveAccount(new Account(0, "40702810230001234567", 100000));
        Optional<Account> account = dbServiceAccount.getAccount(idAccount);

        account.ifPresentOrElse(
                crAccount -> logger.info("created account, no:{}, rest:{}", crAccount.getNo(), crAccount.getRest()),
                () -> logger.info("account was not created")
        );
    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
