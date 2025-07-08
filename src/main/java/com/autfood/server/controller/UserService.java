package com.autfood.server.controller;

import com.autfood.server.dao.UserDao;
import com.autfood.server.model.User;

import java.sql.SQLException;

public class UserService {

    private final UserDao dao = new UserDao();

    /** ثبت کاربر جدید */
    public User register(String username, String password) throws Exception {
        // اعتبارسنجی ساده: یکتا بودن username
        if (dao.findByUsername(username) != null) {
            throw new IllegalArgumentException("username already exists");
        }
        User u = new User(null, username, password);
        return dao.save(u);
    }

    /** بازیابی کاربر با id */
    public User getById(int id) throws SQLException {
        return dao.findById(id);
    }
}

