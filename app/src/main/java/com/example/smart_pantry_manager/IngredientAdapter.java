
package com.example.smart_pantry_manager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

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
}
