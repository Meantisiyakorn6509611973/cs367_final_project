// DTO class used to update or delete item quantities via PUT request.

package com.example.demo.dto;

public class ItemQuantityUpdateRequest {
    // ID of the item to update or delete
    private Long itemId;

    // Action to perform: "update" or "delete"
    private String action;

    // Amount to change the quantity by (used only for "update" action)
    private Integer deltaAmount; 


    // Getter and setter for itemId
    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }


    // Getter and setter for action
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    // Getter and setter for deltaAmount
    public Integer getDeltaAmount() {
        return deltaAmount;
    }

    public void setDeltaAmount(Integer deltaAmount) {
        this.deltaAmount = deltaAmount;
    }
}
