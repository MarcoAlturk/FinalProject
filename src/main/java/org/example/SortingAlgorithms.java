package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Class to hold the storing algorithms
 */
public class SortingAlgorithms {
    /**
     * Quicksort Algorithm
     * @param list the guest arraylist
     * @param left the left bound
     * @param right the right bound
     */
    public static void quickSort(ArrayList<Guest> list, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(list, left, right);
            quickSort(list, left, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, right);
        }
    }

    /**
     * Quicksort method partition, in order to order the two lists
     * @param list the list
     * @param left the left bound
     * @param right the right bound
     * @return
     */
    private static int partition(ArrayList<Guest> list, int left, int right) {
        Guest pivot = list.get(right);
        int i = left - 1;
        for (int j = left; j < right; j++) {
            if (list.get(j).age <= pivot.age) {
                i++;
                Collections.swap(list, i, j);
            }
        }
        Collections.swap(list, i + 1, right);
        return i + 1;
    }

    /**
     * Merge sort method
     * @param list the event arraylist to sort
     */
    public static void mergeSort(ArrayList<Event> list) {
        if (list.size() <= 1) {
            return;
        }

        // Split the array into two halves
        ArrayList<Event> left = new ArrayList<>();
        ArrayList<Event> right = new ArrayList<>();

        int middle = list.size() / 2;

        for (int i = 0; i < middle; i++) {
            left.add(list.get(i));
        }

        for (int i = middle; i < list.size(); i++) {
            right.add(list.get(i));
        }

        // Recursively sort the two halves
        mergeSort(left);
        mergeSort(right);

        // Merge the sorted halves
        merge(list, left, right);
    }

    /**
     * Merge method, to merge a left and right arraylist
     * @param list general list
     * @param left left list
     * @param right right list
     */
    private static void merge(ArrayList<Event> list, ArrayList<Event> left, ArrayList<Event> right) {
        int leftIndex = 0, rightIndex = 0, listIndex = 0;

        while (leftIndex < left.size() && rightIndex < right.size()) {
            if (left.get(leftIndex).date.compareTo(right.get(rightIndex).date) < 0) {
                list.set(listIndex++, left.get(leftIndex++));
            } else {
                list.set(listIndex++, right.get(rightIndex++));
            }
        }

        while (leftIndex < left.size()) {
            list.set(listIndex++, left.get(leftIndex++));
        }

        while (rightIndex < right.size()) {
            list.set(listIndex++, right.get(rightIndex++));
        }
    }


}
