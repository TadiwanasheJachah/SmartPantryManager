package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private Button btnPantry;
    private Button btnRecipes;
    private Button btnSettings;

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main),
                (view, windowInsets) -> {

                    Insets systemBars = windowInsets.getInsets(
                            WindowInsetsCompat.Type.systemBars()
                    );

                    view.setPadding(
                            systemBars.left,
                            systemBars.top,
                            systemBars.right,
                            systemBars.bottom
                    );

                    return windowInsets;
                }
        );

        AppDatabase database =
                AppDatabase.getInstance(this);

        RecipeSeeder.seedRecipes(database);

        btnPantry = findViewById(R.id.btnPantry);
        btnRecipes = findViewById(R.id.btnRecipes);
        btnSettings = findViewById(R.id.btnSettings);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Highlight Home because this is the Home screen
        bottomNavigation.setSelectedItemId(R.id.navHome);

        btnPantry.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    PantryActivity.class
            );

            startActivity(intent);
        });

        btnRecipes.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    RecipesActivity.class
            );

            startActivity(intent);
        });

        btnSettings.setOnClickListener(view -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    SettingsActivity.class
            );

            startActivity(intent);
        });

        bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {
                return true;
            }

            if (itemId == R.id.navPantry) {

                Intent intent = new Intent(
                        MainActivity.this,
                        PantryActivity.class
                );

                startActivity(intent);
                return true;
            }

            if (itemId == R.id.navRecipes) {

                Intent intent = new Intent(
                        MainActivity.this,
                        RecipesActivity.class
                );

                startActivity(intent);
                return true;
            }

            if (itemId == R.id.navSettings) {

                Intent intent = new Intent(
                        MainActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);
                return true;
            }

            return false;
        });
    }
}