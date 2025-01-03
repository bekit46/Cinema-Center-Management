package com.group15;

public class Revenue {
    private int taxFree;
    private int totalTax;
    private int total;

    // No-argument constructor (required)
    public Revenue() {}

    // Parameterized constructor
    public Revenue(int taxFree, int totalTax, int total) {
        this.taxFree = taxFree;
        this.totalTax = totalTax;
        this.total = total;
    }

    // Getters Setters
    public int getTaxFree() { return taxFree;}
    public void setTaxFree(int taxFree) {this.taxFree = taxFree;}
    public int getTotalTax() { return totalTax;}
    public void setTotalTax(int totalTax) {this.totalTax = totalTax;}
    public int getTotal() { return total;}
    public void setTotal(int total) {this.total = total;}
}
