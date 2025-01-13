package com.group15;

// TransactionItem class
/**
 * Represents an item in a transaction.
 */
public class TransactionItem {
    /**
     * The unique ID of the transaction item.
     */
    private int transactionItemsId;
    /**
     * The foreign key referencing the transaction this item belongs to.
     */
    private int fkTransactionId;
    /**
     * The foreign key referencing the product ID. Nullable.
     */
    private Integer fkProductId; // Nullable

    /**
     * The quantity of the product in this transaction.
     */
    private int productQuantity;
    /**
     * The total price for the product(s) in this transaction.
     */
    private int totalPrice;

    // Parameterized constructor
    /**
     * Constructs a new TransactionItem with the specified details.
     *
     * @param transactionItemsId the unique ID of the transaction item
     * @param fkTransactionId    the foreign key referencing the transaction
     * @param fkProductId        the foreign key referencing the product ID (nullable)
     * @param productQuantity    the quantity of the product
     * @param totalPrice         the total price for the product(s)
     */
    public TransactionItem(int transactionItemsId, int fkTransactionId, Integer fkProductId, int productQuantity, int totalPrice) {
        this.transactionItemsId = transactionItemsId;
        this.fkTransactionId = fkTransactionId;
        this.fkProductId = fkProductId;
        this.productQuantity = productQuantity;
        this.totalPrice = totalPrice;
    }

    // Getters and setters (one-liners)
    /**
     * Gets the unique ID of the transaction item.
     *
     * @return the unique ID of the transaction item
     */
    public int getTransactionItemsId() { return transactionItemsId; }
    /**
     * Sets the unique ID of the transaction item.
     *
     * @param transactionItemsId the unique ID of the transaction item
     */
    public void setTransactionItemsId(int transactionItemsId) { this.transactionItemsId = transactionItemsId; }
    /**
     * Gets the foreign key referencing the transaction.
     *
     * @return the foreign key referencing the transaction
     */
    public int getFkTransactionId() { return fkTransactionId; }
    /**
     * Sets the foreign key referencing the transaction.
     *
     * @param fkTransactionId the foreign key referencing the transaction
     */
    public void setFkTransactionId(int fkTransactionId) { this.fkTransactionId = fkTransactionId; }

    /**
     * Gets the foreign key referencing the product ID.
     *
     * @return the foreign key referencing the product ID (nullable)
     */
    public Integer getFkProductId() { return fkProductId; }
    /**
     * Sets the foreign key referencing the product ID.
     *
     * @param fkProductId the foreign key referencing the product ID (nullable)
     */
    public void setFkProductId(Integer fkProductId) { this.fkProductId = fkProductId; }
    /**
     * Gets the quantity of the product in this transaction.
     *
     * @return the quantity of the product
     */
    public int getProductQuantity() { return productQuantity; }
    /**
     * Sets the quantity of the product in this transaction.
     *
     * @param productQuantity the quantity of the product
     */
    public void setProductQuantity(int productQuantity) { this.productQuantity = productQuantity; }

    /**
     * Gets the total price for the product(s) in this transaction.
     *
     * @return the total price for the product(s)
     */
    public int getTotalPrice() { return totalPrice; }
    /**
     * Sets the total price for the product(s) in this transaction.
     *
     * @param totalPrice the total price for the product(s)
     */
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
}