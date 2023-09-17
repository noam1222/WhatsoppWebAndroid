package com.example.whatsopwebandorid.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.whatsopwebandorid.Message;
import com.example.whatsopwebandorid.User;
import com.example.whatsopwebandorid.repositories.MessagesRepository;
import com.example.whatsopwebandorid.room.roomMessages;

import java.util.List;

public class MessagesViewModel {

    private static MessagesViewModel messagesViewModel;
    private MessagesRepository messagesRepository;

    private LiveData<List<Message>> messagesLiveData;

    private static int chatId;

    //implement singleton design pattern
    public static MessagesViewModel getInstance(Context context, int id, String token) {
        if (messagesViewModel == null || chatId != id) {
            chatId = id;
            messagesViewModel = new MessagesViewModel(context, chatId, token);
        }
        return messagesViewModel;
    }

    public static MessagesViewModel observe() {
        return messagesViewModel;
    }

    private MessagesViewModel(Context context, int chatId, String token) {
        messagesRepository = new MessagesRepository(context, chatId, token);
        messagesLiveData = messagesRepository.getAll();
    }

    public LiveData<List<Message>> get() {
        return messagesLiveData;
    }
    public void updateMessages() {
        new Thread(() -> {
            messagesRepository.updateMessagesFromServer();
        }).start();
    }

    public void addMessage(String message) {
        messagesRepository.sendMessage(message);
    }

    public void clear() {
        messagesViewModel = null;
    }

}
