package com.example.smartpantrymanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryReminders;
    private Switch switchStrictMatching;
    private Button btnSaveSettings;
    private Button btnBackHome;
    private BottomNavigationView bottomNavigation;

    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "SmartPantrySettings";
    private static final String KEY_EXPIRY_REMINDERS = "expiryReminders";
    private static final String KEY_STRICT_MATCHING = "strictMatching";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        switchExpiryReminders =
                findViewById(R.id.switchExpiryReminders);

        switchStrictMatching =
                findViewById(R.id.switchStrictMatching);

        btnSaveSettings =
                findViewById(R.id.btnSaveSettings);

        btnBackHome =
                findViewById(R.id.btnBackHome);

        bottomNavigation =
                findViewById(R.id.bottomNavigation);

        sharedPreferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        boolean expiryRemindersEnabled =
                sharedPreferences.getBoolean(
                        KEY_EXPIRY_REMINDERS,
                        false
                );

        boolean strictMatchingEnabled =
                sharedPreferences.getBoolean(
                        KEY_STRICT_MATCHING,
                        true
                );

        switchExpiryReminders.setChecked(
                expiryRemindersEnabled
        );

        switchStrictMatching.setChecked(
                strictMatchingEnabled
        );

        setupBottomNavigation();

        btnSaveSettings.setOnClickListener(view -> {

            SharedPreferences.Editor editor =
                    sharedPreferences.edit();

            editor.putBoolean(
                    KEY_EXPIRY_REMINDERS,
                    switchExpiryReminders.isChecked()
            );

            editor.putBoolean(
                    KEY_STRICT_MATCHING,
                    switchStrictMatching.isChecked()
            );

            editor.apply();

            Toast.makeText(
                    SettingsActivity.this,
                    "Settings saved successfully",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnBackHome.setOnClickListener(view -> {

            Intent intent = new Intent(
                    SettingsActivity.this,
                    MainActivity.class
            );

            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(
                    R.id.navSettings
            );
        }
    }

    private void setupBottomNavigation() {

        bottomNavigation.setSelectedItemId(
                R.id.navSettings
        );

        bottomNavigation.setOnItemSelectedListener(item -> {

            int itemId = item.getItemId();

            if (itemId == R.id.navHome) {

                Intent intent = new Intent(
                        SettingsActivity.this,
                        MainActivity.class
                );

                startActivity(intent);
                finish();

                return true;
            }

            if (itemId == R.id.navPantry) {

                Intent intent = new Intent(
                        SettingsActivity.this,
                        PantryActivity.class
                );

                startActivity(intent);
                finish();

                return true;
            }

            if (itemId == R.id.navRecipes) {

                Intent intent = new Intent(
                        SettingsActivity.this,
                        RecipesActivity.class
                );

                startActivity(intent);
                finish();

                return true;
            }

            if (itemId == R.id.navSettings) {
                return true;
            }

            return false;
        });
    }
}