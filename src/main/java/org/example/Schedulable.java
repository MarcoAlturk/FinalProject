package org.example;

public interface Schedulable {
    void addEvent(Event event);
    boolean deleteEvent(Event event);
    boolean rescheduleEvent(Event originalEvent, Event newEvent);
}
