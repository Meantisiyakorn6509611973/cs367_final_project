package com.example.demo.model;

import jakarta.persistence.*;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Box {
    @Id
    @GeneratedValue
    private Long id;

    private String boxName;
    private int boxPrice;

    @OneToMany(mappedBy = "box", cascade = CascadeType.ALL)
    @JsonManagedReference
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

    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
}
