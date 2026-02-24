package com.example.fourscreens.data.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fourscreens.data.dao.TicketListingDao;
import com.example.fourscreens.data.entity.TicketListing;

@Database(entities = {TicketListing.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static volatile AppDatabase INSTANCE;

    public abstract TicketListingDao ticketListingDao();

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "tickets_db"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
