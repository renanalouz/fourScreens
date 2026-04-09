package com.example.fourscreens.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.fourscreens.data.entity.TicketListing;

import java.util.List;

@Dao
public interface TicketListingDao {

    @Query("SELECT * FROM ticket_listings")
    LiveData<List<TicketListing>> getAll();

    @Insert
    void insert(TicketListing ticketListing);

    @Update
    void update(TicketListing ticketListing);

    @Delete
    void delete(TicketListing ticketListing);
}