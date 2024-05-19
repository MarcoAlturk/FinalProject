package org.example;

/**
 * General Diet class extends Diet abstract class
 */
public class GeneralDiet extends Diet {
    static final double price = 22.00;

    /**
     * No-args constructor, initiates price, name and description
     */
    public GeneralDiet() {
        super(price, "General Diet", "Includes a balanced mix of all food groups without specific dietary restrictions.");
    }
}