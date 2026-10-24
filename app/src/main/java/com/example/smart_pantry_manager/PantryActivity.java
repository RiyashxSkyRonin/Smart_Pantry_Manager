
package com.example.smart_pantry_manager;

import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class PantryActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView pantryListView;
    private IngredientAdapter ingredientAdapter;
    private List<Ingredient> ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry);

        databaseHelper = new DatabaseHelper(this);

        pantryListView = findViewById(R.id.pantryListView);

        ingredients = new ArrayList<>();

        ingredientAdapter = new IngredientAdapter(
                this,
                ingredients,
                new IngredientAdapter.IngredientActionListener() {
                    @Override
                    public void onEdit(Ingredient ingredient) {
                        showIngredientDialog(ingredient);
                    }

                    @Override
                    public void onDelete(Ingredient ingredient) {
                        confirmDelete(ingredient);
                    }
                }
        );

        pantryListView.setAdapter(ingredientAdapter);

        Button btnAddIngredient = findViewById(R.id.btnAddIngredient);
        btnAddIngredient.setOnClickListener(
                v -> showIngredientDialog(null));

        displayIngredients();
    }

    private void showIngredientDialog(Ingredient ingredient) {
        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);

        int padding = (int) (16 * getResources()
                .getDisplayMetrics().density);
        form.setPadding(padding, padding, padding, padding);

        EditText nameInput = new EditText(this);
        nameInput.setHint("Ingredient name");
        form.addView(nameInput);

        EditText quantityInput = new EditText(this);
        quantityInput.setHint("Quantity");
        quantityInput.setInputType(InputType.TYPE_CLASS_NUMBER
                | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        form.addView(quantityInput);

        EditText unitInput = new EditText(this);
        unitInput.setHint("Unit (e.g. kg, litres, pieces)");
        form.addView(unitInput);

        EditText expiryInput = new EditText(this);
        expiryInput.setHint("Expiry date (optional: YYYY-MM-DD)");
        expiryInput.setInputType(InputType.TYPE_CLASS_DATETIME);
        form.addView(expiryInput);

        if (ingredient != null) {
            nameInput.setText(ingredient.name);
            quantityInput.setText(String.valueOf(ingredient.quantity));
            unitInput.setText(ingredient.unit);
            expiryInput.setText(ingredient.expiry);
        }

        String title = ingredient == null
                ? "Add Ingredient" : "Edit Ingredient";

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(form)
                .setPositiveButton(
                        ingredient == null ? "Add" : "Save",
                        (dialog, which) -> {
                            String name = nameInput.getText()
                                    .toString().trim();
                            String quantityText = quantityInput.getText()
                                    .toString().trim();
                            String unit = unitInput.getText()
                                    .toString().trim();
                            String expiry = expiryInput.getText()
                                    .toString().trim();

                            if (name.isEmpty() || quantityText.isEmpty()
                                    || unit.isEmpty()) {
                                Toast.makeText(this,
                                        "Enter name, quantity and unit",
                                        Toast.LENGTH_LONG).show();
                                return;
                            }

                            try {
                                double quantity =
                                        Double.parseDouble(quantityText);

                                if (quantity <= 0
                                        || Double.isNaN(quantity)
                                        || Double.isInfinite(quantity)) {
                                    Toast.makeText(this,
                                            "Enter a valid quantity greater than zero",
                                            Toast.LENGTH_LONG).show();
                                    return;
                                }

                                long result;
                                if (ingredient == null) {
                                    result = databaseHelper.addIngredient(
                                            name, quantity, unit, expiry);
                                } else {
                                    result = databaseHelper.updateIngredient(
                                            ingredient.id, name, quantity,
                                            unit, expiry);
                                }

                                if (result == -1) {
                                    Toast.makeText(this,
                                            "Could not save ingredient",
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    displayIngredients();
                                    Toast.makeText(this,
                                            ingredient == null
                                                    ? "Ingredient added"
                                                    : "Ingredient updated",
                                            Toast.LENGTH_SHORT).show();
                                }

                            } catch (NumberFormatException e) {
                                Toast.makeText(this,
                                        "Enter a valid quantity",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void confirmDelete(Ingredient ingredient) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Ingredient")
                .setMessage("Remove " + ingredient.name
                        + " from your pantry?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    int result = databaseHelper.deleteIngredient(
                            ingredient.id);

                    if (result > 0) {
                        displayIngredients();
                        Toast.makeText(this,
                                "Ingredient deleted",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this,
                                "Could not delete ingredient",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void displayIngredients() {
        ingredients.clear();

        try (Cursor cursor = databaseHelper.getAllIngredients()) {
            while (cursor.moveToNext()) {
                Ingredient ingredient = new Ingredient(
                        cursor.getLong(cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_NAME)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_QUANTITY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_UNIT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(
                                DatabaseHelper.COL_EXPIRY))
                );

                ingredients.add(ingredient);
            }
        }

        ingredientAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (databaseHelper != null) {
            databaseHelper.close();
        }
    }
}
