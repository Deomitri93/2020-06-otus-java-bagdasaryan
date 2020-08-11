import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Executor {
    private static void statisticsMessage(int testsExecutedCounter, int testsPassedCounter){
        String statisticsMessage = "|   " + testsPassedCounter + " test" + (testsPassedCounter == 1 ? "" : "s" + " of " + testsExecutedCounter + " passed" + (testsExecutedCounter - testsPassedCounter > 0 ? " (failed " + (testsExecutedCounter - testsPassedCounter) + ")" : "")) + "   |";
        String statisticsMessageHBorder = " " + new String(new char[statisticsMessage.length() - 2]).replace('\0', '-') + " ";
        String statisticsMessageMiddlePart = "|" + (new String(new char[statisticsMessage.length() - 2]).replace('\0', ' ')) + "|";

        System.out.println();
        System.out.println(statisticsMessageHBorder);
        System.out.println(statisticsMessageMiddlePart);
        System.out.println(statisticsMessage);
        System.out.println(statisticsMessageMiddlePart);
        System.out.println(statisticsMessageHBorder);
        System.out.println();
    }

    public static void main(String[] args) throws ClassNotFoundException {
        String testedClassName;

        if(args.length > 0){
            testedClassName = args[0];
        }else{
            throw new ClassNotFoundException("No class name passed");
        }

        // determining class for passed tested class name
        Class<?> clazz;
        try{
            clazz = Class.forName(testedClassName);
        }catch (ClassNotFoundException e){
            throw new ClassNotFoundException("No class with name '" + testedClassName + "' was found");
        }

        // searching default constructor
        Constructor<?> testedClassConstructor = null;
        try{
            testedClassConstructor = clazz.getConstructor();
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }

        // searching for all methods, annotated with @Test, @Before or @After
        // and adding them to lists: testMethods, beforeMethods and afterMethods respectively
        List<Method> beforeMethods = new ArrayList<>();
        List<Method> testMethods = new ArrayList<>();
        List<Method> afterMethods = new ArrayList<>();
        for (Method method: clazz.getDeclaredMethods()){
            if(method.isAnnotationPresent(Test.class)){
                testMethods.add(method);
            }else{
                if(method.isAnnotationPresent(Before.class)){
                    beforeMethods.add(method);
                }else{
                    if(method.isAnnotationPresent(After.class)){
                        afterMethods.add(method);
                    }
                }
            }
        }

        int testsExecutedCounter = 0;
        int testsPassedCounter = 0;

        // iterating through all methods, annotated with @Test
        for(Method testMethod: testMethods){
            // for every method, annotated with @Test, creating new instance of tested class
            Object testedClassInstance = null;
            try{
                if(testedClassConstructor != null){
                    testedClassConstructor.setAccessible(true);
                }else{
                    throw new NullPointerException();
                }
                testedClassInstance = testedClassConstructor.newInstance();
            }catch (InstantiationException | IllegalAccessException | InvocationTargetException e){
                e.printStackTrace();
            }

            // for every method, annotated with @Test, invoking all methods, annotated with @Before
            for (Method beforeMethod: beforeMethods){
                beforeMethod.setAccessible(true);
                try{
                    beforeMethod.invoke(testedClassInstance);
                }catch (IllegalAccessException | InvocationTargetException e){
                    e.printStackTrace();
                }
            }

            // invoking method, annotated with @Test
            testsExecutedCounter++;
            testMethod.setAccessible(true);
            try{
                testMethod.invoke(testedClassInstance);
                testsPassedCounter++;
            }catch (Exception e){
                e.printStackTrace();
            }

            //for every method, annotated with @Test, invoking all methods, annotated with @After
            for (Method afterMethod: afterMethods){
                afterMethod.setAccessible(true);
                try{
                    afterMethod.invoke(testedClassInstance);
                }catch (IllegalAccessException | InvocationTargetException e){
                    e.printStackTrace();
                }
            }
        }

        statisticsMessage(testsExecutedCounter, testsPassedCounter);
    }
}