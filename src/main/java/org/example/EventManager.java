package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Event manager class, holds the arraylist for the events and implements the interface Manageable
 */
public class EventManager implements Manageable<Event> {
    public ArrayList<Event> events;

    /**
     * No-args constructor, initiates an empty arraylist
     */
    public EventManager() {
        this.events = new ArrayList<>();
    }

    /**
     * adds an event to the arraylist
     * @param event
     */
    @Override
    public void add(Event event) {
        events.add(event);
    }


    /**
     * removes an event from the arraylist
     * @param event
     */
    @Override
    public void remove(Event event) {
        events.remove(event);
    }

    /**
     * Updates an event from the arraylist
     * @param oldEvent oldEvent to replace
     * @param newEvent newEvent to replace with
     * @return
     */
    @Override
    public boolean update(Event oldEvent, Event newEvent) {
        int index = events.indexOf(oldEvent);
        if (index != -1) {
            events.set(index, newEvent);
            return true;
        }
        return false;
    }

    /**
     * Prints all the events in the arraylist, sorted by date
     */
    public void viewEvents() {
        SortingAlgorithms.mergeSort(events);
        System.out.println("Events sorted by date : ");
        for (int i = 0; i < events.size(); i++) {
            events.get(i).displayEventDetails();
        }
    }
}

