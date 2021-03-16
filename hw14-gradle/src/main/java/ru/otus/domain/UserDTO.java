package ru.otus.domain;

import java.util.Arrays;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class UserDTO extends User {
    private String roles;

    public void setRoles(String roles){
        this.roles = roles;
    }

    public String getRoles() {
        return roles;
    }

    public Set<Role> getRolesAsSet(){
        if(roles != null){
            return Arrays.stream(roles.split(",")).map(s -> new Role(s.trim())).collect(toSet());
        }else{
            return null;
        }
    }
}
