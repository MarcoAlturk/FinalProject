package org.example;

/**
 * Exception if the date entered is wrong
 */
public class InvalidDateException extends Exception {
    public InvalidDateException(String message) {
        super(message);
    }
}
