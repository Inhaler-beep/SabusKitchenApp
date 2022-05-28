package com.example.sabuskitchen.Models;

public class Competed {

    String datentime,orderid,paymentid,dishes,totalamount;

    public Competed()
    {

    }
    @Override
    public String toString() {
        return "Users{" +
                "dishes='" + dishes + '\'' +
                ", paymentid='" + paymentid + '\'' +
                ", totalamount='" + totalamount + '\'' +
                ", datentime='" + datentime + '\'' +
                ", orderid='" + orderid + '\'' +
                '}';
    }

    public String getDatentime() {
        return datentime;
    }

    public void setDatentime(String datentime) {
        this.datentime = datentime;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(String paymentid) {
        this.paymentid = paymentid;
    }

    public String getDishes() {
        return dishes;
    }

    public void setDishes(String dishes) {
        this.dishes = dishes;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }
}
