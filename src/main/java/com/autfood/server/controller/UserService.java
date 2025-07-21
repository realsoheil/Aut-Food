package com.autfood.server.controller;

import com.autfood.server.dao.UserDao;
import com.autfood.server.model.Role;
import com.autfood.server.model.User;
import com.autfood.server.util.HashUtil;

import java.sql.SQLException;

public class UserService {

    private final UserDao dao = new UserDao();

    /** ثبت کاربر جدید */
    public User register(String username, String email, String password) throws Exception {
        // اعتبارسنجی: یکتا بودن username یا email
        if (dao.findByUsername(username) != null) {
            throw new IllegalArgumentException("username already exists");
        }
        if (dao.findByEmail(email) != null) {
            throw new IllegalArgumentException("email already exists");
        }

        // ساخت salt و هش کردن رمز عبور
        String salt = HashUtil.randomSalt();
        String passwordHash = HashUtil.hashPassword(password, salt);

        // ساخت یوزر
        User u = new User(null, username, email, passwordHash, salt, Role.BUYER);
        return dao.save(u);
    }

    /** بازیابی کاربر با id */
    public User getById(int id) throws SQLException {
        return dao.findById(id);
    }
}


