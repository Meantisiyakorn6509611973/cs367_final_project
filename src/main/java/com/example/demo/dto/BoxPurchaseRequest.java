package com.example.demo.dto;

public class BoxPurchaseRequest {
    private String boxName;
    private int quantity;

    public BoxPurchaseRequest() {}

    public BoxPurchaseRequest(String boxName, int quantity) {
        this.boxName = boxName;
        this.quantity = quantity;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
