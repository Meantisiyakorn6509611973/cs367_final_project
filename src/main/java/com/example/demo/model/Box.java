package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Box {
    @Id
    @GeneratedValue
    private Long id;

    private String box_name;
    private int box_price;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "box_id")
    private List<Item> items;

    public Box() {}

    // Getter and setter for box_name
    public String getBoxName() {
        return box_name;
    }

    public void setBoxName(String box_name) {
        this.box_name = box_name;
    }

    public int getBoxPrice() {
        return box_price;
    }
    
    public void setBoxPrice(int box_price) {
        this.box_price = box_price;
    }

    // Getter and setter for items
    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
