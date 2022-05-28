package com.example.sabuskitchen.Models;

public class AddMenuModel {
    String dishname,price;
    public AddMenuModel()
    {

    }
    @Override
    public String toString() {
        return "Users{" +
                "dishname='" + dishname + '\'' +
                ", price='" + price + '\'' +

                '}';
    }

    public String getDishname() {
        return dishname;
    }

    public void setDishname(String dishname) {
        this.dishname = dishname;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
