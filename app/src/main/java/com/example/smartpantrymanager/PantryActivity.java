package com.example.smartpantrymanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private Button btnAddIngredient;
    private TextView tvEmptyPantry;
    private RecyclerView recyclerPantry;
    private BottomNavigationView bottomNavigation;

    private PantryDao pantryDao;
    private PantryAdapter pantryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);
        recyclerPantry = findViewById(R.id.recyclerPantry);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        AppDatabase database = AppDatabase.getInstance(this);
        pantryDao = database.pantryDao();

        setupRecyclerView();
        setupBottomNavigation();
        loadPantryItems();

        btnAddIngredient.setOnClickListener(view -> {
            Intent intent = new Intent(
                    PantryActivity.this,
                    AddEditIngredientActivity.class
            );
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (pantryDao != null && pantryAdapter != null) {
            loadPantryItems();
        }

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.navPantry);
        }
    }

    private void setupRecyclerView() {

        recyclerPantry.setLayoutManager(
                new LinearLayoutManager(this)
        );

        pantryAdapter = new PantryAdapter(
                pantryDao.getAllPantryItems(),
                new PantryAdapter.OnPantryItemActionListener() {

                    @Override
                    public void onEdit(PantryItem item) {

                        Intent intent = new Intent(
                                PantryActivity.this,
                                AddEditIngredientActivity.class
                        );

                        intent.putExtra(
                                "ingredient_id",
                                item.getId()
                        );

                        startActivity(intent);
                    }

                    @Override
                    public void onDelete(PantryItem item) {
                        confirmDelete(item);
                    }
                }
        );

        recyclerPantry.setAdapter(pantryAdapter);
    }

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(R.id.navPantry);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {

                Intent intent = new Intent(
                        PantryActivity.this,
                        MainActivity.class
                );

                startActivity(intent);
                finish();
                return true;
            }

            if (itemId == R.id.navPantry) {
                return true;
            }

            if (itemId == R.id.navRecipes) {

                Intent intent = new Intent(
                        PantryActivity.this,
                        RecipesActivity.class
                );

                startActivity(intent);
                finish();
                return true;
            }

            if (itemId == R.id.navSettings) {

                Intent intent = new Intent(
                        PantryActivity.this,
                        SettingsActivity.class
                );

                startActivity(intent);
                finish();
                return true;
            }

            return false;
        });
    }

    private void loadPantryItems() {

        List<PantryItem> pantryItems =
                pantryDao.getAllPantryItems();

        pantryAdapter.setPantryItems(pantryItems);

        if (pantryItems.isEmpty()) {
            tvEmptyPantry.setVisibility(View.VISIBLE);
            recyclerPantry.setVisibility(View.GONE);
        } else {
            tvEmptyPantry.setVisibility(View.GONE);
            recyclerPantry.setVisibility(View.VISIBLE);
        }
    }

    private void confirmDelete(PantryItem item) {

        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage(
                        "Are you sure you want to delete "
                                + item.getName()
                                + "?"
                )
                .setPositiveButton(
                        "Delete",
                        (dialog, which) -> {

                            pantryDao.delete(item);

                            Toast.makeText(
                                    PantryActivity.this,
                                    "Ingredient deleted",
                                    Toast.LENGTH_SHORT
                            ).show();

                            loadPantryItems();
                        }
                )
                .setNegativeButton("Cancel", null)
                .show();
    }
}
