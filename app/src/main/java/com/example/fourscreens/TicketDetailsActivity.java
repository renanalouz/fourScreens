package com.example.fourscreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fourscreens.data.entity.TicketListing;

public class TicketDetailsActivity extends AppCompatActivity implements FirestoreCallback {

    private EditText etName, etDate, etLocation, etPrice, etType, etDescription;
    private Button btnUpdate, btnDelete, btnChat;
    private DBHandler dbHandler;
    private TicketListing ticket;

    // כרגע משתמש דמו. אחר כך נחליף למשתמש מחובר אמיתי
    private final String currentUsername = "demoUser";

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
        btnChat = findViewById(R.id.btnChat);

        dbHandler = new DBHandler();
        ticket = (TicketListing) getIntent().getSerializableExtra("ticket");

        if (ticket == null) {
            Toast.makeText(this, "לא נמצא כרטיס", Toast.LENGTH_SHORT).show();
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

        btnDelete.setOnClickListener(v -> dbHandler.deleteTicket(ticket.getId(), this));

        btnChat.setOnClickListener(v -> {
            if (ticket.getSellerUsername() == null || ticket.getSellerUsername().trim().isEmpty()) {
                Toast.makeText(this, "לא נמצא מוכר", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ticket.getSellerUsername().equals(currentUsername)) {
                Toast.makeText(this, "זה הכרטיס שלך", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(TicketDetailsActivity.this, ChatActivity.class);
            intent.putExtra("senderUsername", currentUsername);
            intent.putExtra("receiverUsername", ticket.getSellerUsername());
            intent.putExtra("ticketId", ticket.getId());
            intent.putExtra("ticketName", ticket.getEventName());
            startActivity(intent);
        });
    }

    private void updateTicket() {
        String name = etName.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String location = etLocation.getText().toString().trim();
        String priceText = etPrice.getText().toString().trim();
        String type = etType.getText().toString().trim();
        String description = etDescription.getText().toString().trim();

        if (name.isEmpty() || date.isEmpty() || location.isEmpty() || priceText.isEmpty() || type.isEmpty()) {
            Toast.makeText(this, "מלאי את כל השדות החובה", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "מחיר חייב להיות מספר", Toast.LENGTH_SHORT).show();
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