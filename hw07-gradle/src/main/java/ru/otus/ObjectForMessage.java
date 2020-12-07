package ru.otus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObjectForMessage {
    private List<String> data;

    public ObjectForMessage(List<String> data) {
        this.data = new ArrayList<>();
        this.data.addAll(data);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectForMessage that = (ObjectForMessage) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
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
