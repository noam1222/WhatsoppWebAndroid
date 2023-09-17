package com.example.whatsopwebandorid.api.WebClasses;

public class resAddChat {

    private int id;

    private resUser newChat;

    public resAddChat(int id, String username, String displayName, String profilePic) {
        this.id = id;
        this.newChat = new resUser(username, displayName, profilePic);
    }

    public int getId() {
        return id;
    }

    public resUser getNewChat() {
        return newChat;
    }
}
