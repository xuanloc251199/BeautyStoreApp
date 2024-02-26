package com.example.beautystoreapp.model;

import com.google.gson.annotations.SerializedName;

public class CartItem {
    @SerializedName("product_id")
    private int productId;
    @SerializedName("name")
    private String name;
    @SerializedName("star")
    private double star;
    @SerializedName("quantity")
    private int quantity;
    @SerializedName("price")
    private double price;
    @SerializedName("total_price")
    private double totalPrice;
    @SerializedName("featured_image")
    private String featuredImage;

    public CartItem() {
    }

    public CartItem(int productId, String name, double star, int quantity, double price, double totalPrice, String featuredImage) {
        this.productId = productId;
        this.name = name;
        this.star = star;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
        this.featuredImage = featuredImage;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getStar() {
        return star;
    }

    public void setStar(double star) {
        this.star = star;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }
}
