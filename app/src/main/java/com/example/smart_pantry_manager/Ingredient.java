
package com.example.smart_pantry_manager;

public class Ingredient {

    public long id;
    public String name;
    public double quantity;
    public String unit;
    public String expiry;

    public Ingredient(long id, String name,
                      double quantity, String unit,
                      String expiry) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiry = expiry;
    }
}
