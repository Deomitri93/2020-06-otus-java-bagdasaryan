package ru.otus.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "login", unique = true)
    private String login;

    @Column(name = "password")
    private String password;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Column(name = "roles")
    Set<Role> roles = new HashSet<>();

    public User() {
    }

    public User(String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
    }

    public User(String name, String login, String password, Set<Role> roles) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.roles = roles;
    }

    public User(User user) {
        this.name = user.name;
        this.login = user.login;
        this.password = user.password;
    }

    public User(UserDTO userDTO){
        this.name = userDTO.getName();
        this.login = userDTO.getLogin();
        this.password = userDTO.getPassword();
        this.roles = userDTO.getRolesAsSet();
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRolesAsString() {
        return String.join(",", roles.stream().map(r -> r.getRole()).collect(Collectors.toList()));
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name '" + name + '\'' +
                ", login '" + login + '\'' +
                ", password '" + this.password + '\'' +
                ", role '" + roles.toString() + '\'' +
                '}';
    }
}
