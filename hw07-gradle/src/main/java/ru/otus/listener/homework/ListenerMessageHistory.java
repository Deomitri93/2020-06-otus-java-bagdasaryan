package ru.otus.listener.homework;

import ru.otus.Message;
import ru.otus.listener.Listener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListenerMessageHistory implements Listener {
    private class MessageHistoryRecord {
        private Message oldMessage;
        private Message newMessage;
        private LocalDate dateMessage;

        public MessageHistoryRecord(Message oldMessage, Message newMessage, LocalDate dateMessage) {
            this.oldMessage = oldMessage;
            this.newMessage = newMessage;
            this.dateMessage = dateMessage;
        }

        public String toString() {
            return String.format("\n[%s]:\noldMsg:%s\nnewMsg:%s", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), oldMessage, newMessage);
        }
    }

    private List<MessageHistoryRecord> msgHistory = new ArrayList<>();

    @Override
    public void onUpdated(Message oldMsg, Message newMsg) {
        msgHistory.add(new MessageHistoryRecord(oldMsg, newMsg, LocalDate.now()));
    }

    public List<String> printHistory() {
        List<String> res = new ArrayList<>();
        for (MessageHistoryRecord record : msgHistory) {
            res.add(record.toString());
        }
        return res;
    }
}
