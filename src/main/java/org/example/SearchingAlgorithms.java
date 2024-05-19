package org.example;

import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class used to hold the searching algorithms used
 */
public class SearchingAlgorithms {
    /**
     * Linear search, just loops through every element and compares it to the wanted element
     * @param list event arraylist
     * @param name name of the event
     * @return the event if found/null if not
     */
    public static Event linearSearch(ArrayList<Event> list, String name) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).name.toLowerCase().equals(name.toLowerCase())) {
                return list.get(i);
            }
        }
        return null;
    }

    /**
     * Hash based search, enters all the guests in a hashmap and uses the .get() method
     * @param list guest arraylist
     * @param name name of guest
     * @return the guest if found/null if not
     */
    public static Guest hashBasedSearch(ArrayList<Guest> list, String name) {
        HashMap<String, Guest> guestMap = new HashMap<>();
        for (Guest guest : list) {
            guestMap.put(guest.firstName.toLowerCase() + " " + guest.lastName.toLowerCase(), guest);
        }
        return guestMap.get(name.toLowerCase());
    }
}
