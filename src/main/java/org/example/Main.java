package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello and welcome to the event manager!");
        System.out.println("Please enter what you would like to do : ");
        System.out.println("1 - Organize a new event.\n" +
                            "2 - Edit an existing event.\n" +
                            "3 - Publish an event to telegram.\n" +
                            "4 - View all the scheduled events.\n" +
                            "5 - Delete events.\n" +
                            "6 - Print tickets for guests.\n" +
                            "7 - Exit");

        Scanner scanner = new Scanner(System.in);
        try {
            int input = scanner.nextInt();

        } catch(Exception e) {
            System.out.println("Please enter a valid number from 1-7.");
        }

    }
}