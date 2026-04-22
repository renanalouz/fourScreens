package com.example.fourscreens;

import com.example.fourscreens.data.entity.TicketListing;
import com.example.fourscreens.data.entity.Message; // תיקון חשוב

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHandler {

    private final FirebaseFirestore db;

    private static final String USERS_COLLECTION = "users";
    private static final String TICKETS_COLLECTION = "tickets";
    private static final String CHATS_COLLECTION = "chats";
    private static final String MESSAGES_COLLECTION = "messages";

    public DBHandler() {
        db = FirebaseFirestore.getInstance();
    }

    // ==========================================
    // 1. כרטיסים (CRUD מלא)
    // ==========================================

    public void addTicket(TicketListing ticket, FirestoreCallback callback) {
        String id = db.collection(TICKETS_COLLECTION).document().getId();
        ticket.setId(id);

        db.collection(TICKETS_COLLECTION).document(id)
                .set(ticket)
                .addOnSuccessListener(unused -> callback.onSuccess("הכרטיס פורסם!"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void updateTicket(TicketListing ticket, FirestoreCallback callback) {
        db.collection(TICKETS_COLLECTION).document(ticket.getId())
                .set(ticket)
                .addOnSuccessListener(unused -> callback.onSuccess("הכרטיס עודכן"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void deleteTicket(String ticketId, FirestoreCallback callback) {
        db.collection(TICKETS_COLLECTION).document(ticketId)
                .delete()
                .addOnSuccessListener(unused -> callback.onSuccess("נמחק"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getTicketsBySeller(String sellerName, FirestoreCallback callback) {
        db.collection(TICKETS_COLLECTION)
                .whereEqualTo("sellerUsername", sellerName)
                .get()
                .addOnSuccessListener(query -> {
                    List<TicketListing> list = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : query) {
                        list.add(doc.toObject(TicketListing.class));
                    }
                    callback.onTicketsLoaded(list);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getAllTickets(FirestoreCallback callback) {
        db.collection(TICKETS_COLLECTION)
                .get()
                .addOnSuccessListener(query -> {
                    List<TicketListing> list = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : query) {
                        list.add(doc.toObject(TicketListing.class));
                    }
                    callback.onTicketsLoaded(list);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ==========================================
    // 2. משתמשים
    // ==========================================

    public void getStudentById(String id, FirestoreCallback callback) {
        db.collection(USERS_COLLECTION).document(id)
                .get()
                .addOnSuccessListener(doc -> {
                    if (doc.exists()) {
                        Student s = doc.toObject(Student.class);
                        List<Student> list = new ArrayList<>();
                        list.add(s);
                        callback.onStudentsLoaded(list);
                    } else {
                        callback.onFailure("לא נמצא משתמש");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    // ==========================================
    // 3. צ'אט
    // ==========================================

    private String generateChatId(String id1, String id2) {
        return (id1.compareTo(id2) < 0)
                ? id1 + "_" + id2
                : id2 + "_" + id1;
    }

    public void sendMessage(String senderId, String receiverId, String text, String senderName, FirestoreCallback callback) {

        if (text.trim().isEmpty()) return;

        String chatId = generateChatId(senderId, receiverId);
        long timestamp = System.currentTimeMillis();

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("senderId", senderId);
        messageMap.put("receiverId", receiverId);
        messageMap.put("text", text);
        messageMap.put("timestamp", timestamp);

        db.collection(CHATS_COLLECTION)
                .document(chatId)
                .collection(MESSAGES_COLLECTION)
                .add(messageMap)
                .addOnSuccessListener(docRef -> {

                    Map<String, Object> chatSummary = new HashMap<>();
                    chatSummary.put("lastMessage", text);
                    chatSummary.put("timestamp", timestamp);
                    chatSummary.put("participants", Arrays.asList(senderId, receiverId));

                    db.collection(CHATS_COLLECTION)
                            .document(chatId)
                            .set(chatSummary, SetOptions.merge());

                    callback.onSuccess("נשלח");
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getMessages(String user1, String user2, FirestoreCallback callback) {

        String chatId = generateChatId(user1, user2);

        db.collection(CHATS_COLLECTION)
                .document(chatId)
                .collection(MESSAGES_COLLECTION)
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        callback.onFailure(error.getMessage());
                        return;
                    }

                    if (value != null) {
                        List<Message> messages = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : value) {
                            messages.add(doc.toObject(Message.class));
                        }

                        callback.onMessagesLoaded(messages);
                    }
                });
    }
}