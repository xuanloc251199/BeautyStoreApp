package com.example.beautystoreapp.model;

public class UserDetails {
    private String name;
    private String address;
    private String phone_number;
    private String avatar;

    public UserDetails() {
    }

    public UserDetails(String name, String address, String phone_number, String avatar) {
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
