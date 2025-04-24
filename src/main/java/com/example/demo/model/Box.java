package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Box {
    @Id
    @GeneratedValue
    private Long id;

    private String boxName;
    private int boxPrice;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "box_id")
    private List<Item> items;

    public Box() {}

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public int getBoxPrice() {
        return boxPrice;
    }

    public void setBoxPrice(int boxPrice) {
        this.boxPrice = boxPrice;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
