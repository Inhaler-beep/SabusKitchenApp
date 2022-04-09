package com.example.sabuskitchen.Models;



public class Menu {

    String dishname,price,category;

    public Menu(){

    }
    @Override
    public String toString() {
        return "Users{" +
                "dishname='" + dishname + '\'' +
                ", price='" + price + '\'' +
                ", category='" + category + '\'' +
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
