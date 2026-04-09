package com.example.fourscreens.data.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ticket_listings")
public class TicketListing implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;

    private String eventName;
    private String eventDate;
    private String location;
    private int price;
    private String eventType;
    private String sellerUsername;
    private String description;

    public TicketListing() {
        this.id = "";
    }

    @Ignore
    public TicketListing(String id, String eventName, String eventDate, String location, int price,
                         String eventType, String sellerUsername, String description) {
        this.id = id;
        this.eventName = eventName;
        this.eventDate = eventDate;
        this.location = location;
        this.price = price;
        this.eventType = eventType;
        this.sellerUsername = sellerUsername;
        this.description = description;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getSellerUsername() {
        return sellerUsername;
    }

    public void setSellerUsername(String sellerUsername) {
        this.sellerUsername = sellerUsername;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}