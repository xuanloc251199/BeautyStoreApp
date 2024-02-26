package com.example.beautystoreapp.model;

public class Users {
    private String name;
    private String email;
    private String password;
    private String password_confirmationpassword;

    public Users(String name, String email, String password, String password_confirmationpassword) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.password_confirmationpassword = password_confirmationpassword;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmationpassword() {
        return password_confirmationpassword;
    }

    public void setPassword_confirmationpassword(String password_confirmationpassword) {
        this.password_confirmationpassword = password_confirmationpassword;
    }
}
