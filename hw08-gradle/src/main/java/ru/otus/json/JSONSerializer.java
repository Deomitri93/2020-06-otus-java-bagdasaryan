package ru.otus.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

public class JSONSerializer {
    private String stringToJson(Object src) {
        return "\"" + src + "\"";
    }

    private String primitiveToJson(Object src) {
        return src.toString();
    }

    private String collectionToJson(Object src) {
        return arrayToJson(((Collection<Object>) src).toArray());
    }

    private String arrayToJson(Object src) {
        StringBuilder res = new StringBuilder();

        res.append("[");
        for (int i = 0; i < Array.getLength(src); i++) {
            Object o = Array.get(src, i);
            res.append(toJson(o));
            if (i < Array.getLength(src) - 1) {
                res.append(",");
            }
        }
        res.append("]");

        return res.toString();
    }

    private String objectToJson(Object src) {
        StringBuilder res = new StringBuilder();
        res.append("{");

        int cnt = 0;
        for (Field field : src.getClass().getDeclaredFields()) {
            try {
                int modifiers = field.getModifiers();
                if (Modifier.isPrivate(modifiers)) {
                    field.setAccessible(true);
                }

                if (field.get(src) != null) {
                    res.append("\"").append(field.getName()).append("\":");

                    res.append(toJson(field.get(src)));

                    if (cnt < src.getClass().getDeclaredFields().length - 1) {
                        res.append(",");
                    }
                }

                if (Modifier.isPrivate(modifiers)) {
                    field.setAccessible(false);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            cnt++;
        }

        res.append("}");
        return res.toString();
    }

    public String toJson(Object src) {
        if (src == null) {
            return "null";
        }

        Class<?> clazz = src.getClass();
        StringBuilder res = new StringBuilder();


        if (String.class.isAssignableFrom(clazz)) {
            res.append(stringToJson(src));
        } else if ((clazz.isPrimitive()) || Boolean.class.isAssignableFrom(clazz) || Number.class.isAssignableFrom(clazz)) {
            res.append(primitiveToJson(src));
        } else if (Collection.class.isAssignableFrom(clazz)) {
            res.append(collectionToJson(src));
        } else if (clazz.isArray()) {
            res.append(arrayToJson(src));
        } else {
            res.append(objectToJson(src));
        }

        return res.toString();
    }
}
