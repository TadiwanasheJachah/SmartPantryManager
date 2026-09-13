package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipesActivity extends AppCompatActivity {

    private LinearLayout recipeContainer;
    private TextView txtNoRecipes;
    private Button btnBackToPantry;

    private PantryDao pantryDao;
    private RecipeDao recipeDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes);

        recipeContainer = findViewById(R.id.recipeContainer);
        txtNoRecipes = findViewById(R.id.txtNoRecipes);
        btnBackToPantry = findViewById(R.id.btnBackToPantry);

        AppDatabase database =
                AppDatabase.getInstance(this);

        pantryDao = database.pantryDao();
        recipeDao = database.recipeDao();

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

    private void addRecipeToScreen(
            RecipeEntity recipe
    ) {

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
                formatRequirements(
                        recipe.getRequirements()
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

    private String capitalize(String text) {

        if (text == null || text.isEmpty()) {
            return text;
        }

        return text.substring(0, 1)
                .toUpperCase()
                + text.substring(1);
    }
}