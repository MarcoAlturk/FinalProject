package org.example;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Event class
 * Holds the name, description, num of participants, price per person, arrayList of guests, date of event and budget
 */
public class Event {
    String name;
    String description;
    int maxNumOfParticipants;
    double pricePerPerson;
    ArrayList<Guest> guestList = new ArrayList<Guest>();
    LocalDate date;
    double budget;

    /**
     * No-args event constructor
     */
    public Event() {
    }

    /**
     * All args event constructor
     * @param name name of event
     * @param description description of event
     * @param maxNumOfParticipants maximum number of guests
     * @param pricePerPerson price per guest
     * @param guestList arrayList of guests
     * @param date date of the event
     * @param budget total budget for the event
     */
    public Event(String name, String description, int maxNumOfParticipants, double pricePerPerson, ArrayList<Guest> guestList, LocalDate date, double budget) {
        this.name = name;
        this.description = description;
        this.maxNumOfParticipants = maxNumOfParticipants;
        this.pricePerPerson = pricePerPerson;
        this.guestList = guestList;
        this.date = date;
        this.budget = budget;
    }

    /**
     * ToString to display the event
     * @return the name, description, numofparticipants, priceperson, guestlist, date, budget
     */
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

    /**
     * Display event details
     * It is like a toString, but more detailed and more nicely formatted
     */
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return maxNumOfParticipants == event.maxNumOfParticipants && Double.compare(event.pricePerPerson, pricePerPerson) == 0 && Double.compare(event.budget, budget) == 0 && Objects.equals(name, event.name) && Objects.equals(description, event.description) && Objects.equals(date, event.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, maxNumOfParticipants, pricePerPerson, guestList, date, budget);
    }
}
