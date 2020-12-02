package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotSame;

public class MessageTest {
    @DisplayName("Тестируем deep copy для ObjectForMessage")
    @Test
    void ObjectForMessageDeepCopyTest() {
        Object o;


        ObjectForMessage objectForMessage1 = new ObjectForMessage(Arrays.asList("msg1", "msg2"));
        ObjectForMessage objectForMessage2 = new ObjectForMessage(objectForMessage1.getData());

        System.out.println("objectForMessage1 (before): " + objectForMessage1);
        System.out.println("objectForMessage2 (before): " + objectForMessage2);
        System.out.println();

        assertEquals(objectForMessage1, objectForMessage2);
        assertNotSame(objectForMessage1, objectForMessage2);

        objectForMessage1.getData().set(0, "newMsg");

        System.out.println("objectForMessage1 (after): " + objectForMessage1);
        System.out.println("objectForMessage2 (after): " + objectForMessage2);
        System.out.println();

        assertNotEquals(objectForMessage1, objectForMessage2);
        assertNotSame(objectForMessage1, objectForMessage2);
    }

    @DisplayName("Тестируем deep copy для Message")
    @Test
    void MessageDeepCopyTest() {
        ObjectForMessage objectForMessage1 = new ObjectForMessage(Arrays.asList("msg1", "msg2"));
        Message message1 = new Message.Builder().field1("field1").field3("field3").field12("field12").field13(objectForMessage1).build();
        Message message2 = message1.toBuilder().build();

        System.out.println("message1 (before): " + message1);
        System.out.println("message2 (before): " + message2);
        System.out.println();

        assertEquals(message1, message2);
        assertNotSame(message1, message2);

        message1.getField13().getData().set(0, "newMsg1");
        message1.getField13().getData().add("msg3");

        System.out.println("message1 (after): " + message1);
        System.out.println("message2 (after): " + message2);
        System.out.println();

        assertNotEquals(message1, message2);
        assertNotSame(message1, message2);
    }
}
