package com.example.whatsopwebandorid.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.whatsopwebandorid.R;
import com.example.whatsopwebandorid.api.UsersApi;
import com.example.whatsopwebandorid.api.WebClasses.resUser;
import java.io.ByteArrayOutputStream;
import java.util.concurrent.CompletableFuture;

public class RegisterActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button selectImageBtn;
    private Button registerBtn;

    private EditText usernameET, passwordET, verifyPasswordET, displayNameET;

    UsersApi usersApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        selectImageBtn = findViewById(R.id.btnSelectImage);
        selectImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelector();
            }
        });

        usernameET = findViewById(R.id.etRegisterUsername);
        passwordET = findViewById(R.id.etRegisterPassword);
        verifyPasswordET = findViewById(R.id.etRegisterVerifyPassword);
        displayNameET = findViewById(R.id.etRegisterDisplayName);
        registerBtn = findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameET.getText().toString();
                String password = passwordET.getText().toString();
                String verPass = verifyPasswordET.getText().toString();
                String displayName = displayNameET.getText().toString();

                String numbersPattern = ".*\\d{1,2}.*";
                boolean containsOneOrTwoNumbers = password.matches(numbersPattern);

                //bad register
                if (username.length() < 4 || displayName.length() == 0 ||
                        password.length() < 8 || !password.equals(verPass) ||
                !containsOneOrTwoNumbers) {
                    Toast.makeText(getApplicationContext(), "Invalid Details",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //Todo: check server response and if good:
                    usersApi = new UsersApi(getApplicationContext());
                    String img = getBase64Image();
                    CompletableFuture<resUser> future = usersApi.createUser(username, displayName, password, img)
                            .thenApply(resUser -> {
                                Toast.makeText(getApplicationContext(), "Add " + username,
                                        Toast.LENGTH_SHORT).show();
                                return resUser;
                            })
                            .exceptionally(error -> {
                                Toast.makeText(getApplicationContext(), error.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                                return null;
                            });
                    future.thenAccept(resUser -> {
                        if (resUser != null)
                            finish();
                    });
                }
            }
        });
    }

    private void openImageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            // Use the selected image URI as needed
            // For example, display the image in an ImageView
            ImageView imageView = findViewById(R.id.ivRegister);
            imageView.setImageURI(imageUri);
        }
    }

    private String getBase64Image() {
        try {
            ImageView imageView = findViewById(R.id.ivRegister);

            Drawable drawable = imageView.getDrawable();
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            String imageType;
            if (bitmap.hasAlpha()) {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                imageType = "data:image/png;base64,";
            }
            else {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                imageType = "data:image/jpeg;base64,";
            }
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            return imageType + Base64.encodeToString(imageBytes, Base64.DEFAULT);
        } catch (Exception e) {
            return null;
        }
    }
}
