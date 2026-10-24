package com.example.smart_pantry_manager;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecipesActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(this);

        ScrollView scrollView = new ScrollView(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 24, 24, 24);

        TextView title = new TextView(this);
        title.setText("Suggested Recipes");
        title.setTextSize(26);
        title.setGravity(Gravity.CENTER);
        layout.addView(title);

        scrollView.addView(layout);
        setContentView(scrollView);

        showMatchingRecipes();
    }


    private List<PantryItem> loadPantry() {
        List<PantryItem> pantry = new ArrayList<>();

        SimpleDateFormat dateFormat =
                new SimpleDateFormat("yyyy-MM-dd", Locale.ROOT);
        dateFormat.setLenient(false);

        // Today's date, without the time.
        String todayText = dateFormat.format(new Date());

        try (Cursor cursor = databaseHelper.getAllIngredients()) {
            while (cursor.moveToNext()) {

                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_NAME));

                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_QUANTITY));

                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_UNIT));

                String expiry = cursor.getString(
                        cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_EXPIRY));

                // Skip expired ingredients.
                if (expiry != null && !expiry.trim().isEmpty()) {
                    try {
                        Date expiryDate =
                                dateFormat.parse(expiry.trim());

                        Date today = dateFormat.parse(todayText);

                        if (expiryDate != null
                                && today != null
                                && expiryDate.before(today)) {
                            continue;
                        }

                    } catch (ParseException e) {
                        // Do not recommend ingredients with invalid dates.
                        continue;
                    }
                }

                pantry.add(new PantryItem(name, quantity, unit));
            }
        }

        return pantry;
    }


    @SuppressLint("SetTextI18n")
    private void showMatchingRecipes() {
        List<PantryItem> pantry = loadPantry();
        int matches = 0;

        for (Recipe recipe : RecipeData.getRecipes()) {
            if (canMakeRecipe(recipe, pantry)) {
                TextView item = new TextView(this);
                item.setText(
                        recipe.name + "\n" + recipe.description + "\nTap to view details");
                item.setTextSize(18);
                item.setPadding(12, 24, 12, 24);
                item.setClickable(true);
                item.setFocusable(true);
                item.setBackgroundResource(
                        android.R.drawable.list_selector_background);

                item.setOnClickListener(v -> showRecipeDetails(recipe));

                layout.addView(item);
                matches++;
            }
        }

        if (matches == 0) {
            TextView message = new TextView(this);
            message.setText(
                    "No complete recipe matches your pantry yet.\n\n"
                            + "Check your ingredient names, quantities and units."
            );
            message.setTextSize(16);
            message.setPadding(8, 24, 8, 8);
            layout.addView(message);
        }
    }

    @SuppressLint("SetTextI18n")
    private void showRecipeDetails(Recipe recipe) {
        layout.removeAllViews();

        TextView back = new TextView(this);
        back.setText("← Back to Suggested Recipes");
        back.setTextSize(16);
        back.setPadding(8, 12, 8, 20);
        back.setClickable(true);
        back.setFocusable(true);
        back.setOnClickListener(v -> {
            layout.removeAllViews();
            TextView title = new TextView(this);
            title.setText("Suggested Recipes");
            title.setTextSize(26);
            title.setGravity(Gravity.CENTER);
            layout.addView(title);
            showMatchingRecipes();
        });
        layout.addView(back);

        TextView name = new TextView(this);
        name.setText(recipe.name);
        name.setTextSize(25);
        name.setPadding(0, 8, 0, 12);
        layout.addView(name);

        TextView description = new TextView(this);
        description.setText(recipe.description);
        description.setTextSize(16);
        layout.addView(description);

        TextView ingredientsTitle = new TextView(this);
        ingredientsTitle.setText("\nIngredients");
        ingredientsTitle.setTextSize(21);
        layout.addView(ingredientsTitle);

        for (int i = 0; i < recipe.ingredients.size(); i++) {
            TextView ingredient = new TextView(this);
            ingredient.setText(
                    "• " + recipe.quantities.get(i) + " "
                            + recipe.units.get(i) + " "
                            + recipe.ingredients.get(i));
            ingredient.setTextSize(17);
            ingredient.setPadding(4, 8, 4, 8);
            layout.addView(ingredient);
        }

        TextView instructionsTitle = new TextView(this);
        instructionsTitle.setText("\nCooking Instructions");
        instructionsTitle.setTextSize(21);
        layout.addView(instructionsTitle);

        TextView instructions = new TextView(this);
        instructions.setText(recipe.instructions);
        instructions.setTextSize(17);
        instructions.setPadding(4, 8, 4, 16);
        layout.addView(instructions);
    }

    private boolean canMakeRecipe(
            Recipe recipe, List<PantryItem> pantry) {

        for (int i = 0; i < recipe.ingredients.size(); i++) {
            String requiredName = normalizeName(recipe.ingredients.get(i));
            double requiredQuantity = recipe.quantities.get(i);
            String requiredUnit = normalizeUnit(recipe.units.get(i));

            double available = 0;

            for (PantryItem item : pantry) {
                if (normalizeName(item.name).equals(requiredName)
                        && normalizeUnit(item.unit).equals(requiredUnit)) {
                    available += item.quantity;
                }
            }

            if (available < requiredQuantity) {
                return false;
            }
        }

        return true;
    }

    private String normalizeName(String value) {
        String name = value.trim().toLowerCase(Locale.ROOT);

        if (name.equals("eggs")) return "egg";
        if (name.equals("beans")) return "bean";
        if (name.equals("potatoes")) return "potato";
        if (name.equals("tomatoes")) return "tomato";

        if (name.endsWith("s") && name.length() > 3
                && !name.endsWith("ss")) {
            return name.substring(0, name.length() - 1);
        }

        return name;
    }

    private String normalizeUnit(String value) {
        String unit = value.trim().toLowerCase(Locale.ROOT);

        switch (unit) {
            case "l":
            case "liter":
            case "liters":
            case "litre":
            case "litres":
                return "litres";

            case "kg":
            case "kilogram":
            case "kilograms":
                return "kg";

            case "g":
            case "gram":
            case "grams":
                return "g";

            case "piece":
            case "pieces":
            case "pc":
            case "pcs":
                return "pieces";

            case "ml":
            case "millilitre":
            case "millilitres":
            case "milliliter":
            case "milliliters":
                return "ml";

            default:
                return unit;
        }
    }

    private static class PantryItem {
        String name;
        double quantity;
        String unit;

        PantryItem(String name, double quantity, String unit) {
            this.name = name;
            this.quantity = quantity;
            this.unit = unit;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}