package com.example.fourscreens;

import com.example.fourscreens.data.entity.TicketListing;
import com.example.fourscreens.data.entity.Message;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private final FirebaseAuth auth;

    private static final String USERS_COLLECTION = "users";
    private static final String TICKETS_COLLECTION = "tickets";
    private static final String CHATS_COLLECTION = "chats";
    private static final String MESSAGES_COLLECTION = "messages";

    public DBHandler() {
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    // -------------------- TICKETS --------------------

    public void addTicket(TicketListing ticket, FirestoreCallback callback) {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            callback.onFailure("User not connected");
            return;
        }

        String id = db.collection(TICKETS_COLLECTION)
                .document()
                .getId();

        ticket.setId(id);
        ticket.setOwnerId(user.getUid());

        Map<String, Object> map = new HashMap<>();

        map.put("id", ticket.getId());
        map.put("ownerId", ticket.getOwnerId());
        map.put("eventName", ticket.getEventName());
        map.put("eventDate", ticket.getEventDate());
        map.put("location", ticket.getLocation());
        map.put("price", ticket.getPrice());
        map.put("eventType", ticket.getEventType());
        map.put("sellerUsername", ticket.getSellerUsername());
        map.put("description", ticket.getDescription());

        db.collection(TICKETS_COLLECTION)
                .document(id)
                .set(map)
                .addOnCompleteListener(task -> {

                    if (task.isSuccessful()) {

                        callback.onSuccess("הכרטיס פורסם!");

                    } else {

                        String error =
                                task.getException() != null
                                        ? task.getException().getMessage()
                                        : "Unknown Firestore Error";

                        callback.onFailure(error);
                    }
                });
    }
    public void updateTicket(TicketListing ticket, FirestoreCallback callback) {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            callback.onFailure("User not connected");
            return;
        }

        if (ticket.getOwnerId() == null ||
                !ticket.getOwnerId().equals(user.getUid())) {

            callback.onFailure("אין הרשאה לערוך");
            return;
        }

        db.collection(TICKETS_COLLECTION)
                .document(ticket.getId())
                .set(ticket)
                .addOnSuccessListener(unused ->
                        callback.onSuccess("הכרטיס עודכן"))
                .addOnFailureListener(e ->
                        callback.onFailure(e.getMessage()));
    }

    public void deleteTicket(TicketListing ticket, FirestoreCallback callback) {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            callback.onFailure("User not connected");
            return;
        }

        if (ticket.getOwnerId() == null ||
                !ticket.getOwnerId().equals(user.getUid())) {

            callback.onFailure("אין הרשאה למחוק");
            return;
        }

        db.collection(TICKETS_COLLECTION)
                .document(ticket.getId())
                .delete()
                .addOnSuccessListener(unused ->
                        callback.onSuccess("נמחק"))
                .addOnFailureListener(e ->
                        callback.onFailure(e.getMessage()));
    }

    public void listenAllTickets(FirestoreCallback callback) {

        db.collection(TICKETS_COLLECTION)
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        callback.onFailure(error.getMessage());
                        return;
                    }

                    List<TicketListing> list = new ArrayList<>();

                    if (value != null) {

                        for (QueryDocumentSnapshot doc : value) {
                            list.add(doc.toObject(TicketListing.class));
                        }
                    }

                    callback.onTicketsLoaded(list);
                });
    }

    public void listenCurrentUserTickets(FirestoreCallback callback) {

        FirebaseUser user = auth.getCurrentUser();

        if (user == null) {
            callback.onFailure("User not connected");
            return;
        }

        db.collection(TICKETS_COLLECTION)
                .whereEqualTo("ownerId", user.getUid())
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        callback.onFailure(error.getMessage());
                        return;
                    }

                    List<TicketListing> list = new ArrayList<>();

                    if (value != null) {

                        for (QueryDocumentSnapshot doc : value) {
                            list.add(doc.toObject(TicketListing.class));
                        }
                    }

                    callback.onTicketsLoaded(list);
                });
    }

    public void getAllTickets(FirestoreCallback callback) {
        listenAllTickets(callback);
    }

    public void getCurrentUserTickets(FirestoreCallback callback) {
        listenCurrentUserTickets(callback);
    }

    // -------------------- USERS --------------------

    public void getStudentById(String id, FirestoreCallback callback) {

        db.collection(USERS_COLLECTION)
                .document(id)
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
                .addOnFailureListener(e ->
                        callback.onFailure(e.getMessage()));
    }

    // -------------------- CHATS --------------------

    private String generateChatId(String id1, String id2) {

        return (id1.compareTo(id2) < 0)
                ? id1 + "_" + id2
                : id2 + "_" + id1;
    }

    public void sendMessage(String senderId,
                            String receiverId,
                            String text,
                            String senderName,
                            FirestoreCallback callback) {

        if (text.trim().isEmpty()) {
            return;
        }

        String chatId = generateChatId(senderId, receiverId);

        long timestamp = System.currentTimeMillis();

        Map<String, Object> messageMap = new HashMap<>();

        messageMap.put("senderId", senderId);
        messageMap.put("receiverId", receiverId);
        messageMap.put("text", text);
        messageMap.put("timestamp", timestamp);
        messageMap.put("seen", false);

        db.collection(CHATS_COLLECTION)
                .document(chatId)
                .collection(MESSAGES_COLLECTION)
                .add(messageMap)
                .addOnSuccessListener(docRef -> {

                    Map<String, Object> chatSummary = new HashMap<>();

                    chatSummary.put("lastMessage", text);
                    chatSummary.put("timestamp", timestamp);
                    chatSummary.put("participants",
                            Arrays.asList(senderId, receiverId));

                    chatSummary.put("senderId", senderId);
                    chatSummary.put("receiverId", receiverId);

                    db.collection(CHATS_COLLECTION)
                            .document(chatId)
                            .set(chatSummary, SetOptions.merge());

                    callback.onSuccess("נשלח");
                })
                .addOnFailureListener(e ->
                        callback.onFailure(e.getMessage()));
    }

    public void getMessages(String user1,
                            String user2,
                            FirestoreCallback callback) {

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

                    List<Message> messages = new ArrayList<>();

                    if (value != null) {

                        for (QueryDocumentSnapshot doc : value) {
                            messages.add(doc.toObject(Message.class));
                        }
                    }

                    callback.onMessagesLoaded(messages);
                });
    }

    public void getUserChats(String currentUserId,
                             FirestoreCallback callback) {

        db.collection(CHATS_COLLECTION)
                .whereArrayContains("participants", currentUserId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((value, error) -> {

                    if (error != null) {
                        callback.onFailure(error.getMessage());
                        return;
                    }

                    List<Map<String, Object>> chats = new ArrayList<>();

                    if (value != null) {

                        for (QueryDocumentSnapshot doc : value) {

                            Map<String, Object> map = doc.getData();
                            map.put("chatId", doc.getId());

                            chats.add(map);
                        }
                    }

                    callback.onChatsLoaded(chats);
                });
    }
}