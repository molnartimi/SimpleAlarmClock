package com.learntodroid.simplealarmclock.data.contact;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Contact.class}, version = 1, exportSchema = false)
public abstract class ContactDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();

    private static volatile ContactDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static ContactDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ContactDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            ContactDatabase.class,
                            "contact_database"
                    ).build();
                }
            }
        }
        return INSTANCE;
    }
}
