package com.group15;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Transaction {
    private int transactionId;
    private String customerName;
    private String customerSurname;
    private Date date;
    private int scheduleId;
    private String title;
    private int seatQuantity;
    private int totalPrice;
    private boolean discountApplied;

    // Parameterized constructor
    public Transaction(int transactionId, String customerName, String customerSurname, Date date, int scheduleId, String title, int seatQuantity, int totalPrice, boolean discountApplied) {
        this.transactionId = transactionId;
        this.customerName = customerName;
        this.customerSurname = customerSurname;
        this.date = date;
        this.scheduleId = scheduleId;
        this.title = title;
        this.seatQuantity = seatQuantity;
        this.totalPrice = totalPrice;
        this.discountApplied = discountApplied;
    }

    // Getters and setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerSurname() { return customerSurname; }
    public void setCustomerSurname(String customerSurname) { this.customerSurname = customerSurname; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public int getSeatQuantity() { return seatQuantity; }
    public void setSeatQuantity(int seatQuantity) { this.seatQuantity = seatQuantity; }

    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }

    public boolean isDiscountApplied() { return discountApplied; }
    public void setDiscountApplied(boolean discountApplied) { this.discountApplied = discountApplied; }

    public String getDetails() {
        Facade facade = new Facade();
        return facade.getTransactionDetails(this.transactionId);
    }

    public String getSeats(){
        Facade facade = new Facade();
        return facade.getTransactionSeatsById(this.transactionId);
    }

    public int getTaxById(){
        Facade facade = new Facade();
        return facade.getTaxById(this.transactionId);
    }
}