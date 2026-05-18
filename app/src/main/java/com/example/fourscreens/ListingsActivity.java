package com.example.fourscreens;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fourscreens.data.entity.TicketListing;
import com.example.fourscreens.ui.adapter.TicketListingAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class ListingsActivity extends AppCompatActivity implements FirestoreCallback {

    private RecyclerView rvListings;
    private Button btnAdd;
    private TicketListingAdapter adapter;
    private DBHandler dbHandler;

    private FirebaseUser currentUser;
    private String sellerUsername;
    private String ownerId;
    private String mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not connected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        ownerId = currentUser.getUid();
        sellerUsername = currentUser.getEmail();

        mode = getIntent().getStringExtra("mode");
        if (mode == null) {
            mode = "all";
        }

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

        if (mode.equals("mine")) {
            setTitle("הכרטיסים שלי");
            btnAdd.setVisibility(View.VISIBLE);
        } else {
            setTitle("כל הכרטיסים");
            btnAdd.setVisibility(View.GONE);
        }

        btnAdd.setOnClickListener(v -> addNewTicket());

        listenTickets();
    }

    private void listenTickets() {
        if (mode.equals("mine")) {
            dbHandler.listenCurrentUserTickets(this);
        } else {
            dbHandler.listenAllTickets(this);
        }
    }

    private void addNewTicket() {
        Toast.makeText(this,
                "מנסה לשמור כרטיס למשתמש: " + sellerUsername,
                Toast.LENGTH_LONG).show();

        TicketListing newTicket = new TicketListing(
                "",
                ownerId,
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
        dbHandler.deleteTicket(ticket, this);
    }

    @Override
    public void onTicketsLoaded(List<TicketListing> tickets) {
        adapter.setListings(tickets);
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this,
                "שגיאה בשמירה: " + error,
                Toast.LENGTH_LONG).show();
    }
}