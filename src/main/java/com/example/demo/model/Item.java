// Entity class representing an individual snack item that belongs to a box.

package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Unique identifier for the item
    private Long itemId;

    // Name of the snack item (e.g., "Matcha Roll Cake")
    private String itemName;

    // Price of the individual item
    private int itemPrice;

    // Current stock amount of the item
    private int itemAmount;

    @ManyToOne
    @JoinColumn(name = "box_id")
    @JsonIgnoreProperties({"items"})  // prevent infinite loop but allow boxName
    private Box box;

    // Default constructor
    public Item() {}

    // Constructor with parameters
    public Item(String itemName, int itemPrice, int itemAmount) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemAmount = itemAmount;
    }

    // Getters and setters
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public Box getBox() {
        return box;
    }

    public void setBox(Box box) {
        this.box = box;
    }
}
