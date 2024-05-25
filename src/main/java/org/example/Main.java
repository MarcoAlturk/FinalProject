package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import jdk.jshell.spi.ExecutionControlProvider;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetUserProfilePhotos;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

//to do :
// implement sorting algorithm for the guestlist, and for events
// implement searching algorithm for guest list and for events
// documentation
// project report

/**
 * Marco Alturk
 * Main Class
 */
public class Main {
    private static EventManager eventManager = new EventManager();
    private static final Logger logger = LoggerFactory.getLogger(SendTelegramMessage.class);

    /**
     * Main method
     * It asks for the user input and then calls the appropriate method
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Hello and welcome to the event manager!");

        Scanner scanner = new Scanner(System.in);
        int input = 0;

        do {
            try {

                System.out.println("1 - Organize a new event.\n" +
                        "2 - Edit an existing event.\n" +
                        "3 - Publish an event to telegram.\n" +
                        "4 - View all the scheduled events.\n" +
                        "5 - Delete events.\n" +
                        "6 - Make tickets for guests.\n" +
                        "7 - Search for an event\n" +
                        "8 - Search for a guest\n" +
                        "9 - Exit");
                System.out.print("Please enter what you would like to do : ");
                input = scanner.nextInt();

                if (input < 1 || input > 9) {
                    throw new OptionOutOfRangeException("Please enter a number from 1-9.");
                }

                switch(input){
                    case 1:
                        makeNewEvent();
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
                        makeTickets();
                        break;
                    case 7:
                        searchForEvent();
                        break;
                    case 8:
                        searchForGuest();
                        break;
                    case 9:
                        return;

                }

            } catch(OptionOutOfRangeException e) {
                System.out.println(e.getMessage());
                scanner.nextLine(); // clear the buffer
            } catch(Exception e) {
                System.out.println("exception : " + e.getMessage());
                System.out.println("Please enter a number.");
                scanner.nextLine(); // clear the buffer
            }
        } while(input != 9); // keep looping until they exit

    }

    /**
     * It makes a new event, getting all the necessary info in order to create a new Event Object and add it to the Eventmanager events arraylist
     */
    public static void makeNewEvent() { // make a new event
        String name;
        String description;
        int maxNumOfGuests = 0;
        double pricePerGuest = 0;
        LocalDate date = LocalDate.now();
        Scanner scanner = new Scanner(System.in);
        boolean enteredRight = false;
        Event event;
        do {
            System.out.println("Is the event a conference? (Y/N) : ");
            String answer = scanner.nextLine();
            if (answer.equals("Y")) {
                enteredRight = true;
                event = new Conference();
            } else if (answer.equals("N")) {
                enteredRight = true;
                event = new Event();
            } else {
                enteredRight = false;
            }
        } while (!enteredRight);




        System.out.print("Enter the name of the event : ");
        name = scanner.nextLine();
        System.out.println("Enter a description of the event : ");
        description = scanner.nextLine();


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
                    enteredRight = false;
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

        LocalDate today = LocalDate.now();
        scanner.nextLine(); // reset the buffer
        enteredRight = false;
        do {
            try {
                System.out.println("When is the event (dd/mm/yyyy) : ");
                String input = scanner.nextLine();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                date = LocalDate.parse(input, formatter);
                if (DateValidater.isValidDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth())) {

                    if (date.isBefore(LocalDate.now())) {
                        System.out.println("Please don't enter a date before now.");
                    } else {
                        date = LocalDate.parse(input, formatter);
                        enteredRight = true;
                    }
                } else {
                    throw new InvalidDateException("Please enter a valid date.");
                }
            } catch (InvalidDateException e) {
                System.out.println(e.getMessage());
            }
            catch(Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!enteredRight);


        enteredRight = false;
        int numOfPeopleInFile = 0;
        ArrayList<Guest> guestList = new ArrayList<>();
        do {
            try {
                System.out.println("Please enter the directory of the file containing the guests (the file should be formatted as FIRST NAME,LAST NAME,AGE,DIET(VEGAN, VEGETARIAN OR NODIET)) : ");
                String filePath = scanner.nextLine();
                if (filePath.endsWith(".txt")) {
                    List<String> lines = Files.readAllLines(Paths.get(filePath));
                    numOfPeopleInFile = lines.size();
                    if (lines.size() > maxNumOfGuests) {
                        System.out.println("The file contains more guests than allowed.");
                    } else {
                        guestList.clear();
                        for (String line : lines) {
                            String[] data = line.split(",");
                            if (data.length == 4) { // check if each line has 4 pieces of data
                                String firstName = data[0].trim();
                                String lastName = data[1].trim();
                                int age = Integer.parseInt(data[2].trim());
                                String dietPreference = data[3].trim();

                                if (age <= 0) {
                                    enteredRight = false;
                                    throw new NegativeNumberException("Please make sure all of the ages in the file are valid.");
                                }

                                boolean guestBooked = false;

                                for (Event eventLoop : eventManager.events) {
                                    int comparisonResult = date.compareTo(eventLoop.date);
                                    for (Guest guestLoop : eventLoop.guestList) {
                                        if (guestLoop.firstName.equals(firstName) && guestLoop.lastName.equals(lastName) && comparisonResult == 0) {
                                            System.out.println(guestLoop.firstName + " " + guestLoop.lastName + " already has an event scheduled for them on the same day!");
                                            enteredRight = false;
                                            guestBooked = true;
                                        }
                                    }
                                }
                                if (!guestBooked) {
                                    if (guestList.contains(new Guest(firstName, lastName, age, new VeganDiet()))) {
                                        enteredRight = false;
                                        System.out.println(firstName + " " + lastName + " has been added twice!");
                                    } else {
                                        switch (dietPreference.toLowerCase()) {
                                            case "vegan":
                                                Guest guest = new Guest(firstName, lastName, age, new VeganDiet());
                                                guestList.add(guest);
                                                enteredRight = true;
                                                break;
                                            case "nodiet":
                                                Guest guest2 = new Guest(firstName, lastName, age, new GeneralDiet());
                                                guestList.add(guest2);
                                                enteredRight = true;
                                                break;
                                            case "vegetarian":
                                                Guest guest3 = new Guest(firstName, lastName, age, new VegetarianDiet());
                                                guestList.add(guest3);
                                                enteredRight = true;
                                                break;
                                            default:
                                                throw new InvalidDietException("Please make sure all diets are correctly formatted (VEGAN, VEGETARIAN OR NODIET)");
                                        }
                                    }
                                }
                            } else {
                                enteredRight = false;
                                throw new InvalidFileStructureException("There was an error reading the file contents. Please make sure the file is correctly formatted.");
                            }
                        }
                    }


                } else {
                    throw new WrongFileExtensionException("Please enter a txt file.");
                }

            } catch (InvalidDietException e) {
                System.out.println(e.getMessage());
            } catch (NegativeNumberException e) {
                System.out.println(e.getMessage());
            } catch (InvalidFileStructureException e) {
                System.out.println(e.getMessage());
            } catch (WrongFileExtensionException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("There was an error reading the file. Please make sure that the directory is correct.");

            }
        } while (!enteredRight);


        int budget = 0;
        enteredRight = false;
        do {
            try {
                System.out.print("Please enter the budget for the event : ");
                budget = scanner.nextInt();
                if (budget < 0) throw new NegativeNumberException("Please enter a positive number.");
                else {
                    double totalMoney = budget + maxNumOfGuests * pricePerGuest;
                    double totalExpense = 0;
                    for (Guest guest : guestList) {
                        totalExpense += guest.diet.getPrice();
                    }
                    if (totalMoney < totalExpense) {
                        System.out.println("The total budget + price per guest is not enough to cover the expenses!");
                        System.out.printf("$%.2f budget vs $%.2f expenses\n", totalMoney, totalExpense);
                    } else {
                        enteredRight = true;
                    }
                }
            } catch(NegativeNumberException e) {
                System.out.println(e.getMessage());
            }catch (Exception e) {
                System.out.println(e.getMessage());
            }
        } while (!enteredRight);

        System.out.println("You successfully created an event!");

        event = new Event(name, description, maxNumOfGuests, pricePerGuest, guestList, date, budget);
        eventManager.add(event);
    }

