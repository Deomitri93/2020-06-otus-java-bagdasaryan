package ru.otus.processor.homework;

import ru.otus.Message;
import ru.otus.processor.Processor;

public class ProcessorSwapFields11And12 implements Processor {

    @Override
    public Message process(Message message) {
        var field11OldValue = message.getField11();
        var field12OldValue = message.getField12();
        return message.toBuilder().field11(field12OldValue).field12(field11OldValue).build();
    }
}
