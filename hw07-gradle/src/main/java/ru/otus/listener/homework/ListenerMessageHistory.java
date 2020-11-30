package ru.otus.listener.homework;

import ru.otus.Message;
import ru.otus.listener.Listener;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ListenerMessageHistory implements Listener {
    private class MessageHistoryRecord {
        private final Message oldMessage;
        private final Message newMessage;
        private final LocalDate dateMessage;

        public MessageHistoryRecord(Message oldMessage, Message newMessage, LocalDate dateMessage) {
            this.oldMessage = (Message) serializeAndDeserialize(oldMessage);
            this.newMessage = (Message) serializeAndDeserialize(newMessage);
            this.dateMessage = (LocalDate) serializeAndDeserialize(dateMessage);
        }

        public String toString() {
            return String.format("\n[%s]:\noldMsg:%s\nnewMsg:%s", LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), oldMessage, newMessage);
        }

        private Object serializeAndDeserialize(Object obj) {
            try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                 ObjectOutputStream oos = new ObjectOutputStream(bos)) {

                oos.writeObject(obj);
                byte[] byteData = bos.toByteArray();

                ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
                return new ObjectInputStream(bis).readObject();
            } catch (ClassNotFoundException | IOException ex) {
                System.err.println(ex);
            }
            return null;
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
