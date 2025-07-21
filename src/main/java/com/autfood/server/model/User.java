package com.autfood.server.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private Integer id;
    private String  username;
    private String  email;
    private String  passwordHash;
    private String  salt;
    private Role    role;

    public User() { }

    public User(Integer id, String username, String email,
                String passwordHash, String salt, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.salt = salt;
        this.role = role;
    }
    /* Getter / Setter ها (مثل قبل) */
}

