package org.example;

/**
 * Interface to encompass Event and Conference
 */
public interface Budgetable {
    /**
     * Set the budget for the event
     * @param budget
     */
    void setBudget(double budget);

    /**
     * get the budget from the event
     * @return
     */
    double getBudget();
}
