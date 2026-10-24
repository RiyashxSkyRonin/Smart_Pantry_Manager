
package com.example.smart_pantry_manager;

import java.util.List;

public class Recipe {
    public String name;
    public String description;
    public List<String> ingredients;
    public List<Double> quantities;
    public List<String> units;
    public String instructions;

    public Recipe(String name, String description,
                  List<String> ingredients,
                  List<Double> quantities,
                  List<String> units,
                  String instructions) {
        this.name = name;
        this.description = description;
        this.ingredients = ingredients;
        this.quantities = quantities;
        this.units = units;
        this.instructions = instructions;
    }
}
