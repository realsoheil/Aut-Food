package com.autfood.server.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodItem {

    public enum Status { AVAILABLE, UNAVAILABLE }

    private Integer id;
    private Integer restaurantId;
    private String  name;
    private double  price;
    private double  discount;     // درصد - بین 0 و 100
    private Status  status;
    private String  imagePath;    // مسیر ذخیره‌شده روی دیسک

    public FoodItem() {}

    public FoodItem(Integer id,Integer restId,String name,double price,
                    double discount,Status status,String imgPath){
        this.id=id; this.restaurantId=restId; this.name=name;
        this.price=price; this.discount=discount; this.status=status;
        this.imagePath=imgPath;
    }
    /* Getter / Setterها */
}
