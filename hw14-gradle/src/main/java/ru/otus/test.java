package ru.otus;

import ru.otus.domain.Role;
import ru.otus.domain.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class test {
    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        users.add( new User("Крис Гир", "admin1", "admin1", new HashSet<>(Arrays.asList(new Role("user"), new Role("admin")))));
        users.add(new User("Ая Кэш", "user1", "user1"));
        users.add(new User("Десмин Боргес", "user2", "user2"));
        users.add(new User("Кетер Донохью", "user3", "user3"));
        users.add(new User("Стивен Шнайдер", "admin2", "admin2"));
        users.add(new User("Джанет Вэрни", "admin4", "admin4"));
        users.add(new User("Брэндон Смит", "admin5", "admin5"));

    }
}
