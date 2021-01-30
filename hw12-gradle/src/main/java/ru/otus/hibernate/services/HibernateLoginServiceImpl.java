package ru.otus.hibernate.services;

import ru.otus.model.Role;
import ru.otus.model.User;

import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.jetty.security.AbstractLoginService;
import org.eclipse.jetty.util.security.Password;
import ru.otus.services.ServiceUserDao;

public class HibernateLoginServiceImpl extends AbstractLoginService {

    private final ServiceUserDao serviceUserDao;

    public HibernateLoginServiceImpl(ServiceUserDao serviceUserDao) {
        this.serviceUserDao = serviceUserDao;
    }

    @Override
    protected String[] loadRoleInfo(UserPrincipal user) {
        Optional<User> dbUser = serviceUserDao.findByLogin(user.getName());
        return dbUser.map(value -> value.getRoles().stream().map(Role::getRole).collect(Collectors.toList()).toArray(new String[value.getRoles().size()])).orElse(null);
    }

    @Override
    protected UserPrincipal loadUserInfo(String login) {
        Optional<User> dbUser = serviceUserDao.findByLogin(login);
        return dbUser.map(u -> new UserPrincipal(u.getLogin(), new Password(u.getPassword()))).orElse(null);
    }
}
