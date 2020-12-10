package ru.otus.json;

import com.google.gson.Gson;

import java.util.Arrays;

public class Demo {
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
