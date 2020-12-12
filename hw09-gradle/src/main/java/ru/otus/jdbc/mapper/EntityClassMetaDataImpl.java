package ru.otus.jdbc.mapper;

import ru.otus.Annotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {
    private final String name;
    private final Constructor<T> constructor;
    private final Field idField;
    private final List<Field> allFields;
    private final List<Field> fieldsWithoutId;

    public EntityClassMetaDataImpl(Class<T> clazz) {
        name = clazz.getSimpleName();

        Constructor<T> tmpConstructor;
        try {
            tmpConstructor = clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Entity class must have Constructor!");
        }
        constructor = tmpConstructor;

        allFields = Arrays.asList(clazz.getDeclaredFields());

        List<Field> tmpIdFieldsList = new ArrayList<>();
        fieldsWithoutId = new ArrayList<>();
        int idAnnotatedCnt = 0;
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Id.class)) {
                idAnnotatedCnt++;
                tmpIdFieldsList.add(field);
            } else {
                fieldsWithoutId.add(field);
            }
        }
        if (idAnnotatedCnt != 1) {
            throw new RuntimeException("Entity class must have one and only one @Id annotated field!");
        }
        idField = tmpIdFieldsList.get(0);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Constructor<T> getConstructor() {
        return constructor;
    }

    @Override
    public Field getIdField() {
        return idField;
    }

    @Override
    public List<Field> getAllFields() {
        return allFields;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return fieldsWithoutId;
    }
}
