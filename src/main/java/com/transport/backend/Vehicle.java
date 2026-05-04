package com.transport.backend;

import com.transport.backend.exceptions.InsufficientFuelException;

public abstract class Vehicle {
    private String plateNumber;
    private int fuelLevel;
    private static final int MIN_FUEL_TO_MOVE = 10;

    public Vehicle(String plateNumber, int fuelLevel) {
        if (plateNumber == null || plateNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Plate number cannot be null or empty");
        }
        if (fuelLevel < 0) {
            throw new IllegalArgumentException("Fuel level cannot be negative");
        }
        this.plateNumber = plateNumber;
        this.fuelLevel = fuelLevel;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public int getFuelLevel() {
        return fuelLevel;
    }

    protected void consumeFuel(int amount) throws InsufficientFuelException {
        if (fuelLevel < amount) {
            throw new InsufficientFuelException("Insufficient fuel. Current: " + fuelLevel + ", Required: " + amount);
        }
        fuelLevel -= amount;
    }

    public void refuel(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Refuel amount must be positive");
        }
        fuelLevel += amount;
        System.out.println("Refueled. Fuel level: " + fuelLevel);
    }

    protected void checkFuelForMovement() throws InsufficientFuelException {
        if (fuelLevel < MIN_FUEL_TO_MOVE) {
            throw new InsufficientFuelException("Not enough fuel to move. Minimum required: " + MIN_FUEL_TO_MOVE);
        }
    }

    public abstract void move() throws InsufficientFuelException;

    public void displayInfo() {
        System.out.println("Vehicle Plate: " + plateNumber + ", Fuel: " + fuelLevel);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Vehicle vehicle = (Vehicle) obj;
        return plateNumber.equals(vehicle.plateNumber);
    }

    @Override
    public int hashCode() {
        return plateNumber.hashCode();
    }
}
