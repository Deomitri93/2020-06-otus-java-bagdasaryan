package ru.otus.services;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import ru.otus.domain.Role;
import ru.otus.domain.User;
import ru.otus.repository.UserRepository;

import java.util.Arrays;
import java.util.HashSet;

@Service
public class UserInitService implements InitializingBean {

    private final UserRepository userRepository;

    public UserInitService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void afterPropertiesSet() {
        userRepository.save(new User("Крис Гир", "admin1", "admin1", new HashSet<>(Arrays.asList(new Role("user"), new Role("admin")))));
        userRepository.save(new User("Ая Кэш", "user1", "user1"));
        userRepository.save(new User("Десмин Боргес", "user2", "user2"));
        userRepository.save(new User("Кетер Донохью", "user3", "user3"));
        userRepository.save(new User("Стивен Шнайдер", "admin2", "admin2"));
        userRepository.save(new User("Джанет Вэрни", "admin4", "admin4"));
        userRepository.save(new User("Брэндон Смит", "admin5", "admin5"));
    }
}