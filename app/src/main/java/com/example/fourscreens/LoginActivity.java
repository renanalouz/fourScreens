package com.example.fourscreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.loginButton);
        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, TicketsListActivity.class);
            startActivity(intent);
        });
    }
}
