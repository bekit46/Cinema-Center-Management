package com.group15;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The DataPass class serves as a data container for various elements
 * in the application such as movies, selected seats, schedules, and pricing details.
 * It provides methods to manage and retrieve this information.
 */
public class DataPass {

    /**
     * Default constructor initializing all member variables.
     */
    public DataPass() {
        movie = null;
        cartContent = null;
        cartContent2 = null;
        selectedSeats = new ArrayList<>();
        schedule=null;
        totalPrice = 0;
        absTotal = 0;
        discountedTicketCount = 0;
        nameSurname=null;
        tax=0;
        products=new HashMap<>();
        discountApplied=false;
    };

    public Movie movie;
//////////////////////////////////////////////////////////////////////////////
   public boolean  discountApplied;
    public String cartContent;
    public String cartContent2;

//////////////////////////////////////////////////////////////////////////////
    private ArrayList<String> selectedSeats;

    /**
     * Sets the list of selected seats.
     *
     * @param data An ArrayList containing seat identifiers.
     */
    public void setSelectedSeats(ArrayList<String> data){
        selectedSeats = data;
    }
    /**
     * Clears the list of selected seats.
     */
    public void clearSelectedSeats(){
        selectedSeats.clear();
    }
    /**
     * Adds a seat to the selected seats list.
     *
     * @param seat The seat identifier to add.
     */
    public void addSeat(String seat){
        if(selectedSeats==null){
            selectedSeats = new ArrayList<>();
        }
        selectedSeats.add(seat);
    }
    /**
     * Removes a seat from the selected seats list.
     *
     * @param seat The seat identifier to remove.
     */
    public void removeSeat(String seat){
        selectedSeats.remove(seat);
    }
    /**
     * Checks if the selected seats list is empty.
     *
     * @return True if the list is empty, false otherwise.
     */
    public boolean seatIsEmpty(){
        if(selectedSeats.isEmpty())
            return true;
        return false;
    }
    /**
     * Retrieves the number of selected seats.
     *
     * @return The size of the selected seats list.
     */
    public int seatSize(){
        return selectedSeats.size();
    }
    public String getSeat(int i){
        return selectedSeats.get(i);
    }
    /**
     * Retrieves all selected seats as a single concatenated string.
     *
     * @return A string representation of all selected seats.
     */
    public String getSeatsAsString(){
        String ans = String.join("",selectedSeats);
        return ans;
    }

//////////////////////////////////////////////////////////////////////////////
    public Schedule schedule;
    public LocalDate selectedDate;
//////////////////////////////////////////////////////////////////////////////
    private double totalPrice;
    public double absTotal;
    private int discountedTicketCount;
    private String nameSurname;
    public double tax;

    /**
     * Sets the total price.
     *
     * @param total The total price to set.
     */
    public void setTotalPrice(double total){
        totalPrice = total;
    }
    /**
     * Retrieves the total price.
     *
     * @return The total price.
     */
    public double getTotalPrice(){
        return totalPrice;
    }

    /**
     * Sets the count of discounted tickets.
     *
     * @param i The count to set.
     */
    public void setDiscountedTicketCount(int i){
        discountedTicketCount=i;
    }
    /**
     * Retrieves the count of discounted tickets.
     *
     * @return The count of discounted tickets.
     */
    public int getDiscountedTicketCount(){
        return discountedTicketCount;
    }
    /**
     * Sets the user's name and surname.
     *
     * @param nameSurname The name and surname to set.
     */
    public void setNameSurname(String nameSurname){
        this.nameSurname = nameSurname;
    }
    /**
     * Retrieves the user's name and surname.
     *
     * @return The name and surname.
     */
    public String getNameSurname(){
        return nameSurname;
    }
    /**
     * A map of products and their quantities.
     */
    public Map<Product,Integer> products;
}
