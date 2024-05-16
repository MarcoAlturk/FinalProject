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

    public Event() {
    }

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

    public void displayEventDetails() {
        System.out.println("Name : " + name);
        System.out.println("Description : " + description);
        System.out.println(maxNumOfParticipants + " participants maximum.");
        System.out.printf("%.2f$ per person.\n", pricePerPerson);
        SortingAlgorithms.quickSort(guestList, 0, guestList.size()-1);
        System.out.println("Guest List Sorted By Age : ");
        for (int i = 0; i < guestList.size(); i++) {
            System.out.println(i + 1 + " - " + guestList.get(i).firstName + " " + guestList.get(i).lastName + " " + guestList.get(i).age);
        }
        System.out.println("Date : " + date);
        System.out.println("Budget : " + budget + "$");
        System.out.println("--------------------------------------------");

    }
}
