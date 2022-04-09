package com.example.sabuskitchen.Models;

public class Users {
    String name;
    String phone;

    public Users()
    {
    }

    @Override
    public String toString() {
        return "Users{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
