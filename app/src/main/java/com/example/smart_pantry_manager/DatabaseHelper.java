
package com.example.smart_pantry_manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smart_pantry.db";
    private static final int DATABASE_VERSION = 2;

    public static final String TABLE_INGREDIENTS = "ingredients";
    public static final String COL_ID = "id";
    public static final String COL_NAME = "name";
    public static final String COL_QUANTITY = "quantity";
    public static final String COL_UNIT = "unit";
    public static final String COL_EXPIRY = "expiry_date";

    private static final String TABLE_RECIPES = "recipes";
    private static final String TABLE_RECIPE_INGREDIENTS =
            "recipe_ingredients";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_INGREDIENTS + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_NAME + " TEXT NOT NULL, "
                + COL_QUANTITY + " REAL NOT NULL, "
                + COL_UNIT + " TEXT NOT NULL, "
                + COL_EXPIRY + " TEXT)");

        createRecipeTables(db);
        seedRecipes(db);
    }

    private void createRecipeTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL UNIQUE, "
                + "description TEXT NOT NULL, "
                + "instructions TEXT NOT NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS "
                + TABLE_RECIPE_INGREDIENTS + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "recipe_id INTEGER NOT NULL, "
                + "ingredient_name TEXT NOT NULL, "
                + "quantity REAL NOT NULL, "
                + "unit TEXT NOT NULL, "
                + "FOREIGN KEY(recipe_id) REFERENCES "
                + TABLE_RECIPES + "(id))");
    }

    private void seedRecipes(SQLiteDatabase db) {
        for (Recipe recipe : RecipeData.getRecipes()) {

            ContentValues recipeValues = new ContentValues();
            recipeValues.put("name", recipe.name);
            recipeValues.put("description", recipe.description);
            recipeValues.put("instructions", recipe.instructions);

            long recipeId = db.insertWithOnConflict(
                    TABLE_RECIPES,
                    null,
                    recipeValues,
                    SQLiteDatabase.CONFLICT_IGNORE
            );

            if (recipeId == -1) {
                continue;
            }

            for (int i = 0; i < recipe.ingredients.size(); i++) {
                ContentValues ingredientValues = new ContentValues();

                ingredientValues.put("recipe_id", recipeId);
                ingredientValues.put(
                        "ingredient_name",
                        recipe.ingredients.get(i)
                );
                ingredientValues.put(
                        "quantity",
                        recipe.quantities.get(i)
                );
                ingredientValues.put(
                        "unit",
                        recipe.units.get(i)
                );

                db.insert(
                        TABLE_RECIPE_INGREDIENTS,
                        null,
                        ingredientValues
                );
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion,
                          int newVersion) {

        if (oldVersion < 2) {
            createRecipeTables(db);
            seedRecipes(db);
        }
    }

    public long addIngredient(String name, double quantity,
                              String unit, String expiryDate) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit);
        values.put(COL_EXPIRY, expiryDate);

        return db.insert(TABLE_INGREDIENTS, null, values);
    }

    public Cursor getAllIngredients() {
        SQLiteDatabase db = getReadableDatabase();

        return db.query(
                TABLE_INGREDIENTS,
                null,
                null,
                null,
                null,
                null,
                COL_ID + " DESC"
        );
    }

    public int updateIngredient(long id, String name,
                                double quantity, String unit,
                                String expiryDate) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, name);
        values.put(COL_QUANTITY, quantity);
        values.put(COL_UNIT, unit);
        values.put(COL_EXPIRY, expiryDate);

        return db.update(
                TABLE_INGREDIENTS,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public int deleteIngredient(long id) {
        SQLiteDatabase db = getWritableDatabase();

        return db.delete(
                TABLE_INGREDIENTS,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public List<Recipe> getAllRecipes() {
        SQLiteDatabase db = getReadableDatabase();
        List<Recipe> recipes = new ArrayList<>();

        try (Cursor recipeCursor = db.query(
                TABLE_RECIPES,
                new String[]{"id", "name", "description", "instructions"},
                null, null, null, null,
                "id ASC")) {

            while (recipeCursor.moveToNext()) {
                long recipeId = recipeCursor.getLong(0);
                String name = recipeCursor.getString(1);
                String description = recipeCursor.getString(2);
                String instructions = recipeCursor.getString(3);

                List<String> ingredientNames = new ArrayList<>();
                List<Double> quantities = new ArrayList<>();
                List<String> units = new ArrayList<>();

                try (Cursor ingredientCursor = db.query(
                        TABLE_RECIPE_INGREDIENTS,
                        new String[]{
                                "ingredient_name",
                                "quantity",
                                "unit"
                        },
                        "recipe_id = ?",
                        new String[]{String.valueOf(recipeId)},
                        null, null,
                        "id ASC")) {

                    while (ingredientCursor.moveToNext()) {
                        ingredientNames.add(
                                ingredientCursor.getString(0));
                        quantities.add(
                                ingredientCursor.getDouble(1));
                        units.add(
                                ingredientCursor.getString(2));
                    }
                }

                recipes.add(new Recipe(
                        name,
                        description,
                        ingredientNames,
                        quantities,
                        units,
                        instructions
                ));
            }
        }

        return recipes;
    }
}
