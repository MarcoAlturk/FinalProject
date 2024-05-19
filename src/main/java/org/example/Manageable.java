package org.example;

/**
 * Manageable generic interface, which Eventmanager implements
 * @param <T>
 */
public interface Manageable<T> {
    /**
     * Add an item
     * @param item
     */
    void add(T item);

    /**
     * remove an item
     * @param item
     */
    void remove(T item);

    /**
     * Update an item
     * @param oldItem
     * @param newItem
     * @return
     */
    boolean update(T oldItem, T newItem);
}
