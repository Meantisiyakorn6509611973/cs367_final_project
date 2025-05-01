// DTO class used to send a purchase result back to the client, including a message and list of items.

package com.example.demo.dto;

import com.example.demo.model.Item;
import java.util.List;

public class PurchaseResponse {

    // Message describing the result of the purchase
    private String message;

    // List of items included in the purchase
    private List<Item> items;

    // Constructor
    public PurchaseResponse(String message, List<Item> items) {
        this.message = message;
        this.items = items;
    }
    
    // Getter for message
    public String getMessage() {
        return message;
    }

    // Getter for items
    public List<Item> getItems() {
        return items;
    }
}
