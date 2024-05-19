package org.example;

/**
 * Vegetarian diet class that extends Diet abstract class
 */
public class VegetarianDiet extends Diet {
    static final double price = 25.00;

    /**
     * No-args constructor that sets the price, name and description
     */
    public VegetarianDiet() {
        super(price, "Vegetarian Diet", "Excludes meat, poultry, and fish, focusing on plant-based foods.");
    }

}
