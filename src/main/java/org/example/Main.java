package org.example;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import jdk.jshell.spi.ExecutionControlProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
public class Main {
    private static EventManager eventManager = new EventManager();
    private static final Logger logger = LoggerFactory.getLogger(SendTelegramMessage.class);
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
                        "7 - Exit");
                System.out.print("Please enter what you would like to do : ");
                input = scanner.nextInt();

                if (input < 1 || input > 7) {
                    throw new OptionOutOfRangeException("Please enter a number from 1-7.");
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
                        break;
                }

            } catch(OptionOutOfRangeException e) {
                System.out.println(e.getMessage());
                scanner.nextLine(); // clear the buffer
            } catch(Exception e) {
                System.out.println("exception : " + e.getMessage());
                System.out.println("Please enter a number.");
                scanner.nextLine(); // clear the buffer
            }
        } while(input != 7); // keep looping until they exit

    }

    public static void makeNewEvent() { // make a new event
        String name;
        String description;
        int maxNumOfGuests = 0;
        double pricePerGuest = 0;
        LocalDate date = LocalDate.now();


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
        scanner.nextLine();
        enteredRight = false;
        int numOfPeopleInFile = 0;
        ArrayList<Guest> guestList = new ArrayList<Guest>();
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
                                enteredRight = true;
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

        LocalDate today = LocalDate.now();

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

        Event event = new Event(name, description, maxNumOfGuests, pricePerGuest, guestList, date, budget);
        eventManager.add(event);
    }

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
                        break;
                    case 2:
                        validInput = true;
                        System.out.print("Enter the new description : ");
                        String newDescription = scanner.nextLine();
                        eventManager.events.get(chosenEvent-1).description = newDescription;
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
                                    System.out.println("The guest list exceeds the total numbr of participants.");
                                } else {
                                    eventManager.events.get(chosenEvent-1).maxNumOfParticipants = maxNumOfGuests;
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
                                                enteredRight = true;
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
                                        date = LocalDate.parse(input, formatter);
                                        eventManager.events.get(chosenEvent-1).date = date;
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
                        break;
                    case 7:
                        boolean enteredRight2 = false;
                        int budget = 0;

                        while (!enteredRight2) {
                            try {
                                System.out.print("Please enter the budget for the event: ");
                                if (!scanner.hasNextInt()) {
                                    System.out.println("Invalid input, please enter a valid number.");
                                    scanner.next(); // Clear the invalid input
                                    continue;
                                }
                                budget = scanner.nextInt();
                                scanner.nextLine(); // Clear the newline character
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
                                if (scanner.hasNext()) {
                                    scanner.next(); // Clear the invalid input
                                }
                            }
                        }
                    default:
                        validInput = false;

                }
            }

        }
    }



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

    public static void viewFutureEvents() {
        int i = 1;
        for (Event event : eventManager.events) {
            System.out.println("1. " + event.name + " - " + event.description + " at " + event.date);
        }
    }

    public static void deleteEvents() {

    }

    public static void makeTickets() {
        Scanner scanner = new Scanner(System.in);
        boolean enteredRight = false;
        String sourcePath = "C:\\Users\\18180017\\Desktop\\CEGEP\\Sem2\\Programming\\FinalProject\\TicketTemplate.txt";
        System.out.println("Which event would you like to publicize : ");
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




}