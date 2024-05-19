package org.example;

/**
 * Exception used if the diet entered is wrong
 */
public class InvalidDietException extends Exception {
    public InvalidDietException(String message) {super(message);}
}
