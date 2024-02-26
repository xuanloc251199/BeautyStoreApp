package com.example.beautystoreapp.api.response;

public class CartItemCountResponse {
    private boolean success;
    private int item_count;

    public CartItemCountResponse() {
    }

    public CartItemCountResponse(boolean success, int item_count) {
        this.success = success;
        this.item_count = item_count;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getItem_count() {
        return item_count;
    }

    public void setItem_count(int item_count) {
        this.item_count = item_count;
    }
}
