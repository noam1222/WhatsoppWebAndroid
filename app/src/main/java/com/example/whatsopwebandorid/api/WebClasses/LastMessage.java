package com.example.whatsopwebandorid.api.WebClasses;

public class LastMessage {
    private int id;
    private String created;
    private String content;

    public LastMessage(int id, String created, String content) {
        this.id = id;
        this.created = created;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public String getCreated() {
        return created;
    }

    public String getContent() {
        return content;
    }
}