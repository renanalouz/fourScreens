package com.example.fourscreens;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fourscreens.data.db.AppDatabase;
import com.example.fourscreens.data.entity.TicketListing;
import com.example.fourscreens.ui.adapter.TicketListingAdapter;

import java.util.concurrent.Executors;

public class ListingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listings);

        // RecyclerView
        RecyclerView rvListings = findViewById(R.id.rvListings);
        TicketListingAdapter adapter = new TicketListingAdapter();

        rvListings.setLayoutManager(new LinearLayoutManager(this));
        rvListings.setAdapter(adapter);

        // Database
        AppDatabase db = AppDatabase.getInstance(this);

        // הצגת הנתונים
        db.ticketListingDao().getAll().observe(this, adapter::setListings);

        // כפתור הוספה
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> {

            TicketListing newItem = new TicketListing(
                    "הופעה לדוגמה",
                    "12/03/2026",
                    "תל אביב",
                    250
            );
// t
            Executors.newSingleThreadExecutor().execute(() ->
                    db.ticketListingDao().insert(newItem)
            );
        });
    }
}

