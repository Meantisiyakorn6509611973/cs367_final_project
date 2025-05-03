// Entity class representing a snack box that contains multiple items.

package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Box {
    @Id
    @GeneratedValue
    // Unique identifier for the box (auto-generated)
    private Long id;

     // Name of the box (e.g., "Matcha Lover Box")
    private String boxName;

     // Price of the box
    private int boxPrice;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL)
    // FUCK YOU FUCK YOU FUCK YOU FUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOUFUCK YOU @JsonManagedReference
    // List of items associated with this box
    private List<Item> items;

    // Default constructor
    public Box() {}

    public Box(String boxName, int boxPrice, List<Item> items) {
        this.boxName = boxName;
        this.boxPrice = boxPrice;
        this.items = items;
    }

    // Getter and setter for boxName
    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    // Getter and setter for boxPrice
    public int getBoxPrice() {
        return boxPrice;
    }

    public void setBoxPrice(int boxPrice) {
        this.boxPrice = boxPrice;
    }

    // Getter and setter for items
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    // Getter and setter for id
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
}
