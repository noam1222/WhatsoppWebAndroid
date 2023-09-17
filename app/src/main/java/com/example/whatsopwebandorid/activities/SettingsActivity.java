package com.example.whatsopwebandorid.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.whatsopwebandorid.R;
import com.example.whatsopwebandorid.Tools;

public class SettingsActivity extends AppCompatActivity {

    private EditText editTextServerUrl;
    private Button btnSetServerUrl;

    private RelativeLayout greenRectangle;
    private RelativeLayout purpleRectangle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editTextServerUrl = findViewById(R.id.editTextServerUrl);
        editTextServerUrl.setText(Tools.getApiConnectionString(getApplicationContext()));
        btnSetServerUrl = findViewById(R.id.btnSetServerUrl);
        btnSetServerUrl.setOnClickListener(view -> {
            String newApiString = editTextServerUrl.getText().toString();
            if (Tools.isGoodConnectionString(newApiString)) {
                Tools.setApiStringPattern(getApplicationContext(), newApiString);
                Toast.makeText(getApplicationContext(), "API connection string has been changed",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Bad connection string",
                        Toast.LENGTH_SHORT).show();
            }
        });


        greenRectangle = findViewById(R.id.greenRectangle);
        purpleRectangle = findViewById(R.id.purpleRectangle);

        greenRectangle.setOnClickListener(view -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        });


        purpleRectangle.setOnClickListener(view -> {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        });
    }

}
