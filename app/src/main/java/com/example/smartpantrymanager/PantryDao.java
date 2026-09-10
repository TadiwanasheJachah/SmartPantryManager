package com.example.smartpantrymanager;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PantryDao {

    @Insert
    void insert(PantryItem pantryItem);

    @Update
    void update(PantryItem pantryItem);

    @Delete
    void delete(PantryItem pantryItem);

    @Query("SELECT * FROM pantry_items ORDER BY name ASC")
    List<PantryItem> getAllPantryItems();
}
