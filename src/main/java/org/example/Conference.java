package org.example;

import java.time.LocalDate;
import java.util.ArrayList;

class Conference extends Event implements Budgetable {
    private double budget;

    public Conference(String name, String description, int maxNumOfParticipants, double pricePerPerson, ArrayList<Guest> guestList, LocalDate date, double budget) {
        super(name, description, maxNumOfParticipants, pricePerPerson, guestList, date, budget);
    }

    public Conference() {
    }

    @Override
    public void setBudget(double budget) {
        this.budget = budget;
    }

    @Override
    public double getBudget() {
        return budget;
    }

    @Override
    public void displayEventDetails() {
        super.displayEventDetails();
    }
}
