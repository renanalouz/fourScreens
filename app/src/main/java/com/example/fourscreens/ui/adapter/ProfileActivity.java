package com.example.fourscreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvEmail;

    private Button btnMyTickets;
    private Button btnAllTickets;
    private Button btnChats;
    private Button btnLogout;

    private FirebaseAuth auth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        tvEmail = findViewById(R.id.tvEmail);

        btnMyTickets = findViewById(R.id.btnMyTickets);
        btnAllTickets = findViewById(R.id.btnAllTickets);
        btnChats = findViewById(R.id.btnChats);
        btnLogout = findViewById(R.id.btnLogout);

        tvEmail.setText(currentUser.getEmail());

        btnMyTickets.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListingsActivity.class);
            intent.putExtra("mode", "mine");
            startActivity(intent);
        });

        btnAllTickets.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListingsActivity.class);
            intent.putExtra("mode", "all");
            startActivity(intent);
        });

        btnChats.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatsListActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> {

            auth.signOut();

            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

            finishAffinity();
        });
    }
}