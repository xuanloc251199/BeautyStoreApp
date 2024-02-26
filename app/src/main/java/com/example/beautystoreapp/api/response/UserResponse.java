package com.example.beautystoreapp.api.response;

import com.example.beautystoreapp.model.User;

public class UserResponse {
    private String status;
    private String accessToken;
    private User user;

    public UserResponse(String status, String accessToken, User user) {
        this.status = status;
        this.accessToken = accessToken;
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
