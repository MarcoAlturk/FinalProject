package org.example;

import java.util.HashMap;
import java.util.Map;

public class GuestManager implements Manageable<Guest> {
    private Map<Guest, Diet> guestDiets;

    public GuestManager() {
        this.guestDiets = new HashMap<>();
    }

    public void add(Guest guest, Diet diet) {
        guestDiets.put(guest, diet);
    }

    @Override
    public void add(Guest guest) {
        // If a guest is added without a specific diet, add them with a default or null diet
        this.add(guest, new GeneralDiet());  // Or specify a default diet if applicable
    }

    @Override
    public void remove(Guest guest) {
        guestDiets.remove(guest);
    }

    @Override
    public boolean update(Guest oldGuest, Guest newGuest) {
        if (guestDiets.containsKey(oldGuest)) {
            Diet diet = guestDiets.get(oldGuest);
            guestDiets.remove(oldGuest);
            guestDiets.put(newGuest, diet);
            return true;
        }
        return false;
    }
}

