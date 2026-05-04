package com.transport.backend;

import com.transport.backend.exceptions.InsufficientFuelException;

public class Passenger {
    private String name;
    private String id;
    private boolean onTrip;
    private Vehicle currentVehicle;

    public Passenger(String name, String id) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Passenger name cannot be null or empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Passenger ID cannot be null or empty");
        }
        this.name = name;
        this.id = id;
        this.onTrip = false;
        this.currentVehicle = null;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public boolean isOnTrip() {
        return onTrip;
    }

    public Vehicle getCurrentVehicle() {
        return currentVehicle;
    }

    public void startTrip(Vehicle vehicle) {
        if (onTrip) {
            throw new IllegalStateException("Passenger " + name + " is already on a trip");
        }
        this.onTrip = true;
        this.currentVehicle = vehicle;
    }

    public void endTrip() {
        this.onTrip = false;
        this.currentVehicle = null;
    }

    public void bookTrip(Vehicle v) throws InsufficientFuelException {
        if (v == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        if (onTrip) {
            throw new IllegalStateException("Passenger is already on a trip");
        }
        v.move();
    }

    public String toFileString() {
        return id + "|" + name;
    }

    public static Passenger fromFileString(String fileString) {
        String[] parts = fileString.split("\\|");
        if (parts.length == 2) {
            return new Passenger(parts[1], parts[0]);
        }
        throw new IllegalArgumentException("Invalid passenger file format");
    }

    @Override
    public String toString() {
        String status = onTrip ? " [ON TRIP]" : " [AVAILABLE]";
        return name + " (ID: " + id + ")" + status;
    }
}
