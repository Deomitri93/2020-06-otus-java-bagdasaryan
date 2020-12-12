package ru.otus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.Annotations.Id;
import ru.otus.core.model.Account;
import ru.otus.core.model.User;
import ru.otus.jdbc.mapper.EntityClassMetaDataImpl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DisplayName("Класс EntityClassMetaDataImpl")
public class EntityClassMetaDataTest {
    private Object user;
    private EntityClassMetaDataImpl entityClassMetaDataUser;

    private Object account;
    private EntityClassMetaDataImpl entityClassMetaDataAccount;


    @BeforeEach
    void beforeEachTestInitialization() {
        user = new User(1, "User1Name", 33);
        entityClassMetaDataUser = new EntityClassMetaDataImpl(User.class);

        account = new Account(1, "Account1No", 200000);
        entityClassMetaDataAccount = new EntityClassMetaDataImpl(Account.class);

    }


    @Test
    @DisplayName("Проверяем геттер getName")
    public void EntityClassMetaDataGetNameTest() {
        String nameExpected = User.class.getSimpleName();

        assertEquals(nameExpected, entityClassMetaDataUser.getName());
        assertNotEquals(nameExpected, entityClassMetaDataAccount.getName());
    }


    @Test
    @DisplayName("Проверяем геттер getConstructor")
    public void EntityClassMetaDataGetConstructorTest() throws NoSuchMethodException {
        Constructor constructorExpected = User.class.getConstructor();

        assertEquals(constructorExpected, entityClassMetaDataUser.getConstructor());
        assertNotEquals(constructorExpected, entityClassMetaDataAccount.getConstructor());
    }

    @Test
    @DisplayName("Проверяем геттер getIdField")
    public void EntityClassMetaDataGetIdFieldTest() {
        Field idFieldExpected = null;
        for (Field field : User.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                idFieldExpected = field;
            }
        }

        assertEquals(idFieldExpected, entityClassMetaDataUser.getIdField());
        assertNotEquals(idFieldExpected, entityClassMetaDataAccount.getIdField());
    }

    @Test
    @DisplayName("Проверяем геттер getAllFields")
    public void EntityClassMetaDataGetAllFieldsTest() {
        List<Field> allFieldsExpected = Arrays.asList(User.class.getDeclaredFields());

        assertEquals(allFieldsExpected, entityClassMetaDataUser.getAllFields());
        assertNotEquals(allFieldsExpected, entityClassMetaDataAccount.getAllFields());
    }

    @Test
    @DisplayName("Проверяем геттер getFieldsWithoutId")
    public void EntityClassMetaDataGetFieldsWithoutIdTest() {
        List<Field> fieldsWithoutIdExpected = new ArrayList<>();
        for (Field field : User.class.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Id.class)) {
                fieldsWithoutIdExpected.add(field);
            }
        }

        assertEquals(fieldsWithoutIdExpected, entityClassMetaDataUser.getFieldsWithoutId());
        assertNotEquals(fieldsWithoutIdExpected, entityClassMetaDataAccount.getFieldsWithoutId());
    }
}
