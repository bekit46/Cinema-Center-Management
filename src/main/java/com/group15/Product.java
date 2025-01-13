/**
 * Represents a product in the system with attributes such as id, name, price,
 * stock quantity, and tax. Provides properties for JavaFX binding.
 */
package com.group15;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * The Product class models a product with attributes such as id, product name,
 * price, stock quantity, and tax. It also provides JavaFX properties to enable
 * data binding in JavaFX applications.
 */
public class Product {
    private final IntegerProperty id;
    private final StringProperty productName;
    private final DoubleProperty price;
    private final IntegerProperty stockQuantity;
    private final IntegerProperty tax;

    /**
     * Constructs a new Product instance with the specified attributes.
     *
     * @param id             the unique identifier of the product
     * @param name           the name of the product
     * @param price          the price of the product
     * @param stockQuantity  the quantity of the product available in stock
     * @param tax            the tax percentage applied to the product
     */
    public Product(int id, String name, double price, int stockQuantity, int tax) {
        this.id = new SimpleIntegerProperty(id);
        this.productName = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.stockQuantity = new SimpleIntegerProperty(stockQuantity);
        this.tax = new SimpleIntegerProperty(tax);
    }

    // Getters and setters
    /**
     * Gets the unique identifier of the product.
     *
     * @return the product id
     */
    public int getId() { return id.get(); }
    /**
     * Sets the unique identifier of the product.
     *
     * @param id the new product id
     */
    public void setId(int id) { this.id.set(id); }
    /**
     * Gets the property representing the product id for JavaFX binding.
     *
     * @return the id property
     */
    public IntegerProperty idProperty() { return id; }
    /**
     * Gets the name of the product.
     *
     * @return the product name
     */
    public String getProductName() { return productName.get(); }
    /**
     * Sets the name of the product.
     *
     * @param name the new product name
     */
    public void setProductName(String name) { this.productName.set(name); }
    /**
     * Gets the property representing the product name for JavaFX binding.
     *
     * @return the product name property
     */
    public StringProperty nameProperty() { return productName; }
    /**
     * Gets the price of the product.
     *
     * @return the product price
     */
    public double getPrice() { return price.get(); }
    /**
     * Sets the price of the product.
     *
     * @param price the new product price
     */
    public void setPrice(double price) {this.price.set(price);}
    /**
     * Gets the property representing the product price for JavaFX binding.
     *
     * @return the price property
     */
    public DoubleProperty priceProperty() { return price; }
    /**
     * Gets the quantity of the product available in stock.
     *
     * @return the stock quantity
     */
    public int getStockQuantity() { return stockQuantity.get(); }
    /**
     * Sets the quantity of the product available in stock.
     *
     * @param stockQuantity the new stock quantity
     */
    public void setStockQuantity(int stockQuantity) {this.stockQuantity.set(stockQuantity);}
    /**
     * Gets the property representing the stock quantity for JavaFX binding.
     *
     * @return the stock quantity property
     */
    public IntegerProperty stockQuantityProperty() { return stockQuantity; }
    /**
     * Gets the tax percentage applied to the product.
     *
     * @return the tax percentage
     */
    public int getTax() { return tax.get(); }
    /**
     * Sets the tax percentage applied to the product.
     *
     * @param tax the new tax percentage
     */
    public void setTax(int tax) {this.tax.set(tax);}
    /**
     * Gets the property representing the tax percentage for JavaFX binding.
     *
     * @return the tax property
     */
    public IntegerProperty taxProperty() { return tax; }
}
