package org.example;

import java.util.Objects;

/**
 * Guest class, holds firstName, lastName, age and Diet
 */
public class Guest {
    String firstName;
    String lastName;
    int age;
    Diet diet;


    /**
     * All-args constructor
     * @param firstName Guest's first name
     * @param lastName Guest's last name
     * @param age Guest's age
     * @param diet Guest's diet
     */
    public Guest(String firstName, String lastName, int age, Diet diet) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.diet = diet;
    }

    /**
     * Getter for the first name
     * @return the first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * getter for the last name
     * @return the last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * getter for the age
     * @return the age
     */
    public int getAge() {
        return age;
    }


    /**
     * Setter for the first name
     * @param firstName (the new firstName)
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Setter for the last name
     * @param lastName (the new lastName)
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Setter for the age
     * @param age (the new age)
     */
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * ToString to print the guest's information
     * @return the info
     */
    @Override
    public String toString() {
        return "Guest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", diet=" + diet +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Guest guest = (Guest) o;
        return Objects.equals(firstName, guest.firstName) &&
                Objects.equals(lastName, guest.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName);
    }

}
