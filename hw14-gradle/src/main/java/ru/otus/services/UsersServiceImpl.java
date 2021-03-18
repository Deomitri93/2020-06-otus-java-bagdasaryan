package ru.otus.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.domain.Role;
import ru.otus.domain.User;
import ru.otus.repository.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

@Service
public class UsersServiceImpl implements UsersService {
    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() {
        this.save(new User("Крис Гир", "admin1", "admin1", new HashSet<>(Arrays.asList(new Role("user"), new Role("admin")))));
        this.save(new User("Ая Кэш", "user1", "user1"));
        this.save(new User("Десмин Боргес", "user2", "user2"));
        this.save(new User("Кетер Донохью", "user3", "user3"));
        this.save(new User("Стивен Шнайдер", "admin2", "admin2"));
        this.save(new User("Джанет Вэрни", "admin4", "admin4"));
        this.save(new User("Брэндон Смит", "admin5", "admin5"));
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public User findByName(String name) {
        return userRepository.findByName(name);
    }

    @Override
    public User findRandom() {
        List<User> users = userRepository.findAll();
        Random r = new Random();
        return users.stream().skip(r.nextInt(users.size())).findFirst().orElse(null);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }
}
