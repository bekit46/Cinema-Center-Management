package com.group15;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DataPass {

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


    public void setSelectedSeats(ArrayList<String> data){
        selectedSeats = data;
    }
    public void clearSelectedSeats(){
        selectedSeats.clear();
    }
    public void addSeat(String seat){
        if(selectedSeats==null){
            selectedSeats = new ArrayList<>();
        }
        selectedSeats.add(seat);
    }
    public void removeSeat(String seat){
        selectedSeats.remove(seat);
    }
    public boolean seatIsEmpty(){
        if(selectedSeats.isEmpty())
            return true;
        return false;
    }
    public int seatSize(){
        return selectedSeats.size();
    }
    public String getSeat(int i){
        return selectedSeats.get(i);
    }
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

    public void setTotalPrice(double total){
        totalPrice = total;
    }
    public double getTotalPrice(){
        return totalPrice;
    }
    
    public void setDiscountedTicketCount(int i){
        discountedTicketCount=i;
    }
    public int getDiscountedTicketCount(){
        return discountedTicketCount;
    }
    public void setNameSurname(String nameSurname){
        this.nameSurname = nameSurname;
    }
    public String getNameSurname(){
        return nameSurname;
    }
    public Map<Product,Integer> products;
}
