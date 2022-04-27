package com.example.sabuskitchen;

public class Orders {

    String date,status,totalamount;

    public  Orders()
    {

    }

    @Override
    public String toString() {
        return "Users{" +
                "totalamount='" + totalamount + '\'' +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                '}';
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }
}
