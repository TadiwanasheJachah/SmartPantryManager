package com.example.smartpantrymanager;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(
        entities = {PantryItem.class},
        version = 1,
        exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase INSTANCE;

    public abstract PantryDao pantryDao();

    public static synchronized AppDatabase getInstance(Context context) {

        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "smart_pantry_database"
                    )
                    .allowMainThreadQueries()
                    .build();
        }

        return INSTANCE;
    }
}
