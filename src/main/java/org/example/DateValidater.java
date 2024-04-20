package org.example;

import java.time.DateTimeException;
import java.time.LocalDate;

public class DateValidater {
    public static boolean isValidDate(int year, int month, int day) {
        try {
            LocalDate date = LocalDate.of(year, month, day);
            return true; // No exception means the date is valid
        } catch (DateTimeException e) {
            return false; // DateTimeException is thrown for invalid dates
        }
    }
}