package com.example.whatsopwebandorid.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsopwebandorid.Message;
import com.example.whatsopwebandorid.Tools;
import com.example.whatsopwebandorid.adapters.MessagesListAdapter;
import com.example.whatsopwebandorid.R;
import com.example.whatsopwebandorid.User;
import com.example.whatsopwebandorid.services.FirebaseMsgService;
import com.example.whatsopwebandorid.viewModels.MessagesViewModel;
import com.example.whatsopwebandorid.viewModels.UsersViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MessagesActivity extends AppCompatActivity {
    UsersViewModel usersViewModel;
    MessagesViewModel messagesViewModel;

    ImageView activeUserImage;
    TextView activeUsernameTV;

    EditText messageEt;
    Button sendBtn;

    ListView listView;
    MessagesListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        String token = getIntent().getExtras().getString("token");
        int chatId = getIntent().getExtras().getInt("chatId");
        String loggedUsername = getIntent().getExtras().getString("loggedUsername");

        usersViewModel = UsersViewModel.getInstance(getApplicationContext(), token);
        User activeUser = usersViewModel.get(chatId);

        //set user photo
        activeUserImage = findViewById(R.id.activeChatProfilePictureIv);
        // Decode the Base64 string to ImageView
        activeUserImage.setImageBitmap(Tools.Base64ToBitmap(activeUser.getProfilePic()));

        //set user display name
        activeUsernameTV = findViewById(R.id.activeChatDisplayNameTv);
        activeUsernameTV.setText(activeUser.getDisplayName());

        messagesViewModel = MessagesViewModel.getInstance(this, chatId, token);

        listView = findViewById(R.id.messages_list_view);

        messageEt = findViewById(R.id.messageEt);
        sendBtn = findViewById(R.id.sendBtn);
        sendBtn.setOnClickListener(view -> {
            String msg = messageEt.getText().toString();
            if (msg.length() != 0) {
                messagesViewModel.addMessage(msg);
                messageEt.setText("");
            }
        });




        messagesViewModel.get().observe(this, messages -> {
            adapter = new MessagesListAdapter(getApplicationContext(),
                    (ArrayList<Message>) messages, loggedUsername);
            listView.setAdapter(adapter);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        messagesViewModel.clear();
    }
}
