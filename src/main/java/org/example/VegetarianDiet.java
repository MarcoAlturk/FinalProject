package org.example;

public class VegetarianDiet extends Diet {

    public VegetarianDiet(double price, int numberOfPlates, String name, String description) {
        super(price, numberOfPlates, name, description);

    }

    public void setPrice() {
        this.price = 22.5;
    }

    public void setName() {
        this.name = "Vegetarian Diet";
    }

    public void setDescription() {
        this.description = "A vegetarian diet excludes meat, poultry, and fish, focusing instead on fruits, vegetables, grains, and nuts. It's known for its health benefits, including lower risks of heart disease, hypertension, and diabetes.";
    }


}
