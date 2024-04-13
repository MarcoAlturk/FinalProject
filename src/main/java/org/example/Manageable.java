package org.example;

public interface Manageable<T> {
    void add(T item);
    void remove(T item);
    boolean update(T oldItem, T newItem);
}
