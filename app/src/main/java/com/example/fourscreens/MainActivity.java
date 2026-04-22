package com.example.fourscreens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // בדיקה אם המשתמש מחובר - רק אז נשמור לו טוקן
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            saveMessagingToken();
        }

        // מעבר למסך הרשימות
        startActivity(new Intent(MainActivity.this, ListingsActivity.class));
        finish();
    }

    private void saveMessagingToken() {
        // בקשת ה-Token מהשרתים של גוגל
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.w("FCM", "Fetching FCM registration token failed", task.getException());
                return;
            }

            // קבלת הטוקן החדש
            String token = task.getResult();
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // עדכון הטוקן בתוך מסמך המשתמש ב-Firestore
            FirebaseFirestore.getInstance().collection("users").document(userId)
                    .update("fcmToken", token)
                    .addOnSuccessListener(aVoid -> Log.d("FCM", "Token updated successfully"))
                    .addOnFailureListener(e -> Log.e("FCM", "Error updating token", e));
        });
    }
}