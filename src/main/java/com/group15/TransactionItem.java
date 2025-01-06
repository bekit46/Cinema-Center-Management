package com.group15;

// TransactionItem class
public class TransactionItem {
    private int transactionItemsId;
    private int fkTransactionId;
    private Integer fkProductId; // Nullable
    private int productQuantity;
    private int totalPrice;

    // Parameterized constructor
    public TransactionItem(int transactionItemsId, int fkTransactionId, Integer fkProductId, int productQuantity, int totalPrice) {
        this.transactionItemsId = transactionItemsId;
        this.fkTransactionId = fkTransactionId;
        this.fkProductId = fkProductId;
        this.productQuantity = productQuantity;
        this.totalPrice = totalPrice;
    }

    // Getters and setters (one-liners)
    public int getTransactionItemsId() { return transactionItemsId; }
    public void setTransactionItemsId(int transactionItemsId) { this.transactionItemsId = transactionItemsId; }
    public int getFkTransactionId() { return fkTransactionId; }
    public void setFkTransactionId(int fkTransactionId) { this.fkTransactionId = fkTransactionId; }
    public Integer getFkProductId() { return fkProductId; }
    public void setFkProductId(Integer fkProductId) { this.fkProductId = fkProductId; }
    public int getProductQuantity() { return productQuantity; }
    public void setProductQuantity(int productQuantity) { this.productQuantity = productQuantity; }
    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
}