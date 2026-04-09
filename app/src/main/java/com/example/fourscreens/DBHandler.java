package com.example.fourscreens;

import com.example.fourscreens.data.entity.TicketListing;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBHandler {

    private final FirebaseFirestore db;
    private static final String COLLECTION_NAME = "tickets";
    private static final String STUDENTS_COLLECTION = "students";

    public DBHandler() {
        db = FirebaseFirestore.getInstance();
    }

    public void createDocument(String fullName, String id, String grade, FirestoreCallback callback) {
        Map<String, Object> student = new HashMap<>();
        student.put("fullName", fullName);
        student.put("id", id);
        student.put("grade", grade);

        db.collection(STUDENTS_COLLECTION)
                .document(id)
                .set(student)
                .addOnSuccessListener(unused -> callback.onSuccess("המסמך נוסף בהצלחה"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void addTicket(TicketListing ticket, FirestoreCallback callback) {
        String id = db.collection(COLLECTION_NAME).document().getId();
        ticket.setId(id);

        db.collection(COLLECTION_NAME)
                .document(id)
                .set(ticket)
                .addOnSuccessListener(unused -> callback.onSuccess("הכרטיס נוסף בהצלחה"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getAllTickets(FirestoreCallback callback) {
        db.collection(COLLECTION_NAME)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<TicketListing> tickets = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        TicketListing ticket = document.toObject(TicketListing.class);
                        tickets.add(ticket);
                    }

                    callback.onTicketsLoaded(tickets);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getTicketsBySeller(String sellerUsername, FirestoreCallback callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("sellerUsername", sellerUsername)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<TicketListing> tickets = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        TicketListing ticket = document.toObject(TicketListing.class);
                        tickets.add(ticket);
                    }

                    callback.onTicketsLoaded(tickets);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getTicketsBySellerAndType(String sellerUsername, String eventType, FirestoreCallback callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("sellerUsername", sellerUsername)
                .whereEqualTo("eventType", eventType)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<TicketListing> tickets = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        TicketListing ticket = document.toObject(TicketListing.class);
                        tickets.add(ticket);
                    }

                    callback.onTicketsLoaded(tickets);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void getTicketsByType(String eventType, FirestoreCallback callback) {
        db.collection(COLLECTION_NAME)
                .whereEqualTo("eventType", eventType)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<TicketListing> tickets = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        TicketListing ticket = document.toObject(TicketListing.class);
                        tickets.add(ticket);
                    }

                    callback.onTicketsLoaded(tickets);
                })
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void updateTicket(TicketListing ticket, FirestoreCallback callback) {
        if (ticket.getId() == null || ticket.getId().isEmpty()) {
            callback.onFailure("אין מזהה לכרטיס");
            return;
        }

        db.collection(COLLECTION_NAME)
                .document(ticket.getId())
                .set(ticket)
                .addOnSuccessListener(unused -> callback.onSuccess("הכרטיס עודכן בהצלחה"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void deleteTicket(String ticketId, FirestoreCallback callback) {
        db.collection(COLLECTION_NAME)
                .document(ticketId)
                .delete()
                .addOnSuccessListener(unused -> callback.onSuccess("הכרטיס נמחק בהצלחה"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
}