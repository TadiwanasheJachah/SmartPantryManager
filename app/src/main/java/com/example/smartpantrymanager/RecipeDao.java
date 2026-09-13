package com.example.smartpantrymanager;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RecipeDao {

    @Insert
    void insert(RecipeEntity recipe);

    @Insert
    void insertAll(List<RecipeEntity> recipes);

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    List<RecipeEntity> getAllRecipes();

    @Query("SELECT COUNT(*) FROM recipes")
    int getRecipeCount();

    @Query("DELETE FROM recipes")
    void deleteAll();
}
