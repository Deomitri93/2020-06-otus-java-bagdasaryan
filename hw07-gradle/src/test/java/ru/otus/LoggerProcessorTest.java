package ru.otus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.DataProvider;
import ru.otus.processor.homework.LoggerProcessor2SecondsException;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerProcessorTest {
    private Message message;

    @BeforeEach
    void initData() {
        message = new Message.Builder().field1("field1").field2("field2").field3("field3").build();
    }

    @DisplayName("Тестируем наличие исключения, когда секунда четная")
    @Test
    void LoggerProcessor2SecondsExceptionTestEven() {
        DataProvider evenSecondDataProvider = () -> 0;
        Processor processor = new LoggerProcessor2SecondsException(evenSecondDataProvider);
        Exception exception = assertThrows(RuntimeException.class, () -> processor.process(message));

        String expectedMessage = "even second exception";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @DisplayName("Тестируем отсутствие исключения, когда секунда нечетная")
    @Test
    void LoggerProcessor2SecondsExceptionTestOdd() {
        DataProvider oddSecondDataProvider = () -> 1;
        Processor processor = new LoggerProcessor2SecondsException(oddSecondDataProvider);
        processor.process(message);
    }
}
