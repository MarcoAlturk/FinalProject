package org.example;

/**
 * Exception used if they enter a negative number
 */
public class NegativeNumberException extends Exception {
    public NegativeNumberException(String message) {

        super(message);
    }
}
