package com.example.whatsopwebandorid.api.WebClasses;

public class resMessage {
    private int id;
    private String created;
    private SenderObject sender;
    private String content;

    public resMessage(int id, String created, SenderObject sender, String content) {
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

    public String getSenderUsername() {
        return sender.getUsername();
    }

    public String getContent() {
        return content;
    }
}

class SenderObject {
    private String username;

    SenderObject(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }
}

