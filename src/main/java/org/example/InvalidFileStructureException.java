package org.example;

/**
 * Exception used if the file structure entered is wrong
 */
public class InvalidFileStructureException extends Exception {
    public InvalidFileStructureException(String message) {
        super(message);
    }
}
