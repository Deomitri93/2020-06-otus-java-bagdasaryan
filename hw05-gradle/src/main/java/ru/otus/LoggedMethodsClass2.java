package ru.otus;

public class LoggedMethodsClass2 {
    public void notLoggedMethod(String param1){

    }

    @Log
    public void loggedMethod(int param1, long param2){

    }
}
