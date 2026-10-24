package com.example.smart_pantry_manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecipeData {

    public static List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();

        recipes.add(new Recipe(
                "Scrambled Eggs",
                "A quick egg breakfast.",
                Arrays.asList("egg", "milk", "butter"),
                Arrays.asList(2.0, 0.05, 0.01),
                Arrays.asList("pieces", "litres", "kg"),
                "1. Beat the eggs with milk.\n"
                        + "2. Melt butter in a pan.\n"
                        + "3. Add the eggs and stir until cooked."
        ));

        recipes.add(new Recipe(
                "Rice and Beans",
                "A simple, filling meal.",
                Arrays.asList("rice", "beans", "salt"),
                Arrays.asList(0.2, 0.2, 0.005),
                Arrays.asList("kg", "kg", "kg"),
                "1. Cook the rice.\n"
                        + "2. Cook or heat the beans thoroughly.\n"
                        + "3. Season and serve together."
        ));

        recipes.add(new Recipe(
                "Simple Homemade Pancakes",
                "Soft homemade pancakes for breakfast.",
                Arrays.asList("flour", "milk", "egg"),
                Arrays.asList(0.2, 0.25, 1.0),
                Arrays.asList("kg", "litres", "pieces"),
                "1. Mix the flour, milk and egg into a smooth batter.\n"
                        + "2. Heat a lightly greased pan.\n"
                        + "3. Pour in some batter.\n"
                        + "4. Cook both sides until golden."
        ));

        recipes.add(new Recipe(
                "Fried Eggs",
                "A quick and easy egg dish.",
                Arrays.asList("egg", "butter", "salt"),
                Arrays.asList(2.0, 0.01, 0.002),
                Arrays.asList("pieces", "kg", "kg"),
                "1. Heat the butter in a pan.\n"
                        + "2. Crack the eggs into the pan.\n"
                        + "3. Cook until the whites are set.\n"
                        + "4. Season with salt and serve."
        ));

        recipes.add(new Recipe(
                "Boiled Eggs",
                "Simple boiled eggs.",
                Arrays.asList("egg", "salt"),
                Arrays.asList(2.0, 0.002),
                Arrays.asList("pieces", "kg"),
                "1. Place the eggs in a pot of water.\n"
                        + "2. Bring the water to a boil.\n"
                        + "3. Cook until the eggs reach your preferred firmness.\n"
                        + "4. Cool, peel and season."
        ));

        recipes.add(new Recipe(
                "Plain Rice",
                "A basic rice side dish.",
                Arrays.asList("rice", "salt"),
                Arrays.asList(0.2, 0.003),
                Arrays.asList("kg", "kg"),
                "1. Rinse the rice.\n"
                        + "2. Add rice and water to a pot.\n"
                        + "3. Simmer until tender and the water is absorbed.\n"
                        + "4. Add salt and serve."
        ));

        recipes.add(new Recipe(
                "Butter Rice",
                "Rice enriched with butter.",
                Arrays.asList("rice", "butter", "salt"),
                Arrays.asList(0.2, 0.01, 0.003),
                Arrays.asList("kg", "kg", "kg"),
                "1. Cook the rice until tender.\n"
                        + "2. Stir in the butter.\n"
                        + "3. Season with salt and serve."
        ));

        recipes.add(new Recipe(
                "Milk Porridge",
                "A warm and simple breakfast.",
                Arrays.asList("flour", "milk", "salt"),
                Arrays.asList(0.05, 0.25, 0.001),
                Arrays.asList("kg", "litres", "kg"),
                "1. Mix the flour with a little cold milk.\n"
                        + "2. Heat the remaining milk gently.\n"
                        + "3. Stir in the flour mixture.\n"
                        + "4. Cook while stirring until thickened."
        ));

        recipes.add(new Recipe(
                "Basic Flatbread",
                "A simple pan-cooked flatbread.",
                Arrays.asList("flour", "salt"),
                Arrays.asList(0.2, 0.003),
                Arrays.asList("kg", "kg"),
                "1. Mix the flour and salt with enough water to form a dough.\n"
                        + "2. Knead until smooth.\n"
                        + "3. Divide and roll into flat rounds.\n"
                        + "4. Cook in a hot pan until lightly browned on both sides."
        ));

        recipes.add(new Recipe(
                "Egg Fried Rice",
                "A simple rice dish with egg.",
                Arrays.asList("rice", "egg", "butter", "salt"),
                Arrays.asList(0.2, 1.0, 0.01, 0.003),
                Arrays.asList("kg", "pieces", "kg", "kg"),
                "1. Cook the rice and allow it to cool slightly.\n"
                        + "2. Melt the butter in a pan.\n"
                        + "3. Add the egg and stir until cooked.\n"
                        + "4. Add the rice and salt, then heat thoroughly."
        ));

        recipes.add(new Recipe(
                "Creamy Scrambled Eggs",
                "Scrambled eggs with milk for a soft texture.",
                Arrays.asList("egg", "milk", "salt"),
                Arrays.asList(2.0, 0.03, 0.002),
                Arrays.asList("pieces", "litres", "kg"),
                "1. Beat the eggs, milk and salt together.\n"
                        + "2. Pour into a lightly greased heated pan.\n"
                        + "3. Stir gently until the eggs are fully cooked."
        ));

        recipes.add(new Recipe(
                "Simple Bean Stew",
                "A warm and filling bean dish.",
                Arrays.asList("beans", "rice", "salt"),
                Arrays.asList(0.25, 0.1, 0.003),
                Arrays.asList("kg", "kg", "kg"),
                "1. Cook dried beans until tender, or heat prepared beans thoroughly.\n"
                        + "2. Cook the rice separately.\n"
                        + "3. Season the beans with salt.\n"
                        + "4. Serve the beans with rice."
        ));

        recipes.add(new Recipe(
                "Salted Pancakes",
                "A simple savoury pancake.",
                Arrays.asList("flour", "milk", "egg", "salt"),
                Arrays.asList(0.2, 0.2, 1.0, 0.002),
                Arrays.asList("kg", "litres", "pieces", "kg"),
                "1. Combine flour, milk, egg and salt.\n"
                        + "2. Mix until smooth.\n"
                        + "3. Pour a thin layer into a heated pan.\n"
                        + "4. Cook both sides until done."
        ));

        recipes.add(new Recipe(
                "Butter Flatbread",
                "Flatbread cooked with a little butter.",
                Arrays.asList("flour", "butter", "salt"),
                Arrays.asList(0.2, 0.01, 0.003),
                Arrays.asList("kg", "kg", "kg"),
                "1. Mix flour and salt with enough water to form dough.\n"
                        + "2. Knead and roll into flat rounds.\n"
                        + "3. Heat a pan and add a little butter.\n"
                        + "4. Cook each flatbread on both sides."
        ));

        recipes.add(new Recipe(
                "Rice Pancakes",
                "A variation on pancakes using rice flour.",
                Arrays.asList("rice", "milk", "egg"),
                Arrays.asList(0.2, 0.2, 1.0),
                Arrays.asList("kg", "litres", "pieces"),
                "1. Grind the rice into a fine flour before measuring, or use rice flour.\n"
                        + "2. Mix with milk and egg until smooth.\n"
                        + "3. Pour small portions into a heated pan.\n"
                        + "4. Cook both sides until set."
        ));

        recipes.add(new Recipe(
                "Egg and Bean Bowl",
                "A filling combination of beans and eggs.",
                Arrays.asList("beans", "egg", "salt"),
                Arrays.asList(0.2, 2.0, 0.003),
                Arrays.asList("kg", "pieces", "kg"),
                "1. Heat prepared beans thoroughly.\n"
                        + "2. Cook the eggs in a separate pan.\n"
                        + "3. Season the beans with salt.\n"
                        + "4. Serve the eggs with the beans."
        ));

        return recipes;
    }
}
