package com.example.whatsopwebandorid.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.whatsopwebandorid.R;
import com.example.whatsopwebandorid.Tools;
import com.example.whatsopwebandorid.api.UsersApi;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.concurrent.CompletableFuture;

public class LoginActivity extends AppCompatActivity {

    private EditText userInput;
    private EditText passwordInput;
    private Button loginBtn;
    private TextView clickHere;
    private FloatingActionButton settingBtn;

    UsersApi usersApi;

    // Helper method to check if the password contains a number
    private boolean hasNumber(String password) {
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) {
                return true;
            }
        }
        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get (or create) logged user details
        SharedPreferences loggedUser = getApplicationContext()
                .getSharedPreferences("loggedUser", Context.MODE_PRIVATE);
        if (loggedUser.getString("token", null) != null) {
            Intent intent = new Intent(this ,ChatsActivity.class);
            startActivity(intent);
        }

        // create server connection string if needed
        SharedPreferences connectionString = getApplicationContext()
                .getSharedPreferences("connectionString", Context.MODE_PRIVATE);
        if (connectionString.getString("connectionString", null) == null) {
            SharedPreferences.Editor editor = connectionString.edit();
            editor.putString("connectionString", Tools.defaultApiConnectString);
            editor.apply();
        }

        setContentView(R.layout.activity_login);

        userInput = findViewById(R.id.userInput);
        passwordInput = findViewById(R.id.passwordInput);
        loginBtn = findViewById(R.id.btnUpdate);
        clickHere = findViewById(R.id.clickHere);
        settingBtn = findViewById(R.id.btnSetting);


        loginBtn.setOnClickListener( view -> {
                String username = userInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                //I need to check as well if the username is already taken
                if (username.length() < 4) {
                    Toast.makeText(LoginActivity.this, "Invalid username", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8 || !hasNumber(password)) {
                    Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    return;
                }

                //TODO: check login with server
                // if bad login (why - server or bad details). else:
                usersApi = new UsersApi(getApplicationContext());
                CompletableFuture<String> future = usersApi.createToken(username, password)
                        .thenApply(token -> token)
                        .exceptionally(error -> {
                            Toast.makeText(getApplicationContext(), error.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            return null;
                        });
                future.thenAccept(token -> {
                    if (token != null) {
                        Intent chatsIntent = new Intent(this, ChatsActivity.class);
                        chatsIntent.putExtra("username", username);
                        chatsIntent.putExtra("token", token);
                        //get firebase token
                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                String firebaseToken = task.getResult().getToken();
                                new Thread(() -> {
                                    usersApi.sendFirebaseToken(username, firebaseToken);
                                }).start();
                            } else {
                                Toast.makeText(getApplicationContext(), "Error: real time data",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                        startActivity(chatsIntent);
                    }
                });
        });

        clickHere.setOnClickListener(view -> {
            // go to register screen
            Intent registerIntent = new Intent(this, RegisterActivity.class);
            startActivity(registerIntent);
        });

        settingBtn.setOnClickListener(view -> {
            //go to setting screen
            Intent settingIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingIntent);
        });

        requestPermission();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(LoginActivity.this,
                    Manifest.permission.POST_NOTIFICATIONS)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Permission Required");
                builder.setMessage("Please allow notification for this app");
                builder.setPositiveButton("ok", (dialog, which) ->
                        ActivityCompat.requestPermissions(LoginActivity.this,
                                new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100));
                builder.setNegativeButton("Cancel", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
            else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }
    }
}
