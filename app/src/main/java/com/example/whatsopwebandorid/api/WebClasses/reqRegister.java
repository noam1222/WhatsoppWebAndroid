package com.example.whatsopwebandorid.api.WebClasses;

public class reqRegister {
    private String username;
    private String displayName;
    private String password;
    private String profilePic;

    public reqRegister(String username, String displayName, String password,  String profilePic) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.profilePic = profilePic;
    }
}
