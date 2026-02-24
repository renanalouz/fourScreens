package com.example.fourscreens;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class TicketsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tickets_list);

        CardView card1 = findViewById(R.id.cardTicket1);
        CardView card2 = findViewById(R.id.cardTicket2);

        card1.setOnClickListener(v -> openDetails());
        card2.setOnClickListener(v -> openDetails());
    }

    private void openDetails() {
        Intent intent = new Intent(TicketsListActivity.this, TicketDetailsActivity.class);
        startActivity(intent);
    }
}
