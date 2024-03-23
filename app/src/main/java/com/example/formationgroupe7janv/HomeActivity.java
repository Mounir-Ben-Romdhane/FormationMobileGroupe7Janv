package com.example.formationgroupe7janv;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private TextView emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        emailUser = findViewById(R.id.emailUser);

        SharedPreferences preferences = getSharedPreferences("checkBox", MODE_PRIVATE);
        String email = preferences.getString("emailUser", "");
        emailUser.setText(email);
    }
}