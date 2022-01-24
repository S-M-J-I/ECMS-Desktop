package project.models;

import java.util.ArrayList;

public class Transaction {
    private int id;
    private String type;
    private String category;
    private double amount;
    private String reason;
    private String month;
    private String year;
    private String date;

    public Transaction(int id, String type, String category, double amount, String reason, String month, String year) {
        this.id = id;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.reason = reason;
        this.month = month;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmountString() {
        return Double.toString(this.amount);
    }

    public String getIdString() {
        return Integer.toString(this.id);
    }

    public String getDate() {
        return theMonth(Integer.parseInt(this.month)) + " " + this.year;
    }

    public String getCategory() {
        return category;
    }

    public String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month-1];
    }
}
