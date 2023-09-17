package com.example.whatsopwebandorid;

import com.example.whatsopwebandorid.api.WebClasses.resMessage;
import com.example.whatsopwebandorid.api.WebClasses.resSendMessage;

import java.util.ArrayList;
import java.util.List;

public class Message {
    private final String content;
    private final String time;
    private final String senderUsername;

    public Message(String content, String time, String senderUsername) {
        this.content = content;
        this.time = time;
        this.senderUsername = senderUsername;
    }

    public Message(resMessage message) {
        this.content = message.getContent();
        this.time = Tools.extractTimeFormat(message.getCreated());
        this.senderUsername = message.getSenderUsername();
    }

    public static List<Message> resMessagesToMessages(List<resMessage> resMessageList) {
        ArrayList<Message> messages = new ArrayList<>();
        for (resMessage rm : resMessageList) {
            Message m = new Message(rm);
            messages.add(m);
        }
        return  messages;
    }

    public String getContent() { return this.content; }

    public String getTime() { return this.time; }

    public String getSenderUsername() {
        return senderUsername;
    }
}
