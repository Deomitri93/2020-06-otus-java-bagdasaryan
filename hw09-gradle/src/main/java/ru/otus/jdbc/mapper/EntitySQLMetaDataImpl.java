package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EntitySQLMetaDataImpl<T> implements EntitySQLMetaData {
    private final String selectAllSql;
    private final String selectByIdSql;
    private final String insertSql;
    private final String updateSql;

    public EntitySQLMetaDataImpl(EntityClassMetaData<T> entityClassMetaData) {
        String idField = entityClassMetaData.getIdField().getName();
        String allFieldsCommaSeparated = entityClassMetaData.getAllFields().stream().map(Field::getName).collect(Collectors.joining(","));
        String fieldsWithoutIdCommaSeparated = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).collect(Collectors.joining(","));
        String fieldsWithoutIdQuestionMarkedCommaSeparated = entityClassMetaData.getFieldsWithoutId().stream().map(Field::getName).map(a -> a + " = ?").collect(Collectors.joining(","));
        String tableName = entityClassMetaData.getName();

        selectAllSql = "SELECT " + allFieldsCommaSeparated +
                " FROM " + tableName + ";";

        selectByIdSql = "SELECT " + allFieldsCommaSeparated +
                " FROM " + tableName +
                " WHERE " + idField + " = ?;";

        String[] s = new String[entityClassMetaData.getFieldsWithoutId().size()];
        Arrays.fill(s, "?");
        insertSql = "INSERT INTO " + tableName + " (" + fieldsWithoutIdCommaSeparated + ")" +
                " VALUES (" + String.join(",", s) + ");";

        updateSql = "UPDATE " + tableName + " SET " + fieldsWithoutIdQuestionMarkedCommaSeparated +
                " WHERE " + idField + " = ?;";
    }

    @Override
    public String getSelectAllSql() {
        return selectAllSql;
    }

    @Override
    public String getSelectByIdSql() {
        return selectByIdSql;
    }

    @Override
    public String getInsertSql() {
        return insertSql;
    }

    @Override
    public String getUpdateSql() {
        return updateSql;
    }
}
