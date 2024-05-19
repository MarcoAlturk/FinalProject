package org.example;

/**
 * Exception used if the option entered is out of the range of the options
 */
public class OptionOutOfRangeException extends Exception {
    public OptionOutOfRangeException(String message) {
        super(message);
    }
}
