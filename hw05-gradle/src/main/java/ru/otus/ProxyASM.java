package ru.otus;

/*
    java -javaagent:proxyASM.jar -jar proxyASM.jar
*/

public class ProxyASM {
    public static void main(String[] args) {
        LoggedMethodsClass loggedMethodsClass = new LoggedMethodsClass();

        loggedMethodsClass.mustBeLogged(true);

        byte param2 = 15;
        short param4 = 512;
        loggedMethodsClass.notForLogging("StringParam1", param2, 'j', param4);

        loggedMethodsClass.mustBeLogged2(false, 127);

        LoggedMethodsClass2 loggedMethodsClass2 = new LoggedMethodsClass2();

        loggedMethodsClass2.notLoggedMethod("String param1");

        loggedMethodsClass2.loggedMethod(12, 1024);
    }
}
