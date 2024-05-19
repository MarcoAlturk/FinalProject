package org.example;

/**
 * Vegan Diet class that extends Diet abstract class
 *
 */
public class VeganDiet extends Diet {
    static final double price = 18.99;

    /**
     * VeganDiet no-args constructor, sets price, name and description
     */
    public VeganDiet() {
        super(price, "Vegan Diet", "Excludes all animal products, including dairy, eggs, and often honey, focusing entirely on plant-based foods.");
    }
}
