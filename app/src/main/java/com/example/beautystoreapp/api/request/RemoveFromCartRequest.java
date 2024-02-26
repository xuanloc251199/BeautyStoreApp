package com.example.beautystoreapp.api.request;

public class RemoveFromCartRequest {
    private int product_id;
    private int quantity;

    public RemoveFromCartRequest(int productId, int quantity) {
        this.product_id = productId;
        this.quantity = quantity;
    }

    public int getProductId() {
        return product_id;
    }

    public void setProductId(int productId) {
        this.product_id = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

