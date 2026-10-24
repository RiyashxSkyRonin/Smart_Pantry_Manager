
package com.example.smart_pantry_manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class IngredientAdapter extends ArrayAdapter<Ingredient> {

    public interface IngredientActionListener {
        void onEdit(Ingredient ingredient);
        void onDelete(Ingredient ingredient);
    }

    private final IngredientActionListener listener;

    public IngredientAdapter(Context context,
                             List<Ingredient> ingredients,
                             IngredientActionListener listener) {
        super(context, 0, ingredients);
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position,
                        @Nullable View convertView,
                        @NonNull ViewGroup parent) {

        Ingredient ingredient = getItem(position);

        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.VERTICAL);

        int padding = (int) (12 *
                getContext().getResources()
                        .getDisplayMetrics().density);

        row.setPadding(padding, padding, padding, padding);

        TextView ingredientText = new TextView(getContext());
        ingredientText.setTextSize(18);

        if (ingredient != null) {
            String text = ingredient.name + " — "
                    + ingredient.quantity + " " + ingredient.unit;

            if (ingredient.expiry != null
                    && !ingredient.expiry.isEmpty()) {
                text += "\nExpires: " + ingredient.expiry;
            }

            ingredientText.setText(text);
        }

        row.addView(ingredientText);

        // Read the saved Settings preference
        SharedPreferences preferences =
                getContext().getSharedPreferences(
                        "SmartPantrySettings",
                        Context.MODE_PRIVATE
                );

        boolean alertsEnabled =
                preferences.getBoolean(
                        "expiryAlertsEnabled",
                        true
                );

        // Show expiry warnings only when enabled
        if (alertsEnabled
                && ingredient != null
                && ingredient.expiry != null
                && !ingredient.expiry.trim().isEmpty()) {

            String warning = getExpiryWarning(ingredient.expiry);

            if (!warning.isEmpty()) {
                TextView warningText = new TextView(getContext());
                warningText.setText(warning);
                warningText.setTextSize(15);

                if (warning.equals("EXPIRED")) {
                    warningText.setTextColor(Color.RED);
                } else {
                    warningText.setTextColor(
                            Color.rgb(230, 140, 0));
                }

                row.addView(warningText);
            }
        }

        LinearLayout buttonRow = new LinearLayout(getContext());
        buttonRow.setOrientation(LinearLayout.HORIZONTAL);

        Button editButton = new Button(getContext());
        editButton.setText("Edit");

        Button deleteButton = new Button(getContext());
        deleteButton.setText("Delete");

        editButton.setOnClickListener(v -> {
            if (ingredient != null) {
                listener.onEdit(ingredient);
            }
        });

        deleteButton.setOnClickListener(v -> {
            if (ingredient != null) {
                listener.onDelete(ingredient);
            }
        });

        buttonRow.addView(editButton,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));

        buttonRow.addView(deleteButton,
                new LinearLayout.LayoutParams(
                        0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1));

        row.addView(buttonRow);

        return row;
    }

    private String getExpiryWarning(String expiry) {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);

        dateFormat.setLenient(false);

        try {
            Date expiryDate = dateFormat.parse(expiry);

            String todayText = dateFormat.format(new Date());
            Date today = dateFormat.parse(todayText);

            if (expiryDate == null || today == null) {
                return "";
            }

            long daysRemaining = TimeUnit.MILLISECONDS.toDays(
                    expiryDate.getTime() - today.getTime());

            if (daysRemaining < 0) {
                return "EXPIRED";
            } else if (daysRemaining == 0) {
                return "EXPIRES TODAY";
            } else if (daysRemaining <= 3) {
                return "EXPIRING SOON";
            }

        } catch (ParseException e) {
            return "";
        }

        return "";
    }
}
