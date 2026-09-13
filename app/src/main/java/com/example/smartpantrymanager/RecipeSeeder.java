package com.example.smartpantrymanager;

import java.util.ArrayList;
import java.util.List;

public class RecipeSeeder {

    public static void seedRecipes(AppDatabase database) {

        RecipeDao recipeDao = database.recipeDao();

        if (recipeDao.getRecipeCount() > 0) {
            return;
        }

        List<RecipeEntity> recipes = new ArrayList<>();

        recipes.add(new RecipeEntity(
                "Scrambled Eggs",
                "egg|2|pieces;milk|100|ml",
                "1. Crack the eggs into a bowl.\n" +
                        "2. Add the milk and whisk together.\n" +
                        "3. Pour the mixture into a heated pan.\n" +
                        "4. Stir gently until the eggs are cooked."
        ));

        recipes.add(new RecipeEntity(
                "Cheese Omelette",
                "egg|2|pieces;cheese|50|g",
                "1. Beat the eggs in a bowl.\n" +
                        "2. Pour them into a heated pan.\n" +
                        "3. Add cheese on top.\n" +
                        "4. Fold the omelette and cook until the cheese melts."
        ));

        recipes.add(new RecipeEntity(
                "French Toast",
                "bread|2|pieces;egg|1|pieces;milk|100|ml",
                "1. Beat the egg and milk together.\n" +
                        "2. Dip the bread into the mixture.\n" +
                        "3. Cook both sides in a heated pan until golden."
        ));

        recipes.add(new RecipeEntity(
                "Cheese Toast",
                "bread|2|pieces;cheese|50|g",
                "1. Place cheese on the bread.\n" +
                        "2. Toast until the bread is crisp and the cheese has melted."
        ));

        recipes.add(new RecipeEntity(
                "Tomato Sandwich",
                "bread|2|pieces;tomato|1|pieces",
                "1. Slice the tomato.\n" +
                        "2. Place the tomato slices between the bread.\n" +
                        "3. Serve immediately."
        ));

        recipes.add(new RecipeEntity(
                "Chicken and Rice",
                "chicken|200|g;rice|150|g",
                "1. Cook the rice until tender.\n" +
                        "2. Cook the chicken thoroughly.\n" +
                        "3. Serve together."
        ));

        recipes.add(new RecipeEntity(
                "Tomato Rice",
                "rice|150|g;tomato|1|pieces",
                "1. Cook the rice.\n" +
                        "2. Chop and cook the tomato until soft.\n" +
                        "3. Mix the tomato with the rice."
        ));

        recipes.add(new RecipeEntity(
                "Chicken Pasta",
                "chicken|200|g;pasta|150|g;tomato|1|pieces",
                "1. Cook the pasta.\n" +
                        "2. Cook the chicken thoroughly.\n" +
                        "3. Add chopped tomato.\n" +
                        "4. Combine everything and serve."
        ));

        recipes.add(new RecipeEntity(
                "Cheesy Pasta",
                "pasta|150|g;cheese|60|g;milk|100|ml",
                "1. Cook the pasta.\n" +
                        "2. Heat the milk.\n" +
                        "3. Add cheese and stir until melted.\n" +
                        "4. Mix the sauce with the pasta."
        ));

        recipes.add(new RecipeEntity(
                "Banana Oats",
                "banana|1|pieces;oats|80|g;milk|200|ml",
                "1. Cook the oats with milk.\n" +
                        "2. Slice the banana.\n" +
                        "3. Add the banana and serve."
        ));

        recipes.add(new RecipeEntity(
                "Banana Toast",
                "banana|1|pieces;bread|2|pieces",
                "1. Toast the bread.\n" +
                        "2. Slice or mash the banana.\n" +
                        "3. Place the banana on top."
        ));

        recipes.add(new RecipeEntity(
                "Potato and Egg Breakfast",
                "potato|2|pieces;egg|2|pieces",
                "1. Cut the potatoes into small pieces.\n" +
                        "2. Cook until tender.\n" +
                        "3. Add beaten eggs.\n" +
                        "4. Cook until done."
        ));

        recipes.add(new RecipeEntity(
                "Chicken and Potato Meal",
                "chicken|200|g;potato|2|pieces",
                "1. Cut and cook the potatoes until tender.\n" +
                        "2. Cook the chicken thoroughly.\n" +
                        "3. Serve together."
        ));

        recipes.add(new RecipeEntity(
                "Simple Fruit Bowl",
                "banana|1|pieces;apple|1|pieces",
                "1. Slice the banana.\n" +
                        "2. Chop the apple.\n" +
                        "3. Combine the fruit in a bowl."
        ));

        recipes.add(new RecipeEntity(
                "Egg Sandwich",
                "bread|2|pieces;egg|2|pieces",
                "1. Cook the eggs.\n" +
                        "2. Place the eggs between slices of bread.\n" +
                        "3. Serve immediately."
        ));

        recipes.add(new RecipeEntity(
                "Apple Oats",
                "apple|1|pieces;oats|80|g;milk|200|ml",
                "1. Cook the oats with milk.\n" +
                        "2. Chop the apple.\n" +
                        "3. Add the apple to the cooked oats."
        ));

        recipes.add(new RecipeEntity(
                "Chicken Cheese Toast",
                "chicken|100|g;bread|2|pieces;cheese|50|g",
                "1. Cook the chicken thoroughly.\n" +
                        "2. Place chicken and cheese on the bread.\n" +
                        "3. Toast until the cheese melts."
        ));

        recipes.add(new RecipeEntity(
                "Potato Cheese Bake",
                "potato|2|pieces;cheese|60|g",
                "1. Cook the potatoes until nearly tender.\n" +
                        "2. Add cheese on top.\n" +
                        "3. Bake or heat until the cheese melts."
        ));

        recipeDao.insertAll(recipes);
    }
}
