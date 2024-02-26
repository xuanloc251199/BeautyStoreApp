package com.example.beautystoreapp.model;

public class Users {
    private int id;
    private String name;
    private String email;
    private UserDetails user_details;

    public Users(int id, String name, String email, UserDetails user_details) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.user_details = user_details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserDetails getUser_details() {
        return user_details;
    }

    public void setUser_details(UserDetails user_details) {
        this.user_details = user_details;
    }
}
