package ru.otus;

public class LoggedMethodsClass {
    public LoggedMethodsClass() {
    }

    @Log
    public void mustBeLogged(String param) {
        System.out.println("mustBeLogged(" + param + ")");
    }

    public void notForLogging(String param) {
        System.out.println("notForLogging(" + param + ")");
    }

    @Log
    public void mustBeLogged2(String param1, int param2) {
        System.out.println("mustBeLogged2(" + param1 + ", " + param2 + ")");
    }

//    public String toString() {
//        return "MyClassImpl{}";
//    }
}
