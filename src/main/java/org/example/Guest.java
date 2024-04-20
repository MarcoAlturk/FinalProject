package org.example;

public class Guest {
    String firstName;
    String lastName;
    int age;
    Diet diet;


    public Guest(String firstName, String lastName, int age, Diet diet) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.diet = diet;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }


}
