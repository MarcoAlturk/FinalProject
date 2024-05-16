package org.example;

import java.util.ArrayList;
import java.util.List;

public class EventManager implements Manageable<Event> {
    public List<Event> events;

    public EventManager() {
        this.events = new ArrayList<>();
    }

    @Override
    public void add(Event event) {
        events.add(event);
    }


    @Override
    public void remove(Event event) {
        events.remove(event);
    }

    @Override
    public boolean update(Event oldEvent, Event newEvent) {
        int index = events.indexOf(oldEvent);
        if (index != -1) {
            events.set(index, newEvent);
            return true;
        }
        return false;
    }

    public static void viewEvents() {
        
    }
}

