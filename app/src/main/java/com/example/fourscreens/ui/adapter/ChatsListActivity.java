package com.example.fourscreens;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChatsListActivity extends AppCompatActivity implements FirestoreCallback {

    private ListView listChats;

    private FirebaseUser currentUser;
    private DBHandler dbHandler;

    private ArrayList<String> chatTitles;
    private ArrayList<String> receiverIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_list);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "User not connected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        listChats = findViewById(R.id.listChats);

        dbHandler = new DBHandler();

        chatTitles = new ArrayList<>();
        receiverIds = new ArrayList<>();

        loadChats();

        listChats.setOnItemClickListener((parent, view, position, id) -> {
            Intent intent = new Intent(this, ChatActivity.class);

            intent.putExtra("receiverId", receiverIds.get(position));
            intent.putExtra("receiverUsername", "משתמש");

            startActivity(intent);
        });
    }

    private void loadChats() {
        dbHandler.getUserChats(currentUser.getUid(), this);
    }

    @Override
    public void onChatsLoaded(List<Map<String, Object>> chats) {
        chatTitles.clear();
        receiverIds.clear();

        for (Map<String, Object> chat : chats) {
            String lastMessage = String.valueOf(chat.get("lastMessage"));
            String senderId = String.valueOf(chat.get("senderId"));
            String receiverId = String.valueOf(chat.get("receiverId"));

            String otherUserId;

            if (senderId.equals(currentUser.getUid())) {
                otherUserId = receiverId;
            } else {
                otherUserId = senderId;
            }

            receiverIds.add(otherUserId);
            chatTitles.add("שיחה עם משתמש\n" + lastMessage);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        chatTitles
                );

        listChats.setAdapter(adapter);
    }

    @Override
    public void onFailure(String error) {
        Toast.makeText(this, "שגיאה: " + error, Toast.LENGTH_SHORT).show();
    }
}