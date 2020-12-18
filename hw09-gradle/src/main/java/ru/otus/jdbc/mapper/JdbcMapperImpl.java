package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {
    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    private final EntityClassMetaData<T> entityClassMetaData;
    private final EntitySQLMetaData entitySQLMetaData;
    private final SessionManager sessionManager;
    private final DbExecutor<T> dbExecutor;

    private void setFieldValue(Field field, T instance, Object value) throws IllegalAccessException {
        field.setAccessible(true);
        field.set(instance, value);
    }

    public JdbcMapperImpl(EntityClassMetaData<T> entityClassMetaData, EntitySQLMetaData entitySQLMetaData, SessionManager sessionManager, DbExecutor<T> dbExecutor) {
        this.entityClassMetaData = entityClassMetaData;
        this.entitySQLMetaData = entitySQLMetaData;
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
    }

    @Override
    public long insert(T objectData) {
        try {
            Connection connection = sessionManager.getCurrentSession().getConnection();
            String sqlString = entitySQLMetaData.getInsertSql();
            List<Object> params = getFieldsWithoutID(objectData);

            return dbExecutor.executeInsert(connection, sqlString, params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Could not insert data into DB!");
        }
    }

    @Override
    public void update(T objectData) {
        try {
            Connection connection = sessionManager.getCurrentSession().getConnection();
            String sqlString = entitySQLMetaData.getUpdateSql();
            List<Object> params = getFieldsWithoutID(objectData);

            dbExecutor.executeInsert(connection, sqlString, params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Could not update data in DB!");
        }
    }

    @Override
    public void insertOrUpdate(T objectData) {
        try {
            var idField = entityClassMetaData.getIdField();
            idField.setAccessible(true);

            var findResult = findById(idField.get(objectData));
            if (findResult.isEmpty()) {
                insert(objectData);
            } else {
                update(objectData);
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<T> findById(Object id) {
        try {
            Connection connection = sessionManager.getCurrentSession().getConnection();

            String sqlString = entitySQLMetaData.getSelectByIdSql();
            Function<ResultSet, T> instanceFromResultSet = resultSet -> {
                try {
                    if (resultSet.next()) {
                        var instance = entityClassMetaData.getConstructor().newInstance();

                        var idField = entityClassMetaData.getIdField();
                        setFieldValue(idField, instance, resultSet.getObject(idField.getName()));

                        for (var field : entityClassMetaData.getFieldsWithoutId()) {
                            setFieldValue(field, instance, resultSet.getObject(field.getName()));
                        }

                        return instance;
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }

                return null;
            };

            return dbExecutor.executeSelect(connection, sqlString, id, instanceFromResultSet);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private List<Object> getFieldsWithoutID(T objectData) throws IllegalAccessException {
        List<Object> params = new ArrayList<>();
        for (var field : entityClassMetaData.getFieldsWithoutId()) {
            field.setAccessible(true);
            params.add(field.get(objectData));
        }

        return params;
    }
}
