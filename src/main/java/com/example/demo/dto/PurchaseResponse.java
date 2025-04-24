package com.example.demo.dto;

import com.example.demo.model.Item;
import java.util.List;

public class PurchaseResponse {
    private String message;
    private List<Item> items;

    public PurchaseResponse(String message, List<Item> items) {
        this.message = message;
        this.items = items;
    }

    public String getMessage() {
        return message;
    }

    public List<Item> getItems() {
        return items;
    }
}
