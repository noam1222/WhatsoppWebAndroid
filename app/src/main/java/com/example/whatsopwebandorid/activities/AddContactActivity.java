package com.example.whatsopwebandorid.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsopwebandorid.Message;
import com.example.whatsopwebandorid.R;
import com.example.whatsopwebandorid.User;
import com.example.whatsopwebandorid.viewModels.UsersViewModel;
import com.example.whatsopwebandorid.api.ChatsApi;
import com.example.whatsopwebandorid.api.WebClasses.resAddChat;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

public class AddContactActivity extends AppCompatActivity {

    private EditText contactInput;
    private Button addContactBtn;

    private UsersViewModel viewModel;

    ChatsApi chatsApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);

        contactInput = findViewById(R.id.contactInput);
        addContactBtn = findViewById(R.id.btnAddContact);


        viewModel = UsersViewModel.getInstance(AddContactActivity.this,
                getIntent().getExtras().getString("token"));

        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String contactName = contactInput.getText().toString().trim();

                if (contactName.isEmpty()) {
                    Toast.makeText(AddContactActivity.this,
                            "Please enter a contact name", Toast.LENGTH_SHORT).show();
                } else {
                    CompletableFuture<Boolean> future = viewModel.add(contactName)
                            .thenApply(results -> {
                                return results;
                            })
                            .exceptionally(e -> {
                                contactInput.setText("");
                                Toast.makeText(AddContactActivity.this,
                                        e.getMessage(), Toast.LENGTH_SHORT).show();
                                return Boolean.FALSE;
                            });
                    future.thenAccept(results -> {
                        if (results == Boolean.TRUE) {
                            Toast.makeText(AddContactActivity.this,
                                    contactName + " added as new Contact",
                                    Toast.LENGTH_SHORT).show();
                            AddContactActivity.this.finish();
                        }
                    });
                }
            }
        });
    }
}
