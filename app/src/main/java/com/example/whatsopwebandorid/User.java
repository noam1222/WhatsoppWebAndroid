package com.example.whatsopwebandorid;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.whatsopwebandorid.api.WebClasses.resAddChat;
import com.example.whatsopwebandorid.api.WebClasses.resChat;
import com.example.whatsopwebandorid.room.MessageListConverter;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @PrimaryKey
    private int id;
    private final String displayName;
    private final String profilePic;
    private String lastMassage;
    private String lastMassageSendingTime;

    public User(int id, String displayName, String profilePic, String lastMassage, String lastMassageSendingTime) {
        this.id = id;
        this.displayName = displayName;
        this.profilePic = profilePic;
        this.lastMassage = lastMassage;
        this.lastMassageSendingTime = lastMassageSendingTime;
    }

    public User(resChat chat) {
        this.id = chat.getId();
        this.displayName = chat.getUser().getDisplayName();
        this.profilePic = Tools.extractImageViewBase64(chat.getUser().getPhoto());
        if (chat.getLastMessage() != null) {
            this.lastMassage = chat.getLastMessage().getContent();
            this.lastMassageSendingTime = Tools.extractTimeFormat(chat.getLastMessage().getCreated());
        } else {
            this.lastMassage = "";
            this.lastMassageSendingTime = "";
        }
    }

    public static List<User> UsersFromChats(List<resChat> chats) {
        ArrayList<User> users = new ArrayList<>();
        for (resChat chat : chats) {
            User user = new User(chat);
            users.add(user);
        }
        return users;
    }

    public static User convertToUser(resAddChat chat) {
        return new User(
                new resChat(chat.getId(), chat.getNewChat(), null)
        );
    }

    public int getId() {
        return id;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getLastMassage() {
        return lastMassage;
    }

    public String getLastMassageSendingTime() {
        return lastMassageSendingTime;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLastMassage(String lastMassage) {
        this.lastMassage = lastMassage;
    }

    public void setLastMassageSendingTime(String lastMassageSendingTime) {
        this.lastMassageSendingTime = lastMassageSendingTime;
    }
}
