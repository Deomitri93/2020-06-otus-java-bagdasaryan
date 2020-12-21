package ru.otus;

public class Demo {
    public static void main(String[] args) {
        CounterSynchronized counterSynchronized = new CounterSynchronized(2, 1, 10, 3);
        counterSynchronized.startAllThreads();
    }
}
