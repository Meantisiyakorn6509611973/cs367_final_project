package com.example.demo.dto;

import java.util.List;

public class PurchaseResponse {
    private String message;
    private List<String> receivedItems;

    public PurchaseResponse(List<String> receivedItems) {
        this.receivedItems = receivedItems;
        this.message = "You received: " + String.join(", ", receivedItems);
    }

    public String getMessage() {
        return message;
    }

    public List<String> getReceivedItems() {
        return receivedItems;
    }
}
