package ru.otus.listener.homework;

import ru.otus.Message;
import ru.otus.listener.Listener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListenerMessageHistory implements Listener {
    private List<String> msgHistory = new ArrayList<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        msgHistory.add(String.format("[%s]: oldMsg:%s - newMsg:%s", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), oldMsg, newMsg));
    }

    public List<String> printHistory() {
        return msgHistory;
    }
}
