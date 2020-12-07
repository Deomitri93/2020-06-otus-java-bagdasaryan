package ru.otus.processor.homework;

import ru.otus.Message;
import ru.otus.processor.Processor;

public class LoggerProcessor2SecondsException implements Processor {
    private final DataProvider dataProvider;

    public LoggerProcessor2SecondsException(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public Message process(Message message) {
        if (dataProvider.currentSecond() % 2 == 0) {
            throw new RuntimeException(this.getClass().getName() + " - even second exception");
        }

        System.out.println("log processing message:" + message);
        return message;
    }
}
