package org.example;

public abstract class Diet {
    protected double price;
    protected int numberOfPlates;
    protected String name;
    protected String description;


    public Diet() {
    }

    public Diet(double price, int numberOfPlates, String name, String description) {
        this.price = price;
        this.numberOfPlates = numberOfPlates;
        this.name = name;
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public int getNumberOfPlates() {
        return numberOfPlates;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setNumberOfPlates(int numberOfPlates) {
        this.numberOfPlates = numberOfPlates;
    }




}
