package com.transport.backend;

import com.transport.backend.exceptions.InsufficientFuelException;
import com.transport.backend.exceptions.VehicleNotAvailableException;

public class Taxi extends Vehicle {
    private String driverName;
    private boolean available;
    private Passenger currentPassenger;
    private String currentZone;
    private double totalEarnings;
    private int completedTrips;

    public Taxi(String plateNumber, int fuelLevel, String driverName, boolean available) {
        super(plateNumber, fuelLevel);
        if (driverName == null || driverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Driver name cannot be null or empty");
        }
        this.driverName = driverName;
        this.available = available;
        this.currentPassenger = null;
        this.currentZone = "Central";
        this.totalEarnings = 0.0;
        this.completedTrips = 0;
    }

    public boolean isAvailable() {
        return available && getFuelLevel() > 10;
    }

    public String getDriverName() {
        return driverName;
    }

    public Passenger getCurrentPassenger() {
        return currentPassenger;
    }

    public String getCurrentZone() {
        return currentZone;
    }

    public void setCurrentZone(String zone) {
        if (zone != null && !zone.trim().isEmpty()) {
            this.currentZone = zone;
        }
    }

    public double getTotalEarnings() {
        return totalEarnings;
    }

    public int getCompletedTrips() {
        return completedTrips;
    }

    @Override
    public void move() throws InsufficientFuelException {
        checkFuelForMovement();
        consumeFuel(5);
    }

    public void acceptBooking(Passenger p, Route r) throws VehicleNotAvailableException, InsufficientFuelException {
        if (p == null || r == null) {
            throw new IllegalArgumentException("Passenger and route cannot be null");
        }
        
        if (!available) {
            throw new VehicleNotAvailableException("Taxi " + getPlateNumber() + " is already booked");
        }

        if (p.isOnTrip()) {
            throw new VehicleNotAvailableException("Passenger " + p.getName() + " is already on a trip");
        }
        
        int requiredFuel = (int) Math.ceil(r.getDistance() / 10);
        if (getFuelLevel() < requiredFuel) {
            throw new InsufficientFuelException("Insufficient fuel for this route. Need " + requiredFuel + " liters");
        }

        available = false;
        currentPassenger = p;
        p.startTrip(this);
        consumeFuel(requiredFuel);
    }

    public void completeTrip() {
        if (currentPassenger != null) {
            currentPassenger.endTrip();
            currentPassenger = null;
            available = true;
            completedTrips++;
            totalEarnings += 15.0 + (Math.random() * 20);
        }
    }

    @Override
    public String toString() {
        String status = available ? "AVAILABLE" : "BUSY";
        String passengerInfo = currentPassenger != null ? " | Passenger: " + currentPassenger.getName() : "";
        return getPlateNumber() + " - Driver: " + driverName + " [" + status + "] | Zone: " + currentZone + 
               " | Fuel: " + getFuelLevel() + "L | Trips: " + completedTrips + passengerInfo;
    }
}
