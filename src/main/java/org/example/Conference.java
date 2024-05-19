package org.example;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * A conference, which extends an event and implements the budgetable interface
 * a conference is a specific type of event
 */
class Conference extends Event implements Budgetable {
    private double budget;

    /**
     * Conference constructor, same as event
     * @param name name of conference
     * @param description description of conference
     * @param maxNumOfParticipants number of participants
     * @param pricePerPerson price per guest
     * @param guestList guestList arraylist
     * @param date Localdate date of event
     * @param budget needed budget
     */
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
