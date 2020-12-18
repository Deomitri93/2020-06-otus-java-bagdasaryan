package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.model.User;
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.EntitySQLMetaDataImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Класс EntitySQLMetaDataImpl")
public class EntitySQLMetaDataTest {
    private Object user;
    private EntityClassMetaDataImpl entityClassMetaDataUser;
    private EntitySQLMetaDataImpl entitySQLMetaDataUser;


    @BeforeEach
    void beforeEachTestInitialization() {
        entityClassMetaDataUser = new EntityClassMetaDataImpl(User.class);
        entitySQLMetaDataUser = new EntitySQLMetaDataImpl(entityClassMetaDataUser);
    }

    @Test
    @DisplayName("Проверяем геттер getSelectAllSql")
    public void EntitySQLMetaDataGetSelectAllSqlTest() {
        String sqlExpected = "select id,name,age" +
                " from user;";

        assertEquals(sqlExpected, entitySQLMetaDataUser.getSelectAllSql().toLowerCase());
    }

    @Test
    @DisplayName("Проверяем геттер getSelectByIdSql")
    public void EntityClassMetaDataGetSelectByIdSqlTest() {
        String sqlExpected = "select id,name,age" +
                " from user" +
                " where id = ?;";

        assertEquals(sqlExpected, entitySQLMetaDataUser.getSelectByIdSql().toLowerCase());
    }

    @Test
    @DisplayName("Проверяем геттер getInsertSql")
    public void EntityClassMetaDataGetInsertSqlTest() {
        String sqlExpected = "insert into user (name,age)" +
                " values (?,?);";

        assertEquals(sqlExpected, entitySQLMetaDataUser.getInsertSql().toLowerCase());
    }

    @Test
    @DisplayName("Проверяем геттер getUpdateSql")
    public void EntityClassMetaDataGetUpdateSqlTest() {
        String sqlExpected = "update user" +
                " set name = ?,age = ?" +
                " where id = ?;";

        assertEquals(sqlExpected, entitySQLMetaDataUser.getUpdateSql().toLowerCase());
    }
}
