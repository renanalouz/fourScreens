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
    private Button btnAdd;
    private TicketListingAdapter adapter;
    private DBHandler dbHandler;

    // כרגע משתמש דמו, אחר כך נחליף למשתמש מחובר אמיתי
    private final String sellerUsername = "demoUser";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        rvListings = findViewById(R.id.rvListings);
        btnAdd = findViewById(R.id.btnAdd);

        dbHandler = new DBHandler();

        adapter = new TicketListingAdapter(new TicketListingAdapter.OnTicketActionListener() {
            @Override
            public void onEdit(TicketListing ticket) {
                updateTicket(ticket);
            }

            @Override
            public void onDelete(TicketListing ticket) {
                deleteTicket(ticket);
            }
        });

        rvListings.setLayoutManager(new LinearLayoutManager(this));
        rvListings.setAdapter(adapter);

        loadTickets();

        btnAdd.setOnClickListener(v -> addNewTicket());
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadTickets();
    }

    private void loadTickets() {
        dbHandler.getTicketsBySeller(sellerUsername, this);
    }

    private void addNewTicket() {
        TicketListing newTicket = new TicketListing(
                "",
                "הופעה חדשה",
                "20/05/2026",
                "חיפה",
                200,
                "הופעה",
                sellerUsername,
                "תיאור הכרטיס"
        );

        dbHandler.addTicket(newTicket, this);
    }

    private void updateTicket(TicketListing ticket) {
        ticket.setPrice(ticket.getPrice() + 10);
        ticket.setDescription(ticket.getDescription() + " (עודכן)");

        dbHandler.updateTicket(ticket, this);
    }

    private void deleteTicket(TicketListing ticket) {
        dbHandler.deleteTicket(ticket.getId(), this);
    }

    @Override
    public void onTicketsLoaded(List<TicketListing> tickets) {
        adapter.setListings(tickets);
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        loadTickets();
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}