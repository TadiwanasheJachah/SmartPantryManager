package com.example.smartpantrymanager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiryReminders;
    private Switch switchStrictMatching;
    private Button btnSaveSettings;
    private Button btnBackHome;

    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "SmartPantrySettings";
    private static final String KEY_EXPIRY_REMINDERS = "expiryReminders";
    private static final String KEY_STRICT_MATCHING = "strictMatching";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Connect Java variables to XML views
        switchExpiryReminders =
                findViewById(R.id.switchExpiryReminders);

        switchStrictMatching =
                findViewById(R.id.switchStrictMatching);

        btnSaveSettings =
                findViewById(R.id.btnSaveSettings);

        btnBackHome =
                findViewById(R.id.btnBackHome);

        // Open the app's saved preferences
        sharedPreferences = getSharedPreferences(
                PREFS_NAME,
                MODE_PRIVATE
        );

        // Load previously saved settings
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

        // Save button
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

        // Return to the previous screen
        btnBackHome.setOnClickListener(view -> finish());
    }
}