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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity implements FirestoreCallback {

    private RecyclerView rvMessages;
    private EditText etMessage;
    private Button btnSend;

    private DBHandler dbHandler;
    private MessageAdapter adapter;
    private List<Message> messageList;

    private FirebaseUser currentUser;

    private String currentUserId;
    private String currentUsername;
    private String receiverId;
    private String receiverUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not connected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        currentUserId = currentUser.getUid();
        currentUsername = currentUser.getEmail();

        receiverId = getIntent().getStringExtra("receiverId");
        receiverUsername = getIntent().getStringExtra("receiverUsername");

        if (receiverId == null || receiverId.trim().isEmpty()) {
            Toast.makeText(this, "לא נמצא משתמש לשיחה", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        if (receiverUsername == null || receiverUsername.trim().isEmpty()) {
            receiverUsername = "משתמש";
        }

        dbHandler = new DBHandler();
        messageList = new ArrayList<>();

        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        adapter = new MessageAdapter(messageList, currentUserId);

        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(adapter);

        setTitle("צ'אט עם " + receiverUsername);

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();

            if (!text.isEmpty()) {
                sendMessage(text);
            }
        });

        listenForMessages();
    }

    private void sendMessage(String text) {
        dbHandler.sendMessage(
                currentUserId,
                receiverId,
                text,
                currentUsername,
                this
        );

        etMessage.setText("");
    }

    private void listenForMessages() {
        dbHandler.getMessages(
                currentUserId,
                receiverId,
                this
        );
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
        // אין צורך בטוסט על כל הודעה
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this, "שגיאה: " + error, Toast.LENGTH_SHORT).show();
    }
}