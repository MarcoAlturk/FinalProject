package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome to the event manager!");

        EventManager eventManager = new EventManager();
        GuestManager guestManager = new GuestManager();

        Scanner scanner = new Scanner(System.in);
        int input = 0;

        do {
            try {
                System.out.println("Please enter what you would like to do : ");
                System.out.println("1 - Organize a new event.\n" +
                        "2 - Edit an existing event.\n" +
                        "3 - Publish an event to telegram.\n" +
                        "4 - View all the scheduled events.\n" +
                        "5 - Delete events.\n" +
                        "6 - Print tickets for guests.\n" +
                        "7 - Exit");
                input = scanner.nextInt();

                if (input < 1 || input > 7) {
                    throw new OptionOutOfRangeException("Please enter a number from 1-7.");
                }

                switch(input){
                    case 1:
                        makeNewEvent(eventManager);
                        break;
                    case 2:
                        editEvent();
                        break;
                    case 3:
                        publishEventToTelegram();
                        break;
                    case 4:
                        viewFutureEvents();
                        break;
                    case 5:
                        deleteEvents();
                        break;
                    case 6:
                        printTickets();
                        break;
                    case 7:
                        break;
                }

            } catch(OptionOutOfRangeException e) {
                System.out.println(e.getMessage());
                scanner.nextLine(); // clear the buffer
            } catch(Exception e) {
                System.out.println("Please enter a number.");
                scanner.nextLine(); // clear the buffer
            }
        } while(input != 7); // keep looping until they exit

    }

    public static void makeNewEvent(EventManager eventManager) { // make a new event
        String name;
        String description;
        int maxNumOfGuests;
        double pricePerGuest;

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the name of the event : ");
        name = scanner.nextLine();
        System.out.println("Enter a description of the event : ");
        description = scanner.nextLine();
        boolean enteredRight = false;
        do {
            try {
                System.out.print("Enter the total number of guests (if there is no maximum enter 0) : ");
                maxNumOfGuests = scanner.nextInt();
                if (maxNumOfGuests < 0) {
                    throw new NegativeNumberException("Please enter a positive number.");
                } else {
                    enteredRight = true;
                }
            } catch (NegativeNumberException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                scanner.nextLine();
            }

        } while (!enteredRight); // keep looping until they enter a valid integer

        enteredRight = false;
        do {
            try {
                System.out.print("Enter the price for guests to participate in the event : ");
                pricePerGuest = scanner.nextDouble();
                if (pricePerGuest < 0) {
                    throw new NegativeNumberException("Please enter a positive number.");
                } else {
                    enteredRight = true;
                }
            } catch (NegativeNumberException e) {
                System.out.println(e.getMessage());
                scanner.nextLine();
            } catch (Exception e) {
                System.out.println("Please enter a number.");
                scanner.nextLine();
            }

        } while (!enteredRight);
        scanner.nextLine(); // reset the buffer
        enteredRight = false;
        do {
            try {
                System.out.println("Please enter the directory of the file containing the guests (the file should be formatted as FIRST NAME,LAST NAME,AGE,DIET(VEGAN, VEGETARIAN, NODIET)) : ");
                String filePath = scanner.nextLine();
                if (filePath.endsWith(".txt")) {
                    String content = new String(Files.readAllBytes(Paths.get(filePath)));
                    System.out.println("File content:");
                    System.out.println(content);
                    enteredRight = true;
                } else {

                }

            } catch (IOException e) {
                System.out.println("There was an error reading the file. Please make sure that the directory is correct.");

            }
        } while (!enteredRight);

        // ask for the directory of guest list, give them the format
        // get it and separate, store in guestList
        // ask for date
        // ask for budget estimate


    }

    public static void editEvent() {

    }

    public static void publishEventToTelegram() {

    }

    public static void viewFutureEvents() {

    }

    public static void deleteEvents() {

    }

    public static void printTickets() {

    }
}