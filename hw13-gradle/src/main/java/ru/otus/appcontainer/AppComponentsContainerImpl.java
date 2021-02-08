package ru.otus.appcontainer;

import org.reflections.Reflections;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?>... configClasses) throws Exception {
        List<Class<?>> configClassesList = removeClassesWithoutAnnotation(AppComponentsContainerConfig.class, configClasses);

        configClassesList.sort(Comparator.comparingInt(m -> m.getAnnotation(AppComponentsContainerConfig.class).order()));

        for (Class<?> configClass : configClassesList) {
            Object configClassInstance = getNoParamConstructor(configClass).newInstance();

            List<Method> currentConfigClassMethods = removeMethodsWithoutAnnotation(AppComponent.class, configClass.getMethods());

            currentConfigClassMethods.sort(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()));

            for (Method method : currentConfigClassMethods) {
                Class<?>[] formalParams = method.getParameterTypes();
                Object[] factParams = new Object[formalParams.length];

                for (int i = 0; i < factParams.length; i++) {
                    factParams[i] = getComponentByType(formalParams[i]);
                }

                appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), createComponent(configClassInstance, method, factParams));
            }
        }
    }

    public AppComponentsContainerImpl(String configClassesPath) throws Exception {
        this(new Reflections(configClassesPath)
                .getTypesAnnotatedWith(AppComponentsContainerConfig.class)
                .toArray(new Class[0]));
    }

    private List<Class<?>> removeClassesWithoutAnnotation(Class<? extends Annotation> annotation, Class<?>... classes) {
        List<Class<?>> resConfigClasses = new ArrayList<>();

        if (classes == null) {
            throw new IllegalArgumentException("Class is null");
        }

        for (Class<?> configClass : classes) {
            if (configClass.isAnnotationPresent(annotation)) {
                resConfigClasses.add(configClass);
            }
        }

        if (resConfigClasses.size() == 0) {
            throw new IllegalArgumentException(String.format("Couldn't find any classes annotated with %s", annotation.getName()));
        }

        return resConfigClasses;
    }

    private List<Method> removeMethodsWithoutAnnotation(Class<? extends Annotation> annotation, Method... methods) {
        List<Method> resCreationMethods = new ArrayList<>();

        if (methods == null) {
            throw new IllegalArgumentException("Method is null");
        }

        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                resCreationMethods.add(method);
            }
        }

        if (resCreationMethods.size() == 0) {
            throw new IllegalArgumentException(String.format("Couldn't find any methods annotated with %s", annotation.getName()));
        }

        return resCreationMethods;
    }

    private Constructor<?> getNoParamConstructor(Class<?> clazz) throws NoSuchMethodException {
        for (Constructor<?> constructor : clazz.getConstructors()) {
            if (constructor.getParameterCount() == 0) {
                return constructor;
            }
        }

        throw new NoSuchMethodException(String.format("Given class has no default constructor %s", clazz));
    }

    private Object getComponentByType(Class<?> componentType) {
        List<Object> components = appComponentsByName.values().stream()
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

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) getComponentByType(componentClass);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
