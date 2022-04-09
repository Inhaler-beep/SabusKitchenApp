package com.example.sabuskitchen.Models;

public class DueUsers
{

    String name;
    String phone;
    String mode;

    public DueUsers()
    {
    }

    @Override
    public String toString() {
        return "DueUsers{" +
                "name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
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
