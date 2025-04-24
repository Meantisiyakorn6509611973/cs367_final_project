package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Item {
    @Id
    @GeneratedValue
    private Long item_id;

    private String item_name;
    private int item_price;
    private int item_amount;

    public Item() {
    }

    public Item(String item_name, int item_price) {
        this.item_name = item_name;
        this.item_price = item_price;
    }

    public Long getItemId() {
        return item_id;
    }

    public void setItemId(Long item_id) {
        this.item_id = item_id;
    }

    public String getName() {
        return item_name;
    }

    public void setName(String item_name) {
        this.item_name = item_name;
    }

    public int getItemPrice() {
        return item_price;
    }

    public void setItemPrice(int item_price) {
        this.item_price = item_price;
    }

    public int getItem_amount() {
        return item_amount;
    }

    public void setItem_amount(int item_amount) {
        this.item_amount = item_amount;
    }
}
