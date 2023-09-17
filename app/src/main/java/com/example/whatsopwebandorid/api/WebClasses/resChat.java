package com.example.whatsopwebandorid.api.WebClasses;

public class resChat {
    private int id;
    private resUser user;
    private LastMessage lastMessage;

    public resChat(int id, resUser user, LastMessage lastMessage) {
        this.id = id;
        this.user = user;
        this.lastMessage = lastMessage;
    }

    public int getId() {
        return id;
    }

    public resUser getUser() {
        return user;
    }

    public LastMessage getLastMessage() {
        return lastMessage;
    }
}
