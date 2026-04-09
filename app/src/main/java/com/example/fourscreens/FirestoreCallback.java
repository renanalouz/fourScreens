package com.example.fourscreens;

import com.example.fourscreens.data.entity.TicketListing;
import java.util.List;

public interface FirestoreCallback {
    void onTicketsLoaded(List<TicketListing> tickets);
    void onSuccess(String message);
    void onFailure(String error);
}