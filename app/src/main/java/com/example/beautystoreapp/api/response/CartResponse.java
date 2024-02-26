package com.example.beautystoreapp.api.response;

import com.example.beautystoreapp.model.CartItem;

import java.util.List;

public class CartResponse {
    private boolean status;
    private List<CartItem> data;

    public CartResponse() {
    }

    public CartResponse(boolean status, List<CartItem> data) {
        this.status = status;
        this.data = data;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<CartItem> getData() {
        return data;
    }

    public void setData(List<CartItem> data) {
        this.data = data;
    }
}
