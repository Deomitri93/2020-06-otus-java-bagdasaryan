package ru.otus;

import java.io.Serializable;
import java.util.List;

public class ObjectForMessage implements Serializable {
    private List<String> data;

    public ObjectForMessage(List<String> data) {
        this.data = data;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
