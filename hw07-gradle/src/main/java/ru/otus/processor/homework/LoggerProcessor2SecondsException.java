package ru.otus.processor.homework;

import ru.otus.Message;
import ru.otus.processor.Processor;

import java.time.LocalDateTime;

public class LoggerProcessor2SecondsException implements Processor {
    private final Processor processor;

    public LoggerProcessor2SecondsException(Processor processor) {
        this.processor = processor;
    }

    @Override
    public Message process(Message message) {
        if (LocalDateTime.now().getSecond() % 2 == 0) {
            throw new RuntimeException(this.getClass().getName() + " - runtime exception");
        }

        System.out.println("log processing message:" + message);
        return processor.process(message);
    }
}
