package ru.otus;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.ListenerMessageHistory;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.LoggerProcessor2SecondsException;
import ru.otus.processor.homework.ProcessorSwapFields11And12;

import java.util.List;

public class HomeWork {

    /*
     Реализовать to do:
       1. Добавить поля field11 - field13 (для field13 используйте класс ObjectForMessage)
       2. Сделать процессор, который поменяет местами значения field11 и field12
       3. Сделать процессор, который будет выбрасывать исключение в четную секунду (сделайте тест с гарантированным результатом)
       4. Сделать Listener для ведения истории: старое сообщение - новое (подумайте, как сделать, чтобы сообщения не портились)
     */

    public static void main(String[] args) {
        /*
           по аналогии с Demo.class
           из элеменов "to do" создать new ComplexProcessor и обработать сообщение
         */

        List<Processor> processors = List.of(new LoggerProcessor2SecondsException(new ProcessorSwapFields11And12()));
        var complexProcessor = new ComplexProcessor(processors, (ex) -> {
            System.out.println("[Exception]: " + ex.getMessage());
        });

        var listenerMessageHistory = new ListenerMessageHistory();
        complexProcessor.addListener(listenerMessageHistory);

        var message = new Message.Builder()
                .field1("field1")
                .field5("field5")
                .field11("field11")
                .field12("field12")
                .field13(new ObjectForMessage(List.of("Obj4Msg1", "Obj4Msg2", "Obj4Msg3")))
                .build();

        var result = complexProcessor.handle(message);
        System.out.println("result:" + result);
        System.out.println("Message history: " + listenerMessageHistory.printHistory());

        complexProcessor.removeListener(listenerMessageHistory);
    }
}
