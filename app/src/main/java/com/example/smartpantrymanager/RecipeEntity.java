package com.example.smartpantrymanager;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class RecipeEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String name;

    /*
     * Recipe requirements are stored in this format:
     *
     * ingredient|quantity|unit;
     * ingredient|quantity|unit
     *
     * Example:
     * eggs|2|pieces;milk|100|ml
     */
    private String requirements;

    private String instructions;

    public RecipeEntity(
            String name,
            String requirements,
            String instructions
    ) {
        this.name = name;
        this.requirements = requirements;
        this.instructions = instructions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
