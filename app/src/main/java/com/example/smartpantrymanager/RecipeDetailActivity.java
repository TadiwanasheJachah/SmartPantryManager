package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeTitle;
    private TextView tvRecipeIngredients;
    private TextView tvRecipeInstructions;
    private Button btnBackToRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        tvRecipeTitle = findViewById(R.id.tvRecipeTitle);
        tvRecipeIngredients = findViewById(R.id.tvRecipeIngredients);
        tvRecipeInstructions = findViewById(R.id.tvRecipeInstructions);
        btnBackToRecipes = findViewById(R.id.btnBackToRecipes);

        Intent intent = getIntent();

        String recipeName = intent.getStringExtra("recipe_name");
        String recipeIngredients = intent.getStringExtra("recipe_ingredients");
        String recipeInstructions = intent.getStringExtra("recipe_instructions");

        tvRecipeTitle.setText(recipeName);
        tvRecipeIngredients.setText(recipeIngredients);
        tvRecipeInstructions.setText(recipeInstructions);

        btnBackToRecipes.setOnClickListener(view -> finish());
    }
}