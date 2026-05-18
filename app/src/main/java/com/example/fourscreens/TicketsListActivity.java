package com.example.fourscreens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class TicketsListActivity extends AppCompatActivity {

    private FirebaseUser currentUser;

    private CardView cardAllTickets;
    private CardView cardMyTickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets_list);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(TicketsListActivity.this, LoginActivity.class));
            finish();
            return;
        }

        cardAllTickets = findViewById(R.id.cardAllTickets);
        cardMyTickets = findViewById(R.id.cardMyTickets);

        cardAllTickets.setOnClickListener(v -> {
            Intent intent = new Intent(TicketsListActivity.this, ListingsActivity.class);
            intent.putExtra("mode", "all");
            startActivity(intent);
        });

        cardMyTickets.setOnClickListener(v -> {
            Intent intent = new Intent(TicketsListActivity.this, ListingsActivity.class);
            intent.putExtra("mode", "mine");
            startActivity(intent);
        });

        findViewById(R.id.btnHome).setOnClickListener(v -> {
            // כבר במסך הבית
        });

        findViewById(R.id.btnMessages).setOnClickListener(v -> {
            Intent intent = new Intent(TicketsListActivity.this, ChatsListActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.btnProfile).setOnClickListener(v -> {
            Intent intent = new Intent(TicketsListActivity.this, ProfileActivity.class);
            startActivity(intent);
        });
    }
}