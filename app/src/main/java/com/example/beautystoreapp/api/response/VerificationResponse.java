package com.example.beautystoreapp.api.response;

public class VerificationResponse {

    boolean isVerified;

    public VerificationResponse(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setVerified(boolean verified) {
        isVerified = verified;
    }
}
