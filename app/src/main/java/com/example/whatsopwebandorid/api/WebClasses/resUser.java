package com.example.whatsopwebandorid.api.WebClasses;

public class resUser {
    private String username;
    private String displayName;
    private String profilePic;

    public resUser(String username, String displayName, String profilePic) {
        this.username = username;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getPhoto() {
        return profilePic;
    }

}
