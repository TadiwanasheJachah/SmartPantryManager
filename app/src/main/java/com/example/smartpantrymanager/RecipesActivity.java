package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipesActivity extends AppCompatActivity {

    private LinearLayout recipeContainer;
    private TextView txtNoRecipes;
    private Button btnBackToPantry;
    private BottomNavigationView bottomNavigation;

    private PantryDao pantryDao;
    private RecipeDao recipeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        recipeContainer = findViewById(R.id.recipeContainer);
        txtNoRecipes = findViewById(R.id.txtNoRecipes);
        btnBackToPantry = findViewById(R.id.btnBackToPantry);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        AppDatabase database =
                AppDatabase.getInstance(this);

        pantryDao = database.pantryDao();
        recipeDao = database.recipeDao();

        setupBottomNavigation();
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

    @Override
    protected void onResume() {
        super.onResume();

        if (pantryDao != null && recipeDao != null) {
            showMatchingRecipes();
        }

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.navRecipes);
        }
    }

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(R.id.navRecipes);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {

                Intent intent = new Intent(
                        RecipesActivity.this,
                        MainActivity.class
                );

                startActivity(intent);
                finish();

                return true;
            }

            if (itemId == R.id.navPantry) {

                Intent intent = new Intent(
                        RecipesActivity.this,
                        PantryActivity.class
                );

                startActivity(intent);
                finish();

                return true;
            }

            if (itemId == R.id.navRecipes) {
                return true;
            }

            if (itemId == R.id.navSettings) {

                Intent intent = new Intent(
                        RecipesActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);
                finish();

                return true;
            }

            return false;
        });
    }

    private void showMatchingRecipes() {

        recipeContainer.removeAllViews();

        List<PantryItem> pantryItems =
                pantryDao.getAllPantryItems();

        List<RecipeEntity> recipes =
                recipeDao.getAllRecipes();

        Map<String, PantryItem> pantryMap =
                new HashMap<>();

        for (PantryItem pantryItem : pantryItems) {

            String normalizedName =
                    normalizeIngredient(
                            pantryItem.getName()
                    );

            pantryMap.put(
                    normalizedName,
                    pantryItem
            );
        }

        int matchingRecipeCount = 0;

        for (RecipeEntity recipe : recipes) {

            if (hasRequiredIngredients(
                    pantryMap,
                    recipe.getRequirements()
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

    private boolean hasRequiredIngredients(
            Map<String, PantryItem> pantryMap,
            String requirements
    ) {

        String[] requirementList =
                requirements.split(";");

        for (String requirement : requirementList) {

            String[] parts =
                    requirement.split("\\|");

            if (parts.length != 3) {
                return false;
            }

            String requiredIngredient =
                    normalizeIngredient(parts[0]);

            double requiredQuantity;

            try {
                requiredQuantity =
                        Double.parseDouble(parts[1]);
            } catch (NumberFormatException e) {
                return false;
            }

            String requiredUnit =
                    parts[2].trim();

            PantryItem pantryItem =
                    pantryMap.get(requiredIngredient);

            if (pantryItem == null) {
                return false;
            }

            if (!unitsAreCompatible(
                    pantryItem.getUnit(),
                    requiredUnit
            )) {
                return false;
            }

            double availableQuantity =
                    convertQuantity(
                            pantryItem.getQuantity(),
                            pantryItem.getUnit(),
                            requiredUnit
                    );

            if (availableQuantity < requiredQuantity) {
                return false;
            }
        }

        return true;
    }

    private boolean unitsAreCompatible(
            String pantryUnit,
            String requiredUnit
    ) {

        String pantry =
                pantryUnit.trim().toLowerCase();

        String required =
                requiredUnit.trim().toLowerCase();

        if (pantry.equals(required)) {
            return true;
        }

        if ((pantry.equals("kg") && required.equals("g"))
                || (pantry.equals("g") && required.equals("kg"))) {
            return true;
        }

        if ((pantry.equals("l") && required.equals("ml"))
                || (pantry.equals("ml") && required.equals("l"))) {
            return true;
        }

        return false;
    }

    private double convertQuantity(
            double quantity,
            String fromUnit,
            String toUnit
    ) {

        String from =
                fromUnit.trim().toLowerCase();

        String to =
                toUnit.trim().toLowerCase();

        if (from.equals(to)) {
            return quantity;
        }

        if (from.equals("kg") && to.equals("g")) {
            return quantity * 1000;
        }

        if (from.equals("g") && to.equals("kg")) {
            return quantity / 1000;
        }

        if (from.equals("l") && to.equals("ml")) {
            return quantity * 1000;
        }

        if (from.equals("ml") && to.equals("l")) {
            return quantity / 1000;
        }

        return quantity;
    }

    private String normalizeIngredient(String ingredient) {

        if (ingredient == null) {
            return "";
        }

        String normalized =
                ingredient
                        .trim()
                        .toLowerCase();

        if (normalized.equals("eggs")) {
            return "egg";
        }

        if (normalized.equals("tomatoes")) {
            return "tomato";
        }

        if (normalized.equals("potatoes")) {
            return "potato";
        }

        if (normalized.equals("bananas")) {
            return "banana";
        }

        if (normalized.equals("apples")) {
            return "apple";
        }

        return normalized;
    }

    private void addRecipeToScreen(RecipeEntity recipe) {

        View recipeCard = LayoutInflater.from(this)
                .inflate(R.layout.item_recipe, recipeContainer, false);

        ImageView recipeImage =
                recipeCard.findViewById(R.id.imgRecipe);

        TextView recipeName =
                recipeCard.findViewById(R.id.tvRecipeCardName);

        TextView ingredientText =
                recipeCard.findViewById(R.id.tvRecipeCardIngredients);

        TextView actionText =
                recipeCard.findViewById(R.id.tvRecipeCardAction);

        recipeImage.setImageResource(
                getRecipeImage(recipe.getName())
        );

        recipeName.setText(recipe.getName());

        ingredientText.setText(
                formatRequirements(recipe.getRequirements())
        );

        actionText.setText("View recipe  >");

        recipeCard.setOnClickListener(
                view -> openRecipeDetails(recipe)
        );

        recipeContainer.addView(recipeCard);
    }
    private String formatRequirements(
            String requirements
    ) {

        StringBuilder builder =
                new StringBuilder(
                        "Ingredients: "
                );

        String[] requirementList =
                requirements.split(";");

        for (int i = 0;
             i < requirementList.length;
             i++) {

            String[] parts =
                    requirementList[i]
                            .split("\\|");

            if (parts.length == 3) {

                builder.append(
                        capitalize(parts[0])
                );

                builder.append(" ");
                builder.append(parts[1]);
                builder.append(" ");
                builder.append(parts[2]);

                if (i < requirementList.length - 1) {
                    builder.append(", ");
                }
            }
        }

        return builder.toString();
    }

    private void openRecipeDetails(
            RecipeEntity recipe
    ) {

        Intent intent = new Intent(
                RecipesActivity.this,
                RecipeDetailActivity.class
        );

        intent.putExtra(
                "recipe_name",
                recipe.getName()
        );

        intent.putExtra(
                "recipe_ingredients",
                formatRequirementsForDetails(
                        recipe.getRequirements()
                )
        );

        intent.putExtra(
                "recipe_instructions",
                recipe.getInstructions()
        );

        intent.putExtra(
                "recipe_image",
                getRecipeImage(recipe.getName())
        );

        startActivity(intent);
    }

    private String formatRequirementsForDetails(
            String requirements
    ) {

        StringBuilder builder =
                new StringBuilder();

        String[] requirementList =
                requirements.split(";");

        for (String requirement : requirementList) {

            String[] parts =
                    requirement.split("\\|");

            if (parts.length == 3) {

                builder
                        .append("• ")
                        .append(
                                capitalize(parts[0])
                        )
                        .append(" - ")
                        .append(parts[1])
                        .append(" ")
                        .append(parts[2])
                        .append("\n");
            }
        }

        return builder.toString();
    }

    private int getRecipeImage(String recipeName) {

        if (recipeName == null) {
            return R.drawable.recipe_placeholder;
        }

        switch (recipeName) {

            case "Scrambled Eggs":
                return R.drawable.recipe_scrambled_eggs;

            case "Cheese Omelette":
                return R.drawable.recipe_cheese_omelette;

            case "French Toast":
                return R.drawable.recipe_french_toast;

            case "Cheese Toast":
                return R.drawable.recipe_cheese_toast;

            case "Tomato Sandwich":
                return R.drawable.recipe_tomato_sandwich;

            case "Chicken and Rice":
                return R.drawable.recipe_chicken_rice;

            case "Tomato Rice":
                return R.drawable.recipe_tomato_rice;

            case "Chicken Pasta":
                return R.drawable.recipe_chicken_pasta;

            case "Cheesy Pasta":
                return R.drawable.recipe_cheesy_pasta;

            case "Banana Oats":
                return R.drawable.recipe_banana_oats;

            case "Banana Toast":
                return R.drawable.recipe_banana_toast;

            case "Potato and Egg Breakfast":
                return R.drawable.recipe_potato_egg_breakfast;

            case "Chicken and Potato Meal":
                return R.drawable.recipe_chicken_potato;

            case "Simple Fruit Bowl":
                return R.drawable.recipe_fruit_bowl;

            case "Egg Sandwich":
                return R.drawable.recipe_egg_sandwich;

            case "Apple Oats":
                return R.drawable.recipe_apple_oats;

            case "Chicken Cheese Toast":
                return R.drawable.recipe_chicken_cheese_toast;

            case "Potato Cheese Bake":
                return R.drawable.recipe_potato_cheese_bake;

            default:
                return R.drawable.recipe_placeholder;
        }
    }
    private String capitalize(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        return text.substring(0, 1)
                .toUpperCase()
                + text.substring(1);
    }
}