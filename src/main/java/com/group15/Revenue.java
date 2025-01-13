package com.group15;

/**
 * Represents the revenue details including tax-free amount, total tax, and total revenue.
 */
public class Revenue {
    private int taxFree;
    private int totalTax;
    private int total;

    // No-argument constructor (required)
    /**
     * No-argument constructor for creating an instance of Revenue with default values.
     */
    public Revenue() {}

    // Parameterized constructor
    /**
     * Parameterized constructor for creating an instance of Revenue with specified values.
     *
     * @param taxFree  the tax-free amount of the revenue
     * @param totalTax the total tax amount of the revenue
     * @param total    the total revenue including tax-free amount and total tax
     */
    public Revenue(int taxFree, int totalTax, int total) {
        this.taxFree = taxFree;
        this.totalTax = totalTax;
        this.total = total;
    }

    // Getters Setters
    /**
     * Gets the tax-free amount of the revenue.
     *
     * @return the tax-free amount
     */
    public int getTaxFree() { return taxFree;}
    /**
     * Sets the tax-free amount of the revenue.
     *
     * @param taxFree the tax-free amount to set
     */
    public void setTaxFree(int taxFree) {this.taxFree = taxFree;}
    /**
     * Gets the total tax amount of the revenue.
     *
     * @return the total tax amount
     */
    public int getTotalTax() { return totalTax;}
    /**
     * Sets the total tax amount of the revenue.
     *
     * @param totalTax the total tax amount to set
     */
    public void setTotalTax(int totalTax) {this.totalTax = totalTax;}
    /**
     * Gets the total revenue including tax-free amount and total tax.
     *
     * @return the total revenue
     */
    public int getTotal() { return total;}
    /**
     * Sets the total revenue including tax-free amount and total tax.
     *
     * @param total the total revenue to set
     */
    public void setTotal(int total) {this.total = total;}
}
