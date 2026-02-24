package com.example.fourscreens.data.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

    @Entity(tableName = "ticket_listings")
    public class TicketListing {

        @PrimaryKey(autoGenerate = true)
        public int id;

        public String eventName;
        public String eventDate;
        public String location;
        public int price;

        public TicketListing(String eventName, String eventDate, String location, int price) {
            this.eventName = eventName;
            this.eventDate = eventDate;
            this.location = location;
            this.price = price;
        }
    }

