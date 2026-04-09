package com.example.fourscreens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fourscreens.data.entity.TicketListing;
import com.example.fourscreens.ui.adapter.TicketListingAdapter;

import java.util.List;

public class ListingsActivity extends AppCompatActivity implements FirestoreCallback {

    private RecyclerView rvListings;
    private TicketListingAdapter adapter;
    private DBHandler dbHandler;
    private String sellerUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        rvListings = findViewById(R.id.rvListings);
        Button btnAdd = findViewById(R.id.btnAdd);

        adapter = new TicketListingAdapter();
        rvListings.setLayoutManager(new LinearLayoutManager(this));
        rvListings.setAdapter(adapter);

        dbHandler = new DBHandler();
        sellerUsername = "demoUser";

        dbHandler.getTicketsBySeller(sellerUsername, this);

        btnAdd.setOnClickListener(v -> {
            TicketListing newItem = new TicketListing(
                    "",
                    "הופעה לדוגמה",
                    "12/03/2026",
                    "תל אביב",
                    250,
                    "הופעה",
                    sellerUsername,
                    "כרטיס לדוגמה"
            );
            dbHandler.addTicket(newItem, this);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbHandler.getTicketsBySeller(sellerUsername, this);
    }

    @Override
    public void onTicketsLoaded(List<TicketListing> tickets) {
        adapter.setListings(tickets);
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        dbHandler.getTicketsBySeller(sellerUsername, this);
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}