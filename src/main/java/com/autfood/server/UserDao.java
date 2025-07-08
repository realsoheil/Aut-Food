package com.autfood.server.dao;

import com.autfood.server.model.User;
import com.autfood.server.util.DbUtil;

import java.sql.*;

public class UserDao {

    public User save(User u) throws SQLException {
        String sql = """
        INSERT INTO users(username,email,password_hash,salt,role)
        VALUES(?,?,?,?,?)
        """;
        try (PreparedStatement ps = DbUtil.getConnection()
                .prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1,u.getUsername());
            ps.setString(2,u.getEmail());
            ps.setString(3,u.getPasswordHash());
            ps.setString(4,u.getSalt());
            ps.setString(5,u.getRole().name());
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) u.setId(rs.getInt(1));
            }
        }
        return u;
    }

    public User findById(int id) throws SQLException {
        String sql = "SELECT id, username, password FROM users WHERE id=?";
        try (PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"));
                }
                return null;
            }
        }
    }

    public User findByUsername(String un) throws SQLException {
        String sql = "SELECT id, username, password FROM users WHERE username=?";
        try (PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql)) {
            ps.setString(1, un);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"));
                }
                return null;
            }
        }
    }
}

