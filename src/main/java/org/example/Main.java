package org.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Main {
    private static EventManager eventManager = new EventManager();
    public static void main(String[] args) {
        System.out.println("Hello and welcome to the event manager!");

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
                    double totalMoney = budget + numOfPeopleInFile * pricePerGuest;
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

        Event event = new Event(name, description, maxNumOfGuests, pricePerGuest, guestList, date, budget);
        eventManager.add(event);

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