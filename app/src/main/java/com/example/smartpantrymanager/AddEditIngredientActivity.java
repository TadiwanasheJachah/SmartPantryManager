package com.example.smartpantrymanager;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddEditIngredientActivity extends AppCompatActivity {

    private TextView tvIngredientFormTitle;
    private TextView tvIngredientFormSubtitle;
    private EditText etIngredientName;
    private EditText etQuantity;
    private EditText etExpiryDate;
    private Spinner spUnit;
    private Button btnSaveIngredient;
    private Button btnCancel;

    private PantryDao pantryDao;

    private int ingredientId = -1;

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
        setContentView(R.layout.activity_add_edit_ingredient);

        tvIngredientFormTitle = findViewById(R.id.tvIngredientFormTitle);
        tvIngredientFormSubtitle = findViewById(R.id.tvIngredientFormSubtitle);
        etIngredientName = findViewById(R.id.etIngredientName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiryDate = findViewById(R.id.etExpiryDate);
        spUnit = findViewById(R.id.spUnit);
        btnSaveIngredient = findViewById(R.id.btnSaveIngredient);
        btnCancel = findViewById(R.id.btnCancel);

        AppDatabase database =
                AppDatabase.getInstance(this);

        pantryDao = database.pantryDao();

        setupUnitSpinner();
        setupExpiryDatePicker();

        ingredientId =
                getIntent().getIntExtra(
                        "ingredient_id",
                        -1
                );

        if (ingredientId != -1) {
            loadIngredientForEditing();
            tvIngredientFormTitle.setText("Edit Ingredient");
            tvIngredientFormSubtitle.setText("Update the details for this pantry item.");
            btnSaveIngredient.setText("Update Ingredient");
        }

        btnSaveIngredient.setOnClickListener(view ->
                saveIngredient()
        );

        btnCancel.setOnClickListener(view ->
                finish()
        );
    }

    private void setupUnitSpinner() {

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        units
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spUnit.setAdapter(adapter);
    }

    private void setupExpiryDatePicker() {

        etExpiryDate.setFocusable(false);
        etExpiryDate.setClickable(true);

        etExpiryDate.setOnClickListener(view -> {

            Calendar calendar =
                    Calendar.getInstance();

            int year =
                    calendar.get(Calendar.YEAR);

            int month =
                    calendar.get(Calendar.MONTH);

            int day =
                    calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog =
                    new DatePickerDialog(
                            AddEditIngredientActivity.this,
                            (datePicker,
                             selectedYear,
                             selectedMonth,
                             selectedDay) -> {

                                String selectedDate =
                                        selectedDay
                                                + "/"
                                                + (selectedMonth + 1)
                                                + "/"
                                                + selectedYear;

                                etExpiryDate.setText(
                                        selectedDate
                                );
                            },
                            year,
                            month,
                            day
                    );

            dialog.show();
        });
    }

    private void loadIngredientForEditing() {

        PantryItem item =
                pantryDao.getPantryItemById(
                        ingredientId
                );

        if (item == null) {
            Toast.makeText(
                    this,
                    "Ingredient could not be found",
                    Toast.LENGTH_SHORT
            ).show();

            finish();
            return;
        }

        etIngredientName.setText(
                item.getName()
        );

        if (item.getQuantity()
                == (long) item.getQuantity()) {

            etQuantity.setText(
                    String.valueOf(
                            (long) item.getQuantity()
                    )
            );

        } else {

            etQuantity.setText(
                    String.valueOf(
                            item.getQuantity()
                    )
            );
        }

        etExpiryDate.setText(
                item.getExpiryDate()
        );

        for (int i = 0;
             i < units.length;
             i++) {

            if (units[i].equals(
                    item.getUnit()
            )) {

                spUnit.setSelection(i);
                break;
            }
        }
    }

    private void saveIngredient() {

        String name =
                etIngredientName
                        .getText()
                        .toString()
                        .trim();

        String quantityText =
                etQuantity
                        .getText()
                        .toString()
                        .trim();

        String unit =
                spUnit
                        .getSelectedItem()
                        .toString();

        String expiryDate =
                etExpiryDate
                        .getText()
                        .toString()
                        .trim();

        if (name.isEmpty()) {

            etIngredientName.setError(
                    "Ingredient name is required"
            );

            etIngredientName.requestFocus();
            return;
        }

        if (quantityText.isEmpty()) {

            etQuantity.setError(
                    "Quantity is required"
            );

            etQuantity.requestFocus();
            return;
        }

        double quantity;

        try {

            quantity =
                    Double.parseDouble(
                            quantityText
                    );

        } catch (NumberFormatException e) {

            etQuantity.setError(
                    "Enter a valid quantity"
            );

            etQuantity.requestFocus();
            return;
        }

        if (quantity <= 0) {

            etQuantity.setError(
                    "Quantity must be greater than zero"
            );

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

        if (ingredientId == -1) {

            PantryItem newItem =
                    new PantryItem(
                            name,
                            quantity,
                            unit,
                            expiryDate
                    );

            pantryDao.insert(
                    newItem
            );

            Toast.makeText(
                    this,
                    "Ingredient saved",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            PantryItem item =
                    pantryDao.getPantryItemById(
                            ingredientId
                    );

            if (item == null) {
                return;
            }

            item.setName(name);
            item.setQuantity(quantity);
            item.setUnit(unit);
            item.setExpiryDate(expiryDate);

            pantryDao.update(item);

            Toast.makeText(
                    this,
                    "Ingredient updated",
                    Toast.LENGTH_SHORT
            ).show();
        }

        finish();
    }
}
