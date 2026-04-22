package com.example.fourscreens;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fourscreens.data.entity.Message;
import com.example.fourscreens.ui.adapter.MessageAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements FirestoreCallback {

    private RecyclerView rvMessages;
    private EditText etMessage;
    private Button btnSend;

    private DBHandler dbHandler;
    private MessageAdapter adapter;
    private List<Message> messageList;

    // כרגע משתמש דמו עד שנעשה התחברות אמיתית
    private String currentUserId = "demoUser";
    private String receiverId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbHandler = new DBHandler();
        messageList = new ArrayList<>();

        if (getIntent() != null) {
            String senderFromIntent = getIntent().getStringExtra("senderUsername");
            String receiverFromIntent = getIntent().getStringExtra("receiverUsername");

            if (senderFromIntent != null && !senderFromIntent.trim().isEmpty()) {
                currentUserId = senderFromIntent;
            }

            if (receiverFromIntent != null && !receiverFromIntent.trim().isEmpty()) {
                receiverId = receiverFromIntent;
            }
        }

        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        adapter = new MessageAdapter(messageList, currentUserId);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(adapter);

        if (receiverId.isEmpty()) {
            Toast.makeText(this, "לא נמצא משתמש לשיחה", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setTitle("צ'אט עם " + receiverId);

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                sendMessage(text);
            }
        });

        listenForMessages();
    }

    private void sendMessage(String text) {
        dbHandler.sendMessage(currentUserId, receiverId, text, currentUserId, this);
        etMessage.setText("");
    }

    private void listenForMessages() {
        dbHandler.getMessages(currentUserId, receiverId, this);
    }

    @Override
    public void onMessagesLoaded(List<Message> messages) {
        messageList.clear();
        messageList.addAll(messages);
        adapter.notifyDataSetChanged();

        if (!messageList.isEmpty()) {
            rvMessages.scrollToPosition(messageList.size() - 1);
        }
    }

    @Override
    public void onSuccess(String message) {
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this, "שגיאה: " + error, Toast.LENGTH_SHORT).show();
    }
}