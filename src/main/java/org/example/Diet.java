package org.example;

/**
 * Abstract class Diet, encompasses all the different types of diets
 */
public abstract class Diet {
    protected double price;
    protected String name;
    protected String description;

    /**
     * No args constructor for Diet
     */
    public Diet() {
    }

    /**
     * All args constructor for diet
     * @param price price of diet
     * @param name name of diet
     * @param description small description of diet
     */
    public Diet(double price, String name, String description) {
        this.price = price;
        this.name = name;
        this.description = description;
    }

    /**
     * getPrice(), getter for the price
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * getName(), getter for the name
     * @return the name
     */
    public String getName() {
        return name;
    }


    /**
     * getDescription(), getter for the description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * ToString to display the Diet
     * @return the price, name, descriptions
     */
    @Override
    public String toString() {
        return "Diet{" +
                "price=" + price +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
