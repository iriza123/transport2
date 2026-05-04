package com.transport.backend;

import com.transport.backend.exceptions.CapacityExceededException;
import com.transport.backend.exceptions.InsufficientFuelException;
import java.util.ArrayList;
import java.util.List;

public class Bus extends Vehicle {
    private int capacity;
    private Route route;
    private List<Passenger> passengers;
    private boolean inService;
    private String currentStop;

    public Bus(String plateNumber, int fuelLevel, int capacity, Route route) {
        super(plateNumber, fuelLevel);
        if (capacity <= 0) {
            throw new IllegalArgumentException("Bus capacity must be positive");
        }
        if (route == null) {
            throw new IllegalArgumentException("Route cannot be null");
        }
        this.capacity = capacity;
        this.route = route;
        this.passengers = new ArrayList<>();
        this.inService = true;
        this.currentStop = route.getOrigin();
    }

    public void addPassenger(Passenger p) throws CapacityExceededException {
        if (p == null) {
            throw new IllegalArgumentException("Passenger cannot be null");
        }
        
        if (!inService) {
            throw new IllegalStateException("Bus is not in service");
        }
        
        if (p.isOnTrip()) {
            throw new IllegalStateException("Passenger " + p.getName() + " is already on a trip");
        }
        
        if (passengers.size() >= capacity) {
            throw new CapacityExceededException("Bus is full. Capacity: " + capacity);
        }
        
        passengers.add(p);
        p.startTrip(this);
    }

    public void removePassenger(Passenger p) {
        if (passengers.remove(p)) {
            p.endTrip();
        }
    }

    public List<Passenger> getPassengers() {
        return new ArrayList<>(passengers);
    }

    public int getPassengerCount() {
        return passengers.size();
    }

    public int getAvailableSeats() {
        return capacity - passengers.size();
    }

    public int getCapacity() {
        return capacity;
    }

    public Route getRoute() {
        return route;
    }

    public boolean isInService() {
        return inService && getFuelLevel() > 20;
    }

    public void setInService(boolean inService) {
        if (!inService && !passengers.isEmpty()) {
            throw new IllegalStateException("Cannot take bus out of service with passengers on board");
        }
        this.inService = inService;
    }

    public String getCurrentStop() {
        return currentStop;
    }

    public void setCurrentStop(String stop) {
        this.currentStop = stop;
    }

    @Override
    public void move() throws InsufficientFuelException {
        if (!inService) {
            throw new IllegalStateException("Bus is not in service");
        }
        
        if (getFuelLevel() < 20) {
            throw new InsufficientFuelException("Bus needs refueling before moving");
        }
        
        checkFuelForMovement();
        consumeFuel(15);
        
        if (currentStop.equals(route.getOrigin())) {
            currentStop = route.getDestination();
        } else {
            currentStop = route.getOrigin();
        }
    }

    @Override
    public String toString() {
        String status = inService ? "IN SERVICE" : "OUT OF SERVICE";
        return getPlateNumber() + " [" + status + "] | Route: " + route.getOrigin() + " → " + route.getDestination() + 
               " | Passengers: " + passengers.size() + "/" + capacity + " | Fuel: " + getFuelLevel() + "L | At: " + currentStop;
    }
}
