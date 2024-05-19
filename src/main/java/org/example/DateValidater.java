package org.example;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Date Validater class, only used to check if the date format is valid
 */
public class DateValidater {
    /**
     * Checks if the date is valid
     * @param year year
     * @param month month
     * @param day day
     * @return true/false, true if the date is correct
     */
    public static boolean isValidDate(int year, int month, int day) {
        try {
            LocalDate date = LocalDate.of(year, month, day);
            return true; // No exception means the date is valid
        } catch (DateTimeException e) {
            return false; // DateTimeException is thrown for invalid dates
        }
    }
}