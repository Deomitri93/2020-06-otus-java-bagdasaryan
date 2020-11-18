package ru.otus;

public class LoggedMethodsClass {
    public LoggedMethodsClass() {
    }

    @Log
    public void mustBeLogged(boolean param) {

    }

    public void notForLogging(String param1, byte param2, char param3, short param4) {

    }

    @Log
    public void mustBeLogged2(boolean param1, int param2) {

    }

    @Override
    public String toString() {
        return "MyClassImpl{}";
    }
}