    /**
     * It edits any already existing event
     * It accesses the event from the EventManager events Arraylist
     */
    public static void editEvent() {
        Scanner scanner = new Scanner(System.in);
        if (eventManager.events.size() == 0) {
            System.out.println("You don't have any events.");
        } else {
            System.out.println("Current events : ");
            for (int i = 0; i < eventManager.events.size(); i++) {
                System.out.println(i + 1 + " - " + eventManager.events.get(i).name);
            }
            boolean enteredRight = false;
            int chosenEvent = 0;
            do {
                System.out.print("Which event would you like to edit : ");

                try {
                    chosenEvent = scanner.nextInt();
                    if (chosenEvent <= 0 || chosenEvent > eventManager.events.size()) {
                        enteredRight = false;
                        throw new OptionOutOfRangeException("The event you chose is out of range.");
                    } else {
                        enteredRight = true;
                    }
                } catch (OptionOutOfRangeException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } while (!enteredRight);
            int chosenField = 0;
            enteredRight = false;
            do {
                System.out.println("1 - Name");
                System.out.println("2 - Description");
                System.out.println("3 - Price per guest");
                System.out.println("4 - Number of participants");
                System.out.println("5 - Guest List");
                System.out.println("6 - Date");
                System.out.println("7 - Budget");
                System.out.print("What would you like to edit : ");

                try {
                    chosenField = scanner.nextInt();
                    if (chosenField <= 0 || chosenField > 7) {
                        enteredRight = false;
                        throw new OptionOutOfRangeException("The field you chose is out of range.");
                    } else {
                        enteredRight = true;
                    }
                } catch (OptionOutOfRangeException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

            } while (!enteredRight);

            boolean validInput = false;
            scanner.nextLine();
            while (!validInput) {
                switch (chosenField) {
                    case 1:
                        validInput = true;
                        System.out.print("Enter the new name : ");
                        String newName = scanner.nextLine();
                        eventManager.events.get(chosenEvent-1).name = newName;
                        System.out.println("New name set successfully.");
                        break;
                    case 2:
                        validInput = true;
                        System.out.print("Enter the new description : ");
                        String newDescription = scanner.nextLine();
                        eventManager.events.get(chosenEvent-1).description = newDescription;
                        System.out.println("New description set successfully.");
                        break;
                    case 3:
                        validInput = true;
                        enteredRight = false;
                        do {
                            try {
                                double pricePerGuest = 0;
                                System.out.print("Enter the price for guests to participate in the event : ");
                                pricePerGuest = scanner.nextDouble();
                                if (pricePerGuest < 0) {
                                    throw new NegativeNumberException("Please enter a positive number.");
                                } else {
                                    enteredRight = true;
                                    eventManager.events.get(chosenEvent-1).pricePerPerson = pricePerGuest;
                                    System.out.println("New price per person set successfully.");
                                }
                            } catch (NegativeNumberException e) {
                                System.out.println(e.getMessage());
                                scanner.nextLine();
                            } catch (Exception e) {
                                System.out.println("Please enter a number.");
                                scanner.nextLine();
                            }

                        } while (!enteredRight);
                        break;
                    case 4:
                        validInput = true;
                        int maxNumOfGuests = 0;
                        enteredRight = false;
                        do {
                            try {
                                System.out.print("Enter the total number of guests (if there is no maximum enter 0) : ");
                                maxNumOfGuests = scanner.nextInt();

                                if (maxNumOfGuests < 0) {
                                    throw new NegativeNumberException("Please enter a positive number.");
                                } else if (maxNumOfGuests < eventManager.events.get(chosenEvent-1).guestList.size()) {
                                    System.out.println("The guest list exceeds the total number of participants.");
                                } else {
                                    eventManager.events.get(chosenEvent-1).maxNumOfParticipants = maxNumOfGuests;
                                    System.out.println("New maximum number of guests set successfully.");
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
                        break;
                    case 5:
                        validInput = true;
                        ArrayList<Guest> guestList = new ArrayList<Guest>();
                        do {
                            try {

                                System.out.println("Please enter the directory of the file containing the guests (the file should be formatted as FIRST NAME,LAST NAME,AGE,DIET(VEGAN, VEGETARIAN OR NODIET)) : ");
                                String filePath = scanner.nextLine();
                                if (filePath.endsWith(".txt")) {
                                    List<String> lines = Files.readAllLines(Paths.get(filePath));
                                    int numOfPeopleInFile = lines.size();
                                    if (lines.size() > eventManager.events.get(chosenEvent-1).maxNumOfParticipants) {
                                        System.out.println("The file contains more guests than allowed.");
                                    } else {
                                        guestList.clear();
                                        for (String line : lines) {
                                            String[] data = line.split(",");
                                            if (data.length == 4) { // check if each line has 4 pieces of data
                                                String firstName = data[0].trim();
                                                String lastName = data[1].trim();
                                                int age = Integer.parseInt(data[2].trim());
                                                String dietPreference = data[3].trim();

                                                if (age <= 0) {
                                                    enteredRight = false;
                                                    throw new NegativeNumberException("Please make sure all of the ages in the file are valid.");
                                                }
                                                boolean guestBooked = false;

                                                for (int i = 0; i < eventManager.events.size(); i++) {
                                                    int comparisonResult = eventManager.events.get(chosenEvent-1).date.compareTo(eventManager.events.get(i).date);
                                                    for (Guest guestLoop : eventManager.events.get(i).guestList) {
                                                        if (guestLoop.firstName.equals(firstName) && guestLoop.lastName.equals(lastName) && comparisonResult == 0 && i != chosenEvent-1) {
                                                            System.out.println(guestLoop.firstName + " " + guestLoop.lastName + " already has an event scheduled for them on the same day!");
                                                            enteredRight = false;
                                                            guestBooked = true;
                                                        }
                                                    }
                                                }
                                                if (!guestBooked) {
                                                    if (guestList.contains(new Guest(firstName, lastName, age, new VeganDiet()))) {
                                                        enteredRight = false;
                                                        System.out.println(firstName + " " + lastName + " has been added twice!");
                                                    } else {
                                                        switch (dietPreference.toLowerCase()) {
                                                            case "vegan":
                                                                Guest guest = new Guest(firstName, lastName, age, new VeganDiet());
                                                                guestList.add(guest);
                                                                enteredRight = true;
                                                                break;
                                                            case "nodiet":
                                                                Guest guest2 = new Guest(firstName, lastName, age, new GeneralDiet());
                                                                guestList.add(guest2);
                                                                enteredRight = true;
                                                                break;
                                                            case "vegetarian":
                                                                Guest guest3 = new Guest(firstName, lastName, age, new VegetarianDiet());
                                                                guestList.add(guest3);
                                                                enteredRight = true;
                                                                break;
                                                            default:
                                                                throw new InvalidDietException("Please make sure all diets are correctly formatted (VEGAN, VEGETARIAN OR NODIET)");

                                                        }
                                                    }
                                                }
                                            } else {
                                                enteredRight = false;
                                                throw new InvalidFileStructureException("There was an error reading the file contents. Please make sure the file is correctly formatted.");
                                            }
                                        }
                                    }


                                } else {
                                    throw new WrongFileExtensionException("Please enter a txt file.");
                                }

                            } catch (InvalidDietException e) {
                                System.out.println(e.getMessage());
                            } catch (NegativeNumberException e) {
                                System.out.println(e.getMessage());
                            } catch (InvalidFileStructureException e) {
                                System.out.println(e.getMessage());
                            } catch (WrongFileExtensionException e) {
                                System.out.println(e.getMessage());
                            } catch (Exception e) {
                                System.out.println("There was an error reading the file. Please make sure that the directory is correct.");

                            }
                        } while (!enteredRight);
                        eventManager.events.get(chosenEvent-1).guestList = guestList;
                        System.out.println("New guest list set succesfully.");
                        break;
                    case 6:
                        validInput = true;
                        LocalDate today = LocalDate.now();
                        LocalDate date = LocalDate.now();
                        enteredRight = false;
                        do {
                            try {
                                System.out.println("When is the event (dd/mm/yyyy) : ");
                                String input = scanner.nextLine();
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/M/yyyy");
                                date = LocalDate.parse(input, formatter);
                                if (DateValidater.isValidDate(date.getYear(), date.getMonthValue(), date.getDayOfMonth())) {

                                    if (date.isBefore(LocalDate.now())) {
                                        System.out.println("Please don't enter a date before now.");
                                    } else {
                                        boolean guestBooked = false;

                                        Map<String, List<LocalDate>> guestSchedule = new HashMap<>();

                                        for (Event event : eventManager.events) {
                                            LocalDate eventDate = event.date;

                                            for (Guest guest : event.guestList) {
                                                String guestKey = guest.firstName + " " + guest.lastName;

                                                List<LocalDate> scheduledDates = guestSchedule.getOrDefault(guestKey, new ArrayList<>());

                                                if (scheduledDates.contains(eventDate)) {
                                                    System.out.println(guest.firstName + " " + guest.lastName + " already has an event scheduled for them on that day!");
                                                    guestBooked = true;
                                                } else {
                                                    scheduledDates.add(eventDate);
                                                    guestSchedule.put(guestKey, scheduledDates);
                                                }
                                            }
                                        }
                                        if (!guestBooked) {
                                            date = LocalDate.parse(input, formatter);
                                            eventManager.events.get(chosenEvent-1).date = date;
                                            enteredRight = true;
                                            System.out.println("New date set successfully.");
                                        }
                                    }
                                } else {
                                    throw new InvalidDateException("Please enter a valid date.");
                                }
                            } catch (InvalidDateException e) {
                                System.out.println(e.getMessage());
                            }
                            catch(Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } while (!enteredRight);
                        break;
                    case 7:
                        boolean enteredRight2 = false;
                        int budget = 0;
                        Scanner scanner2 = new Scanner(System.in);
                        do {
                            try {
                                System.out.print("Please enter the budget for the event: ");
                                if (!scanner2.hasNextInt()) {
                                    System.out.println("Invalid input, please enter a valid number.");
                                    scanner2.next(); // Clear the invalid input
                                    continue;
                                }
                                budget = scanner2.nextInt();
                                scanner2.nextLine(); // Clear the newline character
                                if (budget < 0) {
                                    throw new NegativeNumberException("Please enter a positive number.");
                                } else {
                                    double pricePerGuest = eventManager.events.get(chosenEvent - 1).pricePerPerson;
                                    maxNumOfGuests = eventManager.events.get(chosenEvent - 1).maxNumOfParticipants;
                                    double totalMoney = budget + maxNumOfGuests * pricePerGuest;
                                    double totalExpense = 0;

                                    for (Guest guest : eventManager.events.get(chosenEvent - 1).guestList) {
                                        if (guest != null) {
                                            totalExpense += guest.diet.getPrice();
                                        }
                                    }

                                    if (totalMoney < totalExpense) {
                                        System.out.println("The total budget + price per guest is not enough to cover the expenses!");
                                        System.out.printf("$%.2f budget vs $%.2f expenses\n", totalMoney, totalExpense);
                                    } else {
                                        eventManager.events.get(chosenEvent - 1).budget = budget;
                                        enteredRight2 = true; // Set flag to true to exit the loop
                                        System.out.println("Budget set successfully.");

                                    }
                                }
                            } catch (NegativeNumberException e) {
                                System.out.println(e.getMessage());
                            } catch (Exception e) {
                                System.out.println("Invalid input, please try again.");
                                if (scanner2.hasNext()) {
                                    scanner2.next(); // Clear the invalid input
                                }
                            }
                        } while (!enteredRight2);
                        validInput = true;
                        break;
                    default:
                        validInput = false;

                }
            }

        }
    }


    /**
     * It accesses telegram's api in order to send a message to a group chat
     */
    public static void publishEventToTelegram() {
        if (eventManager.events.size() == 0) {
            System.out.println("You don't have any events!");
        } else {

            boolean enteredRight = false;
            do {
                try {

                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Which event would you like to publicize : ");
                    int i = 0;
                    for (i = 0; i < eventManager.events.size(); i++) {
                        System.out.println(i+1  + "- " + eventManager.events.get(i).name);
                    }
                    int num = scanner.nextInt();
                    if (num <= 0 || num >= i+1) {
                        System.out.println("The number you entered is out of range.");
                        enteredRight = false;
                    } else {
                        scanner.nextLine();
                        enteredRight = true;
                        System.out.println("Enter the Chat ID:");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                        String formattedDate = eventManager.events.get(i-1).date.format(formatter);
                        String chatId = scanner.nextLine();  // Read chat ID from the console
                        String messageText = String.format("Hello everyone! This is a reminder that the event %s will take place at %s, and that the price per guest is $%.2f", eventManager.events.get(i-1).name, eventManager.events.get(i-1).date, eventManager.events.get(i-1).pricePerPerson);

                        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
                        SendTelegramMessage bot = new SendTelegramMessage();
                        botsApi.registerBot(bot);
                        bot.sendMessage(chatId, messageText);
                    }


                } catch (TelegramApiException exception) {
                    System.out.println(exception.getMessage());
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            } while (!enteredRight);

        }

    }

    /**
     * This just calls the EventManager.viewEvents(), which in turn loops through the events and prints them out
     */
    public static void viewFutureEvents() {
        eventManager.viewEvents();
    }

    /**
     * Deletes events, removes them from the eventManager events arraylist
     */
    public static void deleteEvents() {
        if (eventManager.events.size() == 0) {
            System.out.println("You don't have any events scheduled.");
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Current events : ");
            for (int i = 0; i < eventManager.events.size(); i++) {
                System.out.println(i + 1 + " - " + eventManager.events.get(i).name);
            }
            boolean enteredRight = false;
            do {
                System.out.print("Which event would you like to delete : ");
                if (!scanner.hasNextInt()) {
                    System.out.println("Please enter a number.");
                } else {
                    int deleteEvent = scanner.nextInt();
                    if (deleteEvent <= 0 || deleteEvent > eventManager.events.size()) {
                        System.out.println("Please enter a number within the range.");
                    } else {
                        enteredRight = true;
                        eventManager.events.remove(deleteEvent-1);
                        System.out.println("Event removed successfully.");
                    }
                }
            } while (!enteredRight);


        }
    }


    /**
     * Makes tickets for the event
     * The tickets include the first name, last name, date and event name, which they can then print and bring to the event
     * The tickets are stored into any folder that the user would like
     */
    public static void makeTickets() {
        Scanner scanner = new Scanner(System.in);
        boolean enteredRight = false;
        String sourcePath = "C:\\Users\\18180017\\Desktop\\CEGEP\\Sem2\\Programming\\FinalProject\\TicketTemplate.txt";
        System.out.println("Which event would you like to make tickets for  : ");
        Event event = eventManager.events.get(0);
        do {
            int i = 0;
            for (i = 0; i < eventManager.events.size(); i++) {
                System.out.println(i+1  + "- " + eventManager.events.get(i).name);
            }

            int num = scanner.nextInt();
            if (num <= 0 || num >= i+1) {
                System.out.println("The number you entered is out of range.");
                enteredRight = false;
            } else {
                event = eventManager.events.get(num-1);
                enteredRight = true;
            }
        } while(!enteredRight);
        enteredRight = false;


        if (eventManager.events.size() == 0) {
            System.out.println("You don't have any events.");
            enteredRight = false;
        } else {
            do {
                scanner.nextLine();
                System.out.println("Enter the folder path to store the tickets: ");
                String directoryPath = scanner.nextLine();

                File directory = new File(directoryPath);
                if (directory.exists() && directory.isDirectory()) {
                    enteredRight = true;
                    for (Guest guest : event.guestList) {
                        try (
                                BufferedWriter writer = new BufferedWriter(new FileWriter(directoryPath+"/"+guest.firstName+guest.lastName+".txt"))) {


                            String rectangle =
                                    "+--------------------------------------------------+\n" +
                                            "\t\t" + guest.firstName + "\n" +
                                            "\t\t" + guest.lastName + "\n" +
                                            "\t\t" + event.name + "\n" +
                                            "\t\t" + event.date + "\n"+
                                    "+--------------------------------------------------+";





                            writer.write(rectangle);
                            writer.newLine();


                        } catch (IOException e) {
                            System.out.println("An error occurred: " + e.getMessage());
                        }
                    }


                } else {
                    System.out.println("Directory does not exist or is not a directory.");
                    enteredRight = false;
                }

            } while (!enteredRight);
        }

        System.out.println("The tickets have been succesfully made!");
    }


    /**
     * Searches for the name of any event that the user enters. Uses Linear search
     */
    public static void searchForEvent() {
        if (eventManager.events.size() == 0) {
            System.out.println("You don't have any events.");

        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.print("What is the name of the event you want to search for : ");
            String name = scanner.nextLine();
            Event event = SearchingAlgorithms.linearSearch(eventManager.events,name);
            if (event != null) {
                System.out.println("Event found!");
                event.displayEventDetails();
            } else {
                System.out.println("There are no events with that name.");
            }
        }

    }

    /**
     * Searches for the name of any guest that the user enters
     * Uses hash based search
     */
    public static void searchForGuest() {
        if (eventManager.events.size() == 0) {
            System.out.println("You don't have any events.");
        } else {
            Scanner scanner = new Scanner(System.in);
            System.out.println("What is the first name of the guest : ");
            String firstName = scanner.nextLine();
            System.out.println("What is the last name of the guest : ");
            String lastName = scanner.nextLine();
            ArrayList<Guest> guests = new ArrayList<>();
            for (int i = 0; i < eventManager.events.size(); i++) {
                for (int j = 0; j < eventManager.events.get(i).guestList.size(); j++) {
                    guests.add(eventManager.events.get(i).guestList.get(j));
                }
            }
            Guest guest = SearchingAlgorithms.hashBasedSearch(guests, firstName + " " + lastName);
            if (guest != null) {
                System.out.println("Guest found!");
                System.out.println(guest);
            } else {
                System.out.println("No guest with that name is found.");
            }
        }

    }


}