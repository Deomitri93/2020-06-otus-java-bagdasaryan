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

    private Constructor<T> getDefaultConstructor(Class<T> clazz) {
        try {
            return clazz.getConstructor();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Entity class must have Constructor!");
        }
    }

    private List<Field> getUnannotatedFields(Class<T> clazz) {
        List<Field> unannotatedFieldsList = new ArrayList<>();
        for (Field field : allFields) {
            if (!field.isAnnotationPresent(Id.class)) {
                unannotatedFieldsList.add(field);
            }
        }

        return unannotatedFieldsList;
    }

    private Field getIdAnnotatedField(Class<T> clazz) {
        List<Field> idAnnotatedFields = new ArrayList<>();
        for (Field field : allFields) {
            if (field.isAnnotationPresent(Id.class)) {
                idAnnotatedFields.add(field);
            }
        }

        if (idAnnotatedFields.size() != 1) {
            throw new RuntimeException("Entity class must have one and only one @Id annotated field!");
        }

        return idAnnotatedFields.get(0);
    }

    public EntityClassMetaDataImpl(Class<T> clazz) {
        name = clazz.getSimpleName();

        constructor = getDefaultConstructor(clazz);

        allFields = Arrays.asList(clazz.getDeclaredFields());

        idField = getIdAnnotatedField(clazz);

        fieldsWithoutId = getUnannotatedFields(clazz);
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
