package com.example.sabuskitchen.Models;


public class Tables {
    String tableno,status,capacity;

    public Tables()
    {

    }
    @Override
    public String toString() {
        return "Tables{" +
                "tableno='" + tableno + '\'' +
                ", status='" + status + '\'' +
                ", capacity='" + capacity + '\'' +
                '}';
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getTableno() {
        return tableno;
    }

    public void setTableno(String tableno) {
        this.tableno = tableno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
