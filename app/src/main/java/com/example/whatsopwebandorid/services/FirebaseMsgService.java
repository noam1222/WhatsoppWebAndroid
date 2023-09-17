package com.example.whatsopwebandorid.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.whatsopwebandorid.R;
import com.example.whatsopwebandorid.viewModels.MessagesViewModel;
import com.example.whatsopwebandorid.viewModels.UsersViewModel;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMsgService extends FirebaseMessagingService {
    private UsersViewModel usersViewModel;
    private MessagesViewModel messagesViewModel;

    public FirebaseMsgService() {
        super();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);
        if (message.getNotification() != null) {
            String title = message.getNotification().getTitle();
            String content = message.getNotification().getBody();
            createNotification(title, content);
            updateUsersAndMessages();
        }
    }

    private void updateUsersAndMessages() {
        usersViewModel = UsersViewModel.observe();
        messagesViewModel = MessagesViewModel.observe();
        if (usersViewModel != null)
            usersViewModel.updateChats();
        if (messagesViewModel != null)
            messagesViewModel.updateMessages();
    }


    private void createNotification(String title, String body) {
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Check if the device is running Android Oreo (API 26) or higher
        String channelId = "1";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create a notification channel
            channelId = getString(R.string.channelMessageId);
            CharSequence channelName = getString(R.string.messagesChannel);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel =
                    new NotificationChannel(channelId, channelName, importance);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);

        // Show the notification
        notificationManager.notify(Integer.parseInt(channelId), builder.build());
    }
}