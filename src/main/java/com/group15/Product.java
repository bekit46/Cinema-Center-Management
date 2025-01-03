package com.group15;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;


public class Product {
    private final IntegerProperty id;
    private final StringProperty productName;
    private final DoubleProperty price;
    private final IntegerProperty stockQuantity;
    private final IntegerProperty tax;

    public Product(int id, String name, double price, int stockQuantity, int tax) {
        this.id = new SimpleIntegerProperty(id);
        this.productName = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.stockQuantity = new SimpleIntegerProperty(stockQuantity);
        this.tax = new SimpleIntegerProperty(tax);
    }

    // Getters and setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getProductName() { return productName.get(); }
    public void setProductName(String name) { this.productName.set(name); }
    public StringProperty nameProperty() { return productName; }

    public double getPrice() { return price.get(); }
    public void setPrice(double price) {
        if(price >= 0)
            this.price.set(price);
    }
    public DoubleProperty priceProperty() { return price; }

    public int getStockQuantity() { return stockQuantity.get(); }
    public void setStockQuantity(int stockQuantity) {
        if(stockQuantity >= 0)
            this.stockQuantity.set(stockQuantity);
    }
    public IntegerProperty stockQuantityProperty() { return stockQuantity; }

    public int getTax() { return tax.get(); }
    public void setTax(int tax) {
        if(tax >= 0)
            this.tax.set(tax);
    }
    public IntegerProperty taxProperty() { return tax; }
}
