package com.autfood.server.dao;

import com.autfood.server.model.FoodItem;
import com.autfood.server.model.FoodItem.Status;
import com.autfood.server.util.DbUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodItemDao {

    public FoodItem save(FoodItem f) throws SQLException {
        String sql = """
      INSERT INTO food_items(restaurant_id,name,price,discount,status,image_path)
      VALUES (?,?,?,?,?,?)
    """;
        try (PreparedStatement ps = DbUtil.getConnection()
                .prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt   (1, f.getRestaurantId());
            ps.setString(2, f.getName());
            ps.setDouble(3, f.getPrice());
            ps.setDouble(4, f.getDiscount());
            ps.setString(5, f.getStatus().name());
            ps.setString(6, f.getImagePath());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) { if (rs.next()) f.setId(rs.getInt(1)); }
        }
        return f;
    }

    public List<FoodItem> listByRestaurant(int rid) throws SQLException {
        String q = "SELECT * FROM food_items WHERE restaurant_id=? AND status='AVAILABLE'";
        try (PreparedStatement ps = DbUtil.getConnection().prepareStatement(q)) {
            ps.setInt(1, rid);
            try (ResultSet rs = ps.executeQuery()) {
                List<FoodItem> list=new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    public FoodItem findById(int id) throws SQLException {
        String q = "SELECT * FROM food_items WHERE id=?";
        try (PreparedStatement ps = DbUtil.getConnection().prepareStatement(q)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) { return rs.next()? map(rs):null; }
        }
    }

    public List<FoodItem> search(String term) throws SQLException {
        String q = "SELECT * FROM food_items WHERE status='AVAILABLE' AND name LIKE ?";
        try (PreparedStatement ps = DbUtil.getConnection().prepareStatement(q)) {
            ps.setString(1, "%"+term+"%");
            try (ResultSet rs = ps.executeQuery()) {
                List<FoodItem> list=new ArrayList<>();
                while (rs.next()) list.add(map(rs));
                return list;
            }
        }
    }

    private FoodItem map(ResultSet rs)throws SQLException{
        return new FoodItem(
                rs.getInt("id"),
                rs.getInt("restaurant_id"),
                rs.getString("name"),
                rs.getDouble("price"),
                rs.getDouble("discount"),
                Status.valueOf(rs.getString("status")),
                rs.getString("image_path"));
    }
}
