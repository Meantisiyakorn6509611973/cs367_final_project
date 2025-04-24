package com.example.demo.dto;

public class ItemQuantityUpdateRequest {
    private Long itemId;
    private String action; // "update" or "delete"
    private Integer deltaAmount; // only used if action == "update"

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Integer getDeltaAmount() {
        return deltaAmount;
    }

    public void setDeltaAmount(Integer deltaAmount) {
        this.deltaAmount = deltaAmount;
    }
}
