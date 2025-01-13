package com.group15;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Represents a transaction in the system.
 * A transaction stores details about a customer's booking, such as the customer name,
 * transaction ID, date, schedule ID, movie title, and other relevant details.
 */
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
    /**
     * Constructs a new Transaction object with the specified details.
     *
     * @param transactionId    Unique identifier for the transaction
     * @param customerName     First name of the customer
     * @param customerSurname  Surname of the customer
     * @param date             Date of the transaction
     * @param scheduleId       ID of the schedule associated with the transaction
     * @param title            Title of the movie
     * @param seatQuantity     Number of seats booked
     * @param totalPrice       Total price of the transaction
     * @param discountApplied  Whether a discount was applied
     */
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
    /**
     * @return The unique identifier for the transaction.
     */
    public int getTransactionId() { return transactionId; }

    /**
     * @param transactionId Sets the unique identifier for the transaction.
     */
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    /**
     * @return The first name of the customer.
     */
    public String getCustomerName() { return customerName; }
    /**
     * @param customerName Sets the first name of the customer.
     */
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    /**
     * @return The surname of the customer.
     */
    public String getCustomerSurname() { return customerSurname; }
    /**
     * @param customerSurname Sets the surname of the customer.
     */
    public void setCustomerSurname(String customerSurname) { this.customerSurname = customerSurname; }
    /**
     * @return The date of the transaction.
     */
    public Date getDate() { return date; }
    /**
     * @param date Sets the date of the transaction.
     */
    public void setDate(Date date) { this.date = date; }
    /**
     * @return The ID of the schedule associated with the transaction.
     */
    public int getScheduleId() { return scheduleId; }

    /**
     * @param scheduleId Sets the ID of the schedule associated with the transaction.
     */
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    /**
     * @return The title of the movie.
     */
    public String getTitle() { return title; }
    /**
     * @param title Sets the title of the movie.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * @return The number of seats booked in the transaction.
     */
    public int getSeatQuantity() { return seatQuantity; }
    /**
     * @param seatQuantity Sets the number of seats booked in the transaction.
     */
    public void setSeatQuantity(int seatQuantity) { this.seatQuantity = seatQuantity; }
    /**
     * @return The total price of the transaction.
     */
    public int getTotalPrice() { return totalPrice; }
    /**
     * @param totalPrice Sets the total price of the transaction.
     */
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
    /**
     * @return Whether a discount was applied to the transaction.
     */
    public boolean isDiscountApplied() { return discountApplied; }
    /**
     * @param discountApplied Sets whether a discount was applied to the transaction.
     */

    public void setDiscountApplied(boolean discountApplied) { this.discountApplied = discountApplied; }
    /**
     * Retrieves detailed information about the transaction.
     *
     * @return A string containing the transaction details from the Facade.
     */
    public String getDetails() {
        Facade facade = new Facade();
        return facade.getTransactionDetails(this.transactionId);
    }

    /**
     * Retrieves seat information associated with the transaction.
     *
     * @return A string containing the seat details from the Facade.
     */
    public String getSeats(){
        Facade facade = new Facade();
        return facade.getTransactionSeatsById(this.transactionId);
    }

    /**
     * Retrieves the tax amount for the transaction.
     *
     * @return The tax amount calculated by the Facade.
     */
    public int getTaxById(){
        Facade facade = new Facade();
        return facade.getTaxById(this.transactionId);
    }
}