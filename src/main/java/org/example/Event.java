package org.example;

import java.time.LocalDate;
import java.util.ArrayList;

public class Event {
    String name;
    String description;
    int maxNumOfParticipants;
    double pricePerPerson;
    ArrayList<Guest> guestList = new ArrayList<Guest>();
    LocalDate date;
    double budget;

    public Event(String name, String description, int maxNumOfParticipants, double pricePerPerson, ArrayList<Guest> guestList, LocalDate date, double budget) {
        this.name = name;
        this.description = description;
        this.maxNumOfParticipants = maxNumOfParticipants;
        this.pricePerPerson = pricePerPerson;
        this.guestList = guestList;
        this.date = date;
        this.budget = budget;
    }

    @Override
    public String toString() {
        return "Event{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", maxNumOfParticipants=" + maxNumOfParticipants +
                ", pricePerPerson=" + pricePerPerson +
                ", guestList=" + guestList +
                ", date=" + date +
                ", budget=" + budget +
                '}';
    }
}
