package com.autfood.server.controller;

import com.autfood.server.dao.RestaurantDao;
import com.autfood.server.model.Restaurant;
import com.autfood.server.model.Restaurant.Status;
import java.sql.SQLException;
import java.util.List;

public class RestaurantService {
    private final RestaurantDao dao = new RestaurantDao();

    public Restaurant create(int ownerId,String name,String desc) throws SQLException {
        Restaurant r = new Restaurant(null, ownerId, name, Status.PENDING, desc);
        return dao.save(r);
    }
    public Restaurant get(int id) throws SQLException { return dao.findById(id); }
    public List<Restaurant> listApproved() throws SQLException { return dao.listApproved(); }
    public void approve(int id) throws SQLException { dao.updateStatus(id, Status.APPROVED); }
}
