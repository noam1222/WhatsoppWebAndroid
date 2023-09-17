package com.example.whatsopwebandorid.repositories;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Room;

import com.example.whatsopwebandorid.Message;
import com.example.whatsopwebandorid.Tools;
import com.example.whatsopwebandorid.api.ChatsApi;
import com.example.whatsopwebandorid.api.WebClasses.resMessage;
import com.example.whatsopwebandorid.api.WebClasses.resSendMessage;
import com.example.whatsopwebandorid.room.MessagesDao;
import com.example.whatsopwebandorid.room.UserDB;
import com.example.whatsopwebandorid.room.roomMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class MessagesRepository {
    private MessagesDao messagesDao;
    private MessagesListData messagesListData;
    private ChatsApi chatsApi;

    private int chatId;
    private String token;
    private Context context;

    public MessagesRepository(Context context, int chatId, String token) {
        UserDB DB = Room.databaseBuilder(context, UserDB.class, "UserDB")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
        messagesDao = DB.MessagesDao();
        chatsApi = new ChatsApi(context);
        messagesListData = new MessagesListData();
        this.context = context;
        this.chatId = chatId;
        this.token = token;
    }

    private class MessagesListData extends MutableLiveData<List<Message>> {
        public MessagesListData () {
            super();
            setValue(new ArrayList<>());
        }

        @Override
        protected void onActive() {
            super.onActive();

            new Thread(() -> {
                //post local data
                if (messagesDao.getMessagesByChatId(chatId) != null)
                    messagesListData.postValue(messagesDao.getMessagesByChatId(chatId).getMessages());
                updateMessagesFromServer();
            }).start();
        }
    }

    public void updateMessagesFromServer() {
        //fetch remote data
        CompletableFuture<List<resMessage>> future = chatsApi.getChatMessages(token, chatId)
                .thenApply(messages -> messages)
                .exceptionally(e -> {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    return null;
                });
        future.thenAccept(messages -> {
            if (messages != null) {
                List<Message> messageList = Message.resMessagesToMessages(messages);
                messagesListData.postValue(messageList);
                messagesDao.deleteChatMessages(chatId);
                messagesDao.insertNewMessageList(new roomMessages(chatId, messageList));
            }
        });
    }

    public LiveData<List<Message>> getAll() { return messagesListData; }

    public void sendMessage(String message) {
        new Thread(() -> {
            CompletableFuture<resSendMessage> future = chatsApi.sendMessage(token, chatId, message)
                    .thenApply(response -> response)
                    .exceptionally(e -> {
                        Toast.makeText(context, "Error in sending message: " + message,
                                Toast.LENGTH_SHORT).show();
                        return null;
                    });
            future.thenAccept(newMessage -> {
                if (newMessage != null) {
                    List<Message> messages = messagesDao.getMessagesByChatId(chatId).getMessages();
                    messages.add(new Message(newMessage.getContent(),
                            Tools.extractTimeFormat(newMessage.getCreated()),
                            null));
                    messagesDao.deleteChatMessages(chatId);
                    messagesDao.insertNewMessageList(new roomMessages(chatId, messages));
                    messagesListData.postValue(messagesDao.getMessagesByChatId(chatId).getMessages());
                }
            });
        }).start();
    }
}
