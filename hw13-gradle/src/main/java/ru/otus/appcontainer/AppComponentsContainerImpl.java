package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... configClasses) {
        List<Class<?>> configClassesSorted = Arrays.stream(configClasses)
                .filter(clazz -> clazz.isAnnotationPresent(AppComponentsContainerConfig.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponentsContainerConfig.class).order()))
                .collect(Collectors.toList());

        if (configClassesSorted.size() == 0) {
            throw new IllegalArgumentException(String.format("Couldn't find any classes annotated with %s", AppComponentsContainerConfig.class.getName()));
        }

        configClassesSorted.forEach(this::processConfigClass);
    }

    public AppComponentsContainerImpl(String configClassesPath) {
        this(new Reflections(configClassesPath)
                .getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .toArray(new Class[0]));
    }

    private Object getComponentByType(Class<?> componentType) {
        List<Object> components = appComponents.stream()
                .filter(t -> componentType.isAssignableFrom(t.getClass()))
                .collect(Collectors.toList());

        if (components.size() == 0) {
            throw new IllegalArgumentException(String.format("Couldn't find component for class %s", componentType.getName()));
        } else if (components.size() > 1) {
            throw new IllegalArgumentException(String.format("Found two or more implementations for class %s", componentType.getName()));
        }

        return components.get(0);
    }

    private Object createComponent(Object config, Method creatorMethod, Object... args) throws Exception {
        return creatorMethod.invoke(config, args);
    }

    private void processConfigClass(Class<?> configClass) {
        Object configClassInstance = createInstance(configClass);

        List<Method> methods = Arrays.stream(configClass.getMethods())
                .filter(method -> method.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());

        if (methods.size() == 0) {
            throw new IllegalArgumentException(String.format("Couldn't find any methods annotated with %s", AppComponent.class.getName()));
        }

        methods.forEach(method -> processConfigClassMethod(method, configClassInstance));
    }

    private void processConfigClassMethod(Method configClassMethod, Object configClassInstance) {
        Class<?>[] formalParams = configClassMethod.getParameterTypes();
        Object[] factParams = new Object[formalParams.length];

        for (int i = 0; i < factParams.length; i++) {
            factParams[i] = getComponentByType(formalParams[i]);
        }

        try {
            Object newComponent = createComponent(configClassInstance, configClassMethod, factParams);
            appComponentsByName.put(configClassMethod.getAnnotation(AppComponent.class).name(), newComponent);
            appComponents.add(newComponent);
        } catch (Exception e) {
            System.err.printf("Couldn't create instance of class %s", configClassMethod.getAnnotation(AppComponent.class).name());
            e.printStackTrace();
        }
    }

    private Object createInstance(Class<?> clazz) {
        Constructor<?> constructor;
        try {
            constructor = clazz.getDeclaredConstructor();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            System.err.printf("Couldn't find any default constructor for class %s", clazz.getName());
            return null;
        }

        try {
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            System.err.printf("Couldn't create instance of class %s", clazz.getName());
            return null;
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) getComponentByType(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
