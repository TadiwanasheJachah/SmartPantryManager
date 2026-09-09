package com.example.smartpantrymanager;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class PantryActivity extends AppCompatActivity {

    private EditText etIngredientName;
    private EditText etQuantity;
    private EditText etExpiryDate;
    private Spinner spUnit;
    private Button btnAddIngredient;
    private TextView tvEmptyPantry;

    private final ArrayList<String> pantryItems = new ArrayList<>();

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

        String[] units = {"Choose unit", "kg", "g", "L", "ml", "pieces"};

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                units
        );

        unitAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spUnit.setAdapter(unitAdapter);

        btnAddIngredient.setOnClickListener(view -> addIngredient());
    }

    private void addIngredient() {

        String ingredientName =
                etIngredientName.getText().toString().trim();

        String quantity =
                etQuantity.getText().toString().trim();

        String unit =
                spUnit.getSelectedItem().toString();

        String expiryDate =
                etExpiryDate.getText().toString().trim();

        if (ingredientName.isEmpty()) {
            etIngredientName.setError("Ingredient name is required");
            return;
        }

        if (quantity.isEmpty()) {
            etQuantity.setError("Quantity is required");
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

        String pantryItem =
                ingredientName + " - " + quantity + " " + unit;

        if (!expiryDate.isEmpty()) {
            pantryItem += " - Expires: " + expiryDate;
        }

        pantryItems.add(pantryItem);

        tvEmptyPantry.setText(buildPantryList());

        etIngredientName.setText("");
        etQuantity.setText("");
        etExpiryDate.setText("");
        spUnit.setSelection(0);

        Toast.makeText(
                this,
                "Ingredient added",
                Toast.LENGTH_SHORT
        ).show();
    }

    private String buildPantryList() {

        StringBuilder list = new StringBuilder();

        for (String item : pantryItems) {
            list.append("• ")
                    .append(item)
                    .append("\n\n");
        }

        return list.toString();
    }
}