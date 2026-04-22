package com.example.fourscreens;

import com.example.fourscreens.data.entity.TicketListing;
import com.example.fourscreens.data.entity.Message;

import java.util.List;
import java.util.Map;

public interface FirestoreCallback {

    default void onSuccess(String message) {}

    default void onFailure(String error) {}

    default void onTicketsLoaded(List<TicketListing> tickets) {}

    default void onMessagesLoaded(List<Message> messages) {}

    default void onStudentsLoaded(List<Student> students) {}

    default void onChatsLoaded(List<Map<String, Object>> chats) {}

    default void onUsersLoaded(List<Map<String, Object>> users) {}
}