package com.example.sabuskitchen.Models;

public class Payments
{

    String name;
    String date;
    String mode;

    public Payments() {
    }

    @Override
    public String toString() {
        return "Payments{" +
                "name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", mode='" + mode + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
