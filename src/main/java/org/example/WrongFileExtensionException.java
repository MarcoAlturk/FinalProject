package org.example;

/**
 * Exception used if the file extension they entered is wrong(not txt)
 */
public class WrongFileExtensionException extends Exception {
    public WrongFileExtensionException(String message) {
        super(message);
    }
}
