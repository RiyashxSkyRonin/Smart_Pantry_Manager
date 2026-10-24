
package com.example.smart_pantry_manager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "SmartPantrySettings";
    private static final String EXPIRY_ALERTS = "expiryAlertsEnabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Switch switchExpiryAlerts =
                findViewById(R.id.switchExpiryAlerts);

        Button btnBack =
                findViewById(R.id.btnBackSettings);

        SharedPreferences preferences =
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        boolean alertsEnabled =
                preferences.getBoolean(EXPIRY_ALERTS, true);

        switchExpiryAlerts.setChecked(alertsEnabled);

        switchExpiryAlerts.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    preferences.edit()
                            .putBoolean(EXPIRY_ALERTS, isChecked)
                            .apply();
                }
        );

        btnBack.setOnClickListener(v -> finish());
    }
}
