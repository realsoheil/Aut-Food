package com.autfood.server.controller;

import com.autfood.server.dao.FoodItemDao;
import com.autfood.server.model.FoodItem;

import java.sql.SQLException;
import java.util.List;

public class FoodItemService {
    private final FoodItemDao dao = new FoodItemDao();

    public FoodItem add(int restId,String name,double price,double discount,
                        String imgPath) throws SQLException {
        return dao.save(new FoodItem(null, restId, name, price, discount,
                FoodItem.Status.AVAILABLE, imgPath));
    }
    public List<FoodItem> listByRestaurant(int restId) throws SQLException {
        return dao.listByRestaurant(restId);
    }
    public FoodItem get(int id) throws SQLException { return dao.findById(id); }
    public List<FoodItem> search(String term) throws SQLException { return dao.search(term); }
}
