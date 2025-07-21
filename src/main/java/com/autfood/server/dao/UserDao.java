package com.autfood.server.dao;

import com.autfood.server.model.Role;
import com.autfood.server.model.User;
import com.autfood.server.util.DbUtil;

import java.sql.*;

public class UserDao {

    public User save(User u) throws SQLException {
        String sql = """
            INSERT INTO users(username, email, password_hash, salt, role)
            VALUES (?, ?, ?, ?, ?)
        """;
        try (PreparedStatement ps = DbUtil.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPasswordHash());
            ps.setString(4, u.getSalt());
            ps.setString(5, u.getRole().name());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    u.setId(rs.getInt(1));
                }
            }
        }
        return u;
    }

    public User findById(int id) throws SQLException {
        String sql = """
            SELECT id, username, email, password_hash, salt, role
            FROM users
            WHERE id = ?
        """;
        try (PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUser(rs);
                }
            }
        }
        return null;
    }

    public static void createUsersTable() throws SQLException {
        String sql = """
        CREATE TABLE IF NOT EXISTS users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            username TEXT UNIQUE NOT NULL,
            email TEXT UNIQUE NOT NULL,
            password_hash TEXT NOT NULL,
            salt TEXT NOT NULL,
            role TEXT NOT NULL
        );
    """;

        try (Statement st = DbUtil.getConnection().createStatement()) {
            st.execute(sql);
        }
    }


    public User findByUsername(String username) throws SQLException {
        String sql = """
            SELECT id, username, email, password_hash, salt, role
            FROM users
            WHERE username = ?
        """;
        try (PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUser(rs);
                }
            }
        }
        return null;
    }

    public User findByEmail(String email) throws SQLException {
        String sql = """
            SELECT id, username, email, password_hash, salt, role
            FROM users
            WHERE email = ?
        """;
        try (PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return extractUser(rs);
                }
            }
        }
        return null;
    }

    private User extractUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("email"),
                rs.getString("password_hash"),
                rs.getString("salt"),
                Role.valueOf(rs.getString("role"))
        );
    }
}


