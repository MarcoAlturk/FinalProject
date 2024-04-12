package org.example;

public abstract class Event {
    int maxNumOfParticipants;
    double pricePerPerson;

    Date date;
    double budget;

    public abstract double calculateExpenses();



}
