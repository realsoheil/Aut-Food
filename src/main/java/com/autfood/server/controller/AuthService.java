package com.autfood.server.controller;

import com.autfood.server.dao.UserDao;
import com.autfood.server.model.Role;
import com.autfood.server.model.User;
import com.autfood.server.util.HashUtil;
import com.autfood.server.util.SessionStore;

import java.sql.SQLException;

public class AuthService {
    private final UserDao dao = new UserDao();

    public User register(String username,String email,String password) throws Exception{
        if(dao.findByUsername(username)!=null) throw new IllegalArgumentException("username exists");
        if(dao.findByEmail(email)!=null)       throw new IllegalArgumentException("email exists");

        String salt = HashUtil.randomSalt();
        String hash = HashUtil.sha256(password+salt);

        User u = new User(null,username,email,hash,salt, Role.BUYER);
        return dao.save(u);
    }

    public String login(String username,String password) throws Exception{
        User u = dao.findByUsername(username);
        if(u==null) throw new IllegalArgumentException("not found");
        String hash = HashUtil.sha256(password+u.getSalt());
        if(!hash.equals(u.getPasswordHash())) throw new IllegalArgumentException("bad credentials");
        return SessionStore.createToken(u.getId());
    }

    public Integer validate(String token){
        return SessionStore.getUserId(token); // می‌تواند null برگردد
    }

    public Role getUserRole(int userId) throws SQLException {
        User u = dao.findById(userId);
        return u != null ? u.getRole() : null;
    }
}


