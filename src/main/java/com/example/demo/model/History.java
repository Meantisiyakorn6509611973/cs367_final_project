package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String itemName;
    private String boxName;
    private LocalDateTime purchasedAt;

    // Constructors
    public History() {}

    public History(String itemName, String boxName, LocalDateTime purchasedAt) {
        this.itemName = itemName;
        this.boxName = boxName;
        this.purchasedAt = purchasedAt;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getBoxName() {
        return boxName;
    }

    public LocalDateTime getPurchasedAt() {
        return purchasedAt;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public void setPurchasedAt(LocalDateTime purchasedAt) {
        this.purchasedAt = purchasedAt;
    }
}
