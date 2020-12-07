package ru.otus.processor.homework;

import java.time.LocalTime;

public class DataProviderImpl implements DataProvider {
    @Override
    public int currentSecond() {
        return LocalTime.now().getSecond();
    }
}
