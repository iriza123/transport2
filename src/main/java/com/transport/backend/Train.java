package com.transport.backend;

import com.transport.backend.exceptions.InsufficientFuelException;
import java.util.*;

public class Train extends Vehicle {
    private String schedule;
    private int cars;
    private boolean delayed;
    private List<String> stops;
    private boolean inOperation;
    private int currentStopIndex;
    private int totalPassengerCapacity;

    public Train(String plateNumber, int fuelLevel, String schedule, int cars) {
        super(plateNumber, fuelLevel);
        if (schedule == null || schedule.trim().isEmpty()) {
            throw new IllegalArgumentException("Schedule cannot be null or empty");
        }
        if (cars <= 0) {
            throw new IllegalArgumentException("Number of cars must be positive");
        }
        this.schedule = schedule;
        this.cars = cars;
        this.delayed = false;
        this.stops = new ArrayList<>();
        this.inOperation = true;
        this.currentStopIndex = 0;
        this.totalPassengerCapacity = cars * 80;
    }

    public void addStop(String stop) {
        if (stop == null || stop.trim().isEmpty()) {
            throw new IllegalArgumentException("Stop name cannot be null or empty");
        }
        stops.add(stop);
    }

    public List<String> getStops() {
        return new ArrayList<>(stops);
    }

    public boolean removeStop(String stop) {
        return stops.remove(stop);
    }

    public String getSchedule() {
        return schedule;
    }

    public int getCars() {
        return cars;
    }

    public boolean isDelayed() {
        return delayed;
    }

    public boolean isInOperation() {
        return inOperation && getFuelLevel() > 30;
    }

    public void setInOperation(boolean inOperation) {
        this.inOperation = inOperation;
    }

    public String getCurrentStop() {
        if (stops.isEmpty()) {
            return "No stops defined";
        }
        return stops.get(currentStopIndex);
    }

    public int getTotalPassengerCapacity() {
        return totalPassengerCapacity;
    }

    @Override
    public void move() throws InsufficientFuelException {
        if (!inOperation) {
            throw new IllegalStateException("Train is not in operation");
        }
        
        if (delayed) {
            throw new IllegalStateException("Train is delayed and cannot move");
        }
        
        if (getFuelLevel() < 30) {
            throw new InsufficientFuelException("Train needs refueling before moving");
        }
        
        checkFuelForMovement();
        consumeFuel(20);
        
        if (!stops.isEmpty()) {
            currentStopIndex = (currentStopIndex + 1) % stops.size();
        }
    }

    public void delayTrain(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("Delay reason cannot be null or empty");
        }
        delayed = true;
    }

    public void resumeSchedule() {
        delayed = false;
    }

    @Override
    public String toString() {
        String status = delayed ? "DELAYED" : (inOperation ? "OPERATIONAL" : "OUT OF SERVICE");
        String stopInfo = !stops.isEmpty() ? " | Current Stop: " + getCurrentStop() : " | No stops";
        return getPlateNumber() + " [" + status + "] | Schedule: " + schedule + " | Cars: " + cars + 
               " | Capacity: " + totalPassengerCapacity + " | Fuel: " + getFuelLevel() + "L" + stopInfo;
    }
}
