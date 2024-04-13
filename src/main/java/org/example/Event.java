package org.example;

import java.util.ArrayList;

public class Event {
    int maxNumOfParticipants;
    double pricePerPerson;
    ArrayList<Guest> guestList = new ArrayList<Guest>();
    Date date;
    double budget;

    public Event(int maxNumOfParticipants, double pricePerPerson, Date date, double budget) {
        this.maxNumOfParticipants = maxNumOfParticipants;
        this.pricePerPerson = pricePerPerson;
        this.date = date;
        this.budget = budget;
    }


}
