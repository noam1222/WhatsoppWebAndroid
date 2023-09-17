package com.example.whatsopwebandorid.room;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.whatsopwebandorid.Message;

import java.util.List;

@Entity
public class roomMessages {
    @PrimaryKey
    int id;

    @TypeConverters(MessageListConverter.class)
    private List<Message> messages;

    public roomMessages(int id, List<Message> messages) {
        this.id = id;
        this.messages = messages;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
