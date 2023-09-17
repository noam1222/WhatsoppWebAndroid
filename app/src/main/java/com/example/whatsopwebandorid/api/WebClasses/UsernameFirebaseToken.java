package com.example.whatsopwebandorid.api.WebClasses;

public class UsernameFirebaseToken {
    private String username;
    private String token;

    public UsernameFirebaseToken(String username, String token) {
        this.username = username;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }
}
