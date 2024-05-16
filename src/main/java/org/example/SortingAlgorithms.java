package org.example;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SortingAlgorithms {
    public static void quickSort(ArrayList<Guest> list, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(list, left, right);
            quickSort(list, left, pivotIndex - 1);
            quickSort(list, pivotIndex + 1, right);
        }
    }

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

    public static void mergeSort(int[] array) {
        if (array.length <= 1) {
            return;
        }

        int midpoint = array.length / 2;
        int[] leftArray = Arrays.copyOfRange(array, 0, midpoint);
        int[] rightArray = Arrays.copyOfRange(array, midpoint, array.length);

        mergeSort(leftArray);
        mergeSort(rightArray);

        merge(array, leftArray, rightArray);
    }

    private static void merge(int[] result, int[] leftArray, int[] rightArray) {
        int i = 0, j = 0, k = 0;

        while (i < leftArray.length && j < rightArray.length) {
            if (leftArray[i] <= rightArray[j]) {
                result[k++] = leftArray[i++];
            } else {
                result[k++] = rightArray[j++];
            }
        }

        while (i < leftArray.length) {
            result[k++] = leftArray[i++];
        }

        while (j < rightArray.length) {
            result[k++] = rightArray[j++];
        }
    }


}
