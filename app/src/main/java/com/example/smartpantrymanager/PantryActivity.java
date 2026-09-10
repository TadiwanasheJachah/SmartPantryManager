package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private EditText etExpiryDate;
    private Spinner spUnit;
    private Button btnAddIngredient;
    private TextView tvEmptyPantry;
    private RecyclerView recyclerPantry;

    private AppDatabase database;
    private PantryDao pantryDao;
    private PantryAdapter pantryAdapter;

    // If this is null, we are adding a new ingredient.
    // If it contains an item, we are editing that ingredient.
    private PantryItem itemBeingEdited = null;

    private final String[] units = {
            "Choose unit",
            "kg",
            "g",
            "L",
            "ml",
            "pieces"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        spUnit = findViewById(R.id.spUnit);
        btnAddIngredient = findViewById(R.id.btnAddIngredient);
        tvEmptyPantry = findViewById(R.id.tvEmptyPantry);
        recyclerPantry = findViewById(R.id.recyclerPantry);

        database = AppDatabase.getInstance(this);
        pantryDao = database.pantryDao();

        setupUnitSpinner();
        setupRecyclerView();
        loadPantryItems();

        btnAddIngredient.setOnClickListener(view -> saveIngredient());
    }

    private void setupUnitSpinner() {

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spUnit.setAdapter(unitAdapter);
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
                        startEditing(item);
                    }

                    @Override
                    public void onDelete(PantryItem item) {
                        confirmDelete(item);
                    }
                }
        );

        recyclerPantry.setAdapter(pantryAdapter);
    }

    private void saveIngredient() {

        String ingredientName =
                etIngredientName.getText().toString().trim();

        String quantityText =
                etQuantity.getText().toString().trim();

        String unit =
                spUnit.getSelectedItem().toString();

        String expiryDate =
                etExpiryDate.getText().toString().trim();

        if (ingredientName.isEmpty()) {
            etIngredientName.setError("Ingredient name is required");
            etIngredientName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {
            etQuantity.setError("Quantity is required");
            etQuantity.requestFocus();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            etQuantity.setError("Enter a valid quantity");
            etQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {
            etQuantity.setError("Quantity must be greater than zero");
            etQuantity.requestFocus();
            return;
        }

        if (unit.equals("Choose unit")) {
            Toast.makeText(
                    this,
                    "Please choose a unit",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        if (itemBeingEdited == null) {

            // CREATE
            PantryItem newItem = new PantryItem(
                    ingredientName,
                    quantity,
                    unit,
                    expiryDate
            );

            pantryDao.insert(newItem);

            Toast.makeText(
                    this,
                    "Ingredient saved",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            // UPDATE
            itemBeingEdited.setName(ingredientName);
            itemBeingEdited.setQuantity(quantity);
            itemBeingEdited.setUnit(unit);
            itemBeingEdited.setExpiryDate(expiryDate);

            pantryDao.update(itemBeingEdited);

            Toast.makeText(
                    this,
                    "Ingredient updated",
                    Toast.LENGTH_SHORT
            ).show();

            itemBeingEdited = null;
            btnAddIngredient.setText("Add Ingredient");
        }

        clearForm();
        loadPantryItems();
    }

    private void startEditing(PantryItem item) {

        itemBeingEdited = item;

        etIngredientName.setText(item.getName());

        if (item.getQuantity() == (long) item.getQuantity()) {
            etQuantity.setText(
                    String.valueOf((long) item.getQuantity())
            );
        } else {
            etQuantity.setText(
                    String.valueOf(item.getQuantity())
            );
        }

        etExpiryDate.setText(item.getExpiryDate());

        for (int i = 0; i < units.length; i++) {
            if (units[i].equals(item.getUnit())) {
                spUnit.setSelection(i);
                break;
            }
        }

        btnAddIngredient.setText("Update Ingredient");

        etIngredientName.requestFocus();

        Toast.makeText(
                this,
                "Edit the ingredient and tap Update Ingredient",
                Toast.LENGTH_SHORT
        ).show();
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
                .setPositiveButton("Delete", (dialog, which) -> {

                    pantryDao.delete(item);

                    // If the item currently being edited is deleted,
                    // return the form to Add mode.
                    if (itemBeingEdited != null
                            && itemBeingEdited.getId() == item.getId()) {

                        itemBeingEdited = null;
                        clearForm();
                        btnAddIngredient.setText("Add Ingredient");
                    }

                    Toast.makeText(
                            PantryActivity.this,
                            "Ingredient deleted",
                            Toast.LENGTH_SHORT
                    ).show();

                    loadPantryItems();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void clearForm() {

        etIngredientName.setText("");
        etQuantity.setText("");
        etExpiryDate.setText("");
        spUnit.setSelection(0);
    }
}