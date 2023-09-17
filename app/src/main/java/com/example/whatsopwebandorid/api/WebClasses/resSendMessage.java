package com.example.whatsopwebandorid.api.WebClasses;

public class resSendMessage {

    private int id;
    private String created;
    private resUser sender;
    private String content;

    public resSendMessage(int id, String created, resUser sender, String content) {
        this.id = id;
        this.created = created;
        this.sender = sender;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public resUser getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }
}
