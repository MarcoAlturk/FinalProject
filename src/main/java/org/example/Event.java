package org.example;

import java.util.ArrayList;

public class Event {
    String name;
    String description;
    int maxNumOfParticipants;
    double pricePerPerson;
    ArrayList<Guest> guestList = new ArrayList<Guest>();
    Date date;
    double budget;

    public Event(String name, String description, int maxNumOfParticipants, double pricePerPerson, ArrayList<Guest> guestList, Date date, double budget) {
        this.name = name;
        this.description = description;
        this.maxNumOfParticipants = maxNumOfParticipants;
        this.pricePerPerson = pricePerPerson;
        this.guestList = guestList;
        this.date = date;
        this.budget = budget;
    }
}
