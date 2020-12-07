package ru.otus;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.json.JSONSerializer;
import ru.otus.json.PersonData;

import java.util.Arrays;

@DisplayName("Тест JSONSerializer")
public class JSONSerializerTest {
    Gson gson;
    PersonData originalObj;
    JSONSerializer jsonSerializer;

    @BeforeEach
    void beforeEachTestInitialization() {
        gson = new Gson();
        PersonData relative1 = new PersonData();
        PersonData relative2 = new PersonData("Helen", 31, 9000.00f, null, Arrays.asList("Customer Manager", "Cashier"));
        originalObj = new PersonData("Ivan", 33, 12000.00f, new PersonData[]{relative1, relative2}, Arrays.asList("IT Developer", "Programmer", "Anykey worker"));
        jsonSerializer = new JSONSerializer();
    }

    @Test
    @DisplayName("Тестируем эквивалентность генерируемых строк")
    void testJSONSerializedStringsEquals() {
        String originalJSONString = gson.toJson(originalObj);
        String myJSONString = jsonSerializer.toJson(originalObj);

        assertEquals(originalJSONString, myJSONString);
    }

    @Test
    @DisplayName("Тестируем эквивалентность оригинального и десериализованного объекта")
    void testJSONDeserializedEquals() {
        String myJSONString = jsonSerializer.toJson(originalObj);
        PersonData deserializedObj = gson.fromJson(myJSONString, PersonData.class);

        assertEquals(originalObj, deserializedObj);
    }
}
