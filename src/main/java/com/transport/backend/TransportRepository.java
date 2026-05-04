package com.transport.backend;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic repository class for type-safe storage and retrieval of any transport entity.
 * Demonstrates Generics for type safety and reusability.
 *
 * T - the type of entity this repository manages (e.g. Vehicle, Passenger, Route)
 */
public class TransportRepository<T> {

    private final List<T> items;
    private final String entityName;

    public TransportRepository(String entityName) {
        this.items = new ArrayList<>();
        this.entityName = entityName;
    }

    public boolean add(T item) {
        if (item == null) {
            throw new IllegalArgumentException(entityName + " cannot be null");
        }
        if (items.contains(item)) {
            return false;
        }
        items.add(item);
        return true;
    }

    public boolean remove(T item) {
        return items.remove(item);
    }

    public T get(int index) {
        if (index < 0 || index >= items.size()) {
            throw new IndexOutOfBoundsException("Index out of range for " + entityName);
        }
        return items.get(index);
    }

    public List<T> getAll() {
        return new ArrayList<>(items);
    }

    public boolean contains(T item) {
        return items.contains(item);
    }

    public int size() {
        return items.size();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public void clear() {
        items.clear();
    }

    @Override
    public String toString() {
        return entityName + " Repository [" + items.size() + " items]";
    }
}
