package ru.otus;

/*
    java -javaagent:proxyASM.jar -jar proxyASM.jar
*/


public class ProxyASM {
    public static void main(String[] args) {
        LoggedMethodsClass loggedMethodsClass = new LoggedMethodsClass();

//        for (Method method : loggedMethodsClass.getClass().getMethods()) {
//            for(Annotation annotation : method.getAnnotations()){
//                if(method.isAnnotationPresent(Log.class)){
//                    System.out.println(method.getName() + " Log");
//                }
//            }
//        }


        loggedMethodsClass.mustBeLogged(true);
        loggedMethodsClass.notForLogging("StringParam1");
        loggedMethodsClass.mustBeLogged2("StringParam1", 127);
    }
}
