// DTO class representing a request to purchase a specific number of boxes by name.

package com.example.demo.dto;

public class BoxPurchaseRequest {
    // The name of the box to purchase
    private String boxName;

    // The number of boxes to purchase
    private int quantity;

    // Default constructor
    public BoxPurchaseRequest() {}

    // Constructor with parameters
    public BoxPurchaseRequest(String boxName, int quantity) {
        this.boxName = boxName;
        this.quantity = quantity;
    }

     // Getter and setter for boxName
    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    // Getter and setter for quantity
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
