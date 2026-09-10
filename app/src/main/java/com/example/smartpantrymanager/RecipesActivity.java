package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecipesActivity extends AppCompatActivity {

    private LinearLayout recipeContainer;
    private TextView txtNoRecipes;
    private Button btnBackToPantry;

    private PantryDao pantryDao;

    private final List<Recipe> recipes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        recipeContainer = findViewById(R.id.recipeContainer);
        txtNoRecipes = findViewById(R.id.txtNoRecipes);
        btnBackToPantry = findViewById(R.id.btnBackToPantry);

        AppDatabase database = AppDatabase.getInstance(this);
        pantryDao = database.pantryDao();

        createRecipes();
        showMatchingRecipes();

        btnBackToPantry.setOnClickListener(view -> {
            Intent intent = new Intent(
                    RecipesActivity.this,
                    PantryActivity.class
            );

            startActivity(intent);
            finish();
        });
    }

    private void createRecipes() {

        recipes.add(new Recipe(
                "Scrambled Eggs",
                Arrays.asList("eggs", "milk"),
                "1. Crack the eggs into a bowl.\n" +
                        "2. Add the milk and whisk together.\n" +
                        "3. Pour the mixture into a heated pan.\n" +
                        "4. Stir gently until the eggs are cooked."
        ));

        recipes.add(new Recipe(
                "Cheese Omelette",
                Arrays.asList("eggs", "cheese"),
                "1. Beat the eggs in a bowl.\n" +
                        "2. Pour them into a heated pan.\n" +
                        "3. Add cheese on top.\n" +
                        "4. Fold the omelette and cook until the cheese melts."
        ));

        recipes.add(new Recipe(
                "French Toast",
                Arrays.asList("bread", "eggs", "milk"),
                "1. Beat the eggs and milk together.\n" +
                        "2. Dip each slice of bread into the mixture.\n" +
                        "3. Place the bread in a heated pan.\n" +
                        "4. Cook both sides until golden."
        ));

        recipes.add(new Recipe(
                "Cheese Toast",
                Arrays.asList("bread", "cheese"),
                "1. Place cheese on the bread.\n" +
                        "2. Toast or heat until the bread is crisp and the cheese has melted."
        ));

        recipes.add(new Recipe(
                "Tomato Sandwich",
                Arrays.asList("bread", "tomato"),
                "1. Slice the tomato.\n" +
                        "2. Place the tomato slices between the bread.\n" +
                        "3. Serve immediately."
        ));

        recipes.add(new Recipe(
                "Chicken and Rice",
                Arrays.asList("chicken", "rice"),
                "1. Cook the rice until tender.\n" +
                        "2. Cook the chicken thoroughly.\n" +
                        "3. Serve the chicken together with the rice."
        ));

        recipes.add(new Recipe(
                "Tomato Rice",
                Arrays.asList("rice", "tomato"),
                "1. Cook the rice.\n" +
                        "2. Chop the tomato.\n" +
                        "3. Cook the tomato until soft.\n" +
                        "4. Mix the tomato with the cooked rice."
        ));

        recipes.add(new Recipe(
                "Chicken Pasta",
                Arrays.asList("chicken", "pasta", "tomato"),
                "1. Cook the pasta.\n" +
                        "2. Cook the chicken thoroughly.\n" +
                        "3. Add chopped tomato and cook until soft.\n" +
                        "4. Combine everything and serve."
        ));

        recipes.add(new Recipe(
                "Cheesy Pasta",
                Arrays.asList("pasta", "cheese", "milk"),
                "1. Cook the pasta.\n" +
                        "2. Heat the milk gently.\n" +
                        "3. Add the cheese and stir until melted.\n" +
                        "4. Mix the cheese sauce with the pasta."
        ));

        recipes.add(new Recipe(
                "Banana Oats",
                Arrays.asList("banana", "oats", "milk"),
                "1. Cook the oats with milk.\n" +
                        "2. Slice the banana.\n" +
                        "3. Add the banana to the cooked oats and serve."
        ));

        recipes.add(new Recipe(
                "Banana Toast",
                Arrays.asList("banana", "bread"),
                "1. Toast the bread.\n" +
                        "2. Slice or mash the banana.\n" +
                        "3. Place the banana on top of the toast."
        ));

        recipes.add(new Recipe(
                "Potato and Egg Breakfast",
                Arrays.asList("potato", "eggs"),
                "1. Cut the potato into small pieces.\n" +
                        "2. Cook the potato until tender.\n" +
                        "3. Add beaten eggs.\n" +
                        "4. Cook until the eggs are fully done."
        ));

        recipes.add(new Recipe(
                "Chicken and Potato Meal",
                Arrays.asList("chicken", "potato"),
                "1. Cut the potato into pieces.\n" +
                        "2. Cook the potato until tender.\n" +
                        "3. Cook the chicken thoroughly.\n" +
                        "4. Serve together."
        ));

        recipes.add(new Recipe(
                "Simple Fruit Bowl",
                Arrays.asList("banana", "apple"),
                "1. Slice the banana.\n" +
                        "2. Chop the apple.\n" +
                        "3. Combine the fruit in a bowl and serve."
        ));

        recipes.add(new Recipe(
                "Egg Sandwich",
                Arrays.asList("bread", "eggs"),
                "1. Cook the eggs.\n" +
                        "2. Place the cooked eggs between slices of bread.\n" +
                        "3. Serve immediately."
        ));
    }

    private void showMatchingRecipes() {

        recipeContainer.removeAllViews();

        List<PantryItem> pantryItems =
                pantryDao.getAllPantryItems();

        Set<String> pantryIngredientNames =
                new HashSet<>();

        for (PantryItem pantryItem : pantryItems) {

            pantryIngredientNames.add(
                    pantryItem
                            .getName()
                            .trim()
                            .toLowerCase()
            );
        }

        int matchingRecipeCount = 0;

        for (Recipe recipe : recipes) {

            if (pantryIngredientNames.containsAll(
                    recipe.getIngredients()
            )) {

                addRecipeToScreen(recipe);
                matchingRecipeCount++;
            }
        }

        if (matchingRecipeCount == 0) {
            txtNoRecipes.setVisibility(View.VISIBLE);
        } else {
            txtNoRecipes.setVisibility(View.GONE);
        }
    }

    private void addRecipeToScreen(Recipe recipe) {

        LinearLayout recipeCard =
                new LinearLayout(this);

        recipeCard.setOrientation(
                LinearLayout.VERTICAL
        );

        int padding =
                (int) (16 * getResources()
                        .getDisplayMetrics()
                        .density);

        recipeCard.setPadding(
                padding,
                padding,
                padding,
                padding
        );

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

        cardParams.setMargins(
                0,
                0,
                0,
                padding
        );

        recipeCard.setLayoutParams(cardParams);

        recipeCard.setBackgroundColor(
                getColor(android.R.color.white)
        );

        TextView recipeName =
                new TextView(this);

        recipeName.setText(recipe.getName());
        recipeName.setTextSize(18);

        recipeName.setTextColor(
                getColor(android.R.color.black)
        );

        recipeName.setTypeface(
                null,
                android.graphics.Typeface.BOLD
        );

        TextView ingredientText =
                new TextView(this);

        ingredientText.setText(
                "Ingredients: "
                        + String.join(
                        ", ",
                        recipe.getIngredients()
                )
        );

        ingredientText.setTextSize(14);

        ingredientText.setPadding(
                0,
                padding / 2,
                0,
                padding / 2
        );

        TextView tapText =
                new TextView(this);

        tapText.setText("Tap to view recipe");
        tapText.setTextSize(13);

        recipeCard.addView(recipeName);
        recipeCard.addView(ingredientText);
        recipeCard.addView(tapText);

        recipeCard.setOnClickListener(view ->
                openRecipeDetails(recipe)
        );

        recipeContainer.addView(recipeCard);
    }

    private void openRecipeDetails(Recipe recipe) {

        Intent intent = new Intent(
                RecipesActivity.this,
                RecipeDetailActivity.class
        );

        intent.putExtra(
                "recipe_name",
                recipe.getName()
        );

        StringBuilder ingredients =
                new StringBuilder();

        for (String ingredient : recipe.getIngredients()) {
            ingredients
                    .append("• ")
                    .append(capitalize(ingredient))
                    .append("\n");
        }

        intent.putExtra(
                "recipe_ingredients",
                ingredients.toString()
        );

        intent.putExtra(
                "recipe_instructions",
                recipe.getInstructions()
        );

        startActivity(intent);
    }

    private String capitalize(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        return text.substring(0, 1).toUpperCase()
                + text.substring(1);
    }

    private static class Recipe {

        private final String name;
        private final List<String> ingredients;
        private final String instructions;

        public Recipe(
                String name,
                List<String> ingredients,
                String instructions
        ) {
            this.name = name;
            this.ingredients = ingredients;
            this.instructions = instructions;
        }

        public String getName() {
            return name;
        }

        public List<String> getIngredients() {
            return ingredients;
        }

        public String getInstructions() {
            return instructions;
        }
    }
}