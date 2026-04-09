package com.example.fourscreens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fourscreens.data.entity.TicketListing;

import java.util.List;

public class TicketDetailsActivity extends AppCompatActivity implements FirestoreCallback {

    private EditText etName;
    private EditText etDate;
    private EditText etLocation;
    private EditText etPrice;
    private EditText etType;
    private EditText etDescription;
    private Button btnUpdate;
    private Button btnDelete;

    private DBHandler dbHandler;
    private TicketListing ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_details);

        etName = findViewById(R.id.etName);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);
        etPrice = findViewById(R.id.etPrice);
        etType = findViewById(R.id.etType);
        etDescription = findViewById(R.id.etDescription);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        dbHandler = new DBHandler();

        Object extra = getIntent().getSerializableExtra("ticket");
        if (extra instanceof TicketListing) {
            ticket = (TicketListing) extra;
        }

        if (ticket == null) {
            Toast.makeText(this, "לא התקבל כרטיס", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        etName.setText(ticket.getEventName());
        etDate.setText(ticket.getEventDate());
        etLocation.setText(ticket.getLocation());
        etPrice.setText(String.valueOf(ticket.getPrice()));
        etType.setText(ticket.getEventType());
        etDescription.setText(ticket.getDescription());

        btnUpdate.setOnClickListener(v -> updateTicket());
        btnDelete.setOnClickListener(v -> deleteTicket());
    }

    private void updateTicket() {
        String name = etName.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        String type = etType.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || location.isEmpty() || priceText.isEmpty() || type.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "מלאי את כל השדות", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "המחיר חייב להיות מספר", Toast.LENGTH_SHORT).show();
            return;
        }

        ticket.setEventName(name);
        ticket.setEventDate(date);
        ticket.setLocation(location);
        ticket.setPrice(price);
        ticket.setEventType(type);
        ticket.setDescription(description);

        dbHandler.updateTicket(ticket, this);
    }

    private void deleteTicket() {
        dbHandler.deleteTicket(ticket.getId(), this);
    }

    @Override
    public void onTicketsLoaded(List<TicketListing> tickets) {
    }

    @Override
    public void onSuccess(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }
}