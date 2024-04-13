package org.example;

public abstract class Diet {
    protected double price;
    protected String name;
    protected String description;


    public Diet() {
    }

    public Diet(double price, String name, String description) {
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public double getPrice() {
        return price;
    }



    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
