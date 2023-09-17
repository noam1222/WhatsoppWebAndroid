package com.example.whatsopwebandorid.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsopwebandorid.R;
import com.example.whatsopwebandorid.Tools;
import com.example.whatsopwebandorid.User;
import com.example.whatsopwebandorid.viewModels.MessagesViewModel;
import com.example.whatsopwebandorid.viewModels.UsersViewModel;
import com.example.whatsopwebandorid.adapters.ChatsListAdapter;
import com.example.whatsopwebandorid.api.ChatsApi;
import com.example.whatsopwebandorid.api.UsersApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class ChatsActivity extends AppCompatActivity {

    private UsersViewModel viewModel;

    ImageView currentProfilePictureIv;
    TextView currentDisplayNameTv;
    ListView listView;
    ChatsListAdapter adapter;

    FloatingActionButton addContactBtn;

    Button logoutBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        //get logged user details
        currentProfilePictureIv = findViewById(R.id.currentProfilePictureIv);
        currentDisplayNameTv = findViewById(R.id.currentDisplayNameTv);
        SharedPreferences loggedUser =
                getApplicationContext().getSharedPreferences("loggedUser",
                        Context.MODE_PRIVATE);
        if (getIntent().getExtras() != null) {
            // first log in
            String username = getIntent().getExtras().getString("username");
            String token = getIntent().getExtras().getString("token");
            String firbaseToken = getIntent().getExtras().getString("firebaseToken");
            viewModel = UsersViewModel.getInstance(getApplicationContext(), token);
            SharedPreferences.Editor editor = loggedUser.edit();
            editor.putString("username", username);
            editor.putString("token", token);
            editor.putString("firebaseToken", firbaseToken);
            editor.apply();

            final SharedPreferences.Editor editor1 = loggedUser.edit();
            CompletableFuture<User> future = viewModel.getLoggedUser(username)
                    .thenApply(user -> user)
                    .exceptionally(e -> {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                        editor1.apply();
                        return null;
                    });
            future.thenAccept(user -> {
                if (user != null) {
                    currentProfilePictureIv.setImageBitmap(Tools.Base64ToBitmap(user.getProfilePic()));
                    currentDisplayNameTv.setText(user.getDisplayName());
                    editor1.putString("profilePic", user.getProfilePic());
                    editor1.putString("displayName", user.getDisplayName());
                    editor1.apply();
                }
            });
        } else {
            // reconnecting
            viewModel = UsersViewModel.getInstance(getApplicationContext(),
                    loggedUser.getString("token", null));
            String profilePic =  loggedUser.getString("profilePic", null);
            String displayName =  loggedUser.getString("displayName", null);
            if (profilePic != null && displayName != null) {
                currentProfilePictureIv.setImageBitmap(Tools.Base64ToBitmap(profilePic));
                currentDisplayNameTv.setText(displayName);
            }
        }

        listView = findViewById(R.id.chats_list_view);

        //TODO: change for real messages
        listView.setClickable(true);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            User contact = viewModel.get().getValue().get(i);
            Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
            intent.putExtra("token", loggedUser.getString("token", null));
            intent.putExtra("chatId", contact.getId());
            intent.putExtra("loggedUsername", loggedUser.getString("username", null));
            startActivity(intent);
        });
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            viewModel.delete(viewModel.get().getValue().get(i));
            return true;
        });

        viewModel.get().observe(this, newUsers -> {
            adapter = new ChatsListAdapter(getApplicationContext(),
                    (ArrayList<User>) viewModel.get().getValue());
            listView.setAdapter(adapter);
        });


        addContactBtn = findViewById(R.id.btnAdd);
        addContactBtn.setOnClickListener(view -> {
            Intent newContactIntent = new Intent(this, AddContactActivity.class);
            newContactIntent.putExtra("token", loggedUser.getString("token", null));
            startActivity(newContactIntent);
        });

        logoutBtn = findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(view -> {
            clearAllData();
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        clearAllData();
    }

    private void clearAllData() {
        //TODO: delete all messages from local db
        viewModel.clearDB();
        SharedPreferences sharedPreferences = getApplicationContext()
                .getSharedPreferences("loggedUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}