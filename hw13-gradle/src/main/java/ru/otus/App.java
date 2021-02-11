package ru.otus;

import ru.otus.appcontainer.AppComponentsContainerImpl;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.complexconfig.AppConfig1;
import ru.otus.complexconfig.AppConfig2;
import ru.otus.config.AppConfig;
import ru.otus.services.GameProcessor;
import ru.otus.services.GameProcessorImpl;

/*
В классе AppComponentsContainerImpl реализовать обработку, полученной в конструкторе конфигурации,
основываясь на разметке аннотациями из пакета appcontainer. Так же необходимо реализовать методы getAppComponent.
В итоге должно получиться работающее приложение. Менять можно только класс AppComponentsContainerImpl.

PS Приложение представляет из себя тренажер таблицы умножения)
*/

public class App {
    public static void main(String[] args) throws Exception {
        // Опциональные варианты
        AppComponentsContainer container1 = new AppComponentsContainerImpl(AppConfig2.class, AppConfig1.class);

        // Тут можно использовать библиотеку Reflections (см. зависимости)
        AppComponentsContainer container2 = new AppComponentsContainerImpl("ru.otus.config");

        // Обязательный вариант
        AppComponentsContainer container3 = new AppComponentsContainerImpl(AppConfig.class);

        // Приложение должно работать в каждом из указанных ниже вариантов
        GameProcessor gameProcessor1 = container1.getAppComponent(GameProcessor.class);
        GameProcessor gameProcessor2 = container2.getAppComponent(GameProcessorImpl.class);
        GameProcessor gameProcessor3 = container3.getAppComponent("gameProcessor");

        gameProcessor1.startGame();
        gameProcessor2.startGame();
        gameProcessor3.startGame();
    }
}
