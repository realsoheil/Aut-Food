package com.autfood.server.dao;

import com.autfood.server.model.Restaurant;
import com.autfood.server.model.Restaurant.Status;
import com.autfood.server.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * عملیات JDBC مربوط به جدول «restaurants».
 */
public class RestaurantDao {

    /* ─────────────────  INSERT  ───────────────── */
    public Restaurant save(Restaurant r) throws SQLException {
        final String sql = """
            INSERT INTO restaurants(owner_id, name, status, description)
            VALUES (?, ?, ?, ?)
        """;
        try (PreparedStatement ps = DbUtil.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt   (1, r.getOwnerId());
            ps.setString(2, r.getName());
            ps.setString(3, r.getStatus().name());
            ps.setString(4, r.getDescription());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) r.setId(rs.getInt(1));
            }
        }
        return r;
    }

    /* ─────────────────  SELECT BY ID  ──────────── */
    public Restaurant findById(int id) throws SQLException {
        final String sql = "SELECT * FROM restaurants WHERE id = ?";
        try (PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    /* ─────────────────  LIST APPROVED  ─────────── */
    public List<Restaurant> listApproved() throws SQLException {
        final String sql = "SELECT * FROM restaurants WHERE status = 'APPROVED'";
        try (Statement st = DbUtil.getConnection().createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            List<Restaurant> list = new ArrayList<>();
            while (rs.next()) list.add(map(rs));
            return list;
        }
    }

    /* ─────────────────  UPDATE STATUS  ─────────── */
    public void updateStatus(int id, Status status) throws SQLException {
        final String sql = "UPDATE restaurants SET status = ? WHERE id = ?";
        try (PreparedStatement ps = DbUtil.getConnection().prepareStatement(sql)) {
            ps.setString(1, status.name());
            ps.setInt   (2, id);
            ps.executeUpdate();
        }
    }

    /* ─────────────────  مبدّل ResultSet → Object  ─ */
    private Restaurant map(ResultSet rs) throws SQLException {
        return new Restaurant(
                rs.getInt("id"),
                rs.getInt("owner_id"),
                rs.getString("name"),
                Status.valueOf(rs.getString("status")),
                rs.getString("description")
        );
    }
}
