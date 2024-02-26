package com.example.beautystoreapp.model;

public class Product {
    private int id;
    private String barcode;
    private String sku;
    private String name;
    private double price;
    private int discount_percentage;
    private String discount_from_date;
    private String discount_to_date;
    private String featured_image;
    private int inventory_qty;
    private int category_id;
    private int brand_id;
    private String created_date;
    private String description;
    private double star; // Sử dụng Double để có thể nhận giá trị null
    private Integer featured; // Sử dụng Integer để có thể nhận giá trị null

    // Getters và Setters


    public Product() {
    }

    public Product(int id, String barcode, String sku, String name, double price, int discount_percentage, String discount_from_date, String discount_to_date, String featured_image, int inventory_qty, int category_id, int brand_id, String created_date, String description, Double star, Integer featured) {
        this.id = id;
        this.barcode = barcode;
        this.sku = sku;
        this.name = name;
        this.price = price;
        this.discount_percentage = discount_percentage;
        this.discount_from_date = discount_from_date;
        this.discount_to_date = discount_to_date;
        this.featured_image = featured_image;
        this.inventory_qty = inventory_qty;
        this.category_id = category_id;
        this.brand_id = brand_id;
        this.created_date = created_date;
        this.description = description;
        this.star = star;
        this.featured = featured;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDiscount_percentage() {
        return discount_percentage;
    }

    public void setDiscount_percentage(int discount_percentage) {
        this.discount_percentage = discount_percentage;
    }

    public String getDiscount_from_date() {
        return discount_from_date;
    }

    public void setDiscount_from_date(String discount_from_date) {
        this.discount_from_date = discount_from_date;
    }

    public String getDiscount_to_date() {
        return discount_to_date;
    }

    public void setDiscount_to_date(String discount_to_date) {
        this.discount_to_date = discount_to_date;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public void setFeatured_image(String featured_image) {
        this.featured_image = featured_image;
    }

    public int getInventory_qty() {
        return inventory_qty;
    }

    public void setInventory_qty(int inventory_qty) {
        this.inventory_qty = inventory_qty;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getStar() {
        return star;
    }

    public void setStar(Double star) {
        this.star = star;
    }

    public Integer getFeatured() {
        return featured;
    }

    public void setFeatured(Integer featured) {
        this.featured = featured;
    }
}

