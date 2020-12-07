package ru.otus.json;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collection;

public class JSONSerializer {
    StringBuilder res = new StringBuilder();

    public String toJson(Object src) {
        if (src == null) {
            return "null";
        }

        if (String.class.isAssignableFrom(src.getClass())) {
            res.append("\"").append(src).append("\"");
        } else if ((src.getClass().isPrimitive()) || Boolean.class.isAssignableFrom(src.getClass()) || Number.class.isAssignableFrom(src.getClass())) {
            res.append(src);
        } else if (Collection.class.isAssignableFrom(src.getClass())) {
            res.append("[");

            Object[] arr = ((Collection<Object>) src).toArray();
            for (int i = 0; i < arr.length; i++) {
                toJson(arr[i]);
                if (i < arr.length - 1) {
                    res.append(",");
                }
            }

            res.append("]");
        } else if (src.getClass().isArray()) {
            res.append("[");

            for (int i = 0; i < Array.getLength(src); i++) {
                Object o = Array.get(src, i);
                toJson(o);
                if (i < Array.getLength(src) - 1) {
                    res.append(",");
                }
            }

            res.append("]");
        } else {
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

                        toJson(field.get(src));

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
        }

        return res.toString();
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        PersonData relative1 = new PersonData();
        PersonData relative2 = new PersonData("Helen", 31, 9000.00f, null, Arrays.asList("Customer Manager", "Cashier"));
        PersonData personData = new PersonData("Ivan", 33, 12000.00f, new PersonData[]{relative1, relative2}, Arrays.asList("IT Developer", "Programmer", "Anykey worker"));

        String jsonString = gson.toJson(personData);
        System.out.println("Gson:   " + jsonString);

        JSONSerializer jsonSerializer = new JSONSerializer();
        String myJSONString = jsonSerializer.toJson(personData);
        System.out.println("myJson: " + myJSONString);
        System.out.println();

        PersonData personData2 = gson.fromJson(jsonString, PersonData.class);
        System.out.println("personData.equals(personData2): " + personData.equals(personData2));
        System.out.println("jsonString.equals(myJSONString): " + jsonString.equals(myJSONString));
    }
}
