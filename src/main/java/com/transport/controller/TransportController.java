package com.transport.controller;

import com.transport.backend.*;
import com.transport.backend.exceptions.*;
import com.transport.backend.io.DataPersistence;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class TransportController {

    private TransportManager manager;
    private ObservableList<Vehicle> vehicleList;
    private ObservableList<Passenger> passengerList;
    private ObservableList<String> routeList;
    private ObservableList<String> logList;

    public TransportController() {
        this.manager = new TransportManager();
        this.vehicleList = FXCollections.observableArrayList();
        this.passengerList = FXCollections.observableArrayList();
        this.routeList = FXCollections.observableArrayList();
        this.logList = FXCollections.observableArrayList();
    }

    // ── VEHICLE ──────────────────────────────────────────────────────────────

    public String addVehicle(String type, String plateNumber, int fuelLevel, String extra1, String extra2) {
        try {
            Vehicle vehicle = null;
            switch (type) {
                case "Bus":
                    int capacity = Integer.parseInt(extra1);
                    Route route = manager.getRoute(extra2);
                    if (route == null) return "Error: Route '" + extra2 + "' not found. Add route first.";
                    vehicle = new Bus(plateNumber, fuelLevel, capacity, route);
                    break;
                case "Taxi":
                    vehicle = new Taxi(plateNumber, fuelLevel, extra1, true);
                    break;
                case "Train":
                    int cars = Integer.parseInt(extra2);
                    vehicle = new Train(plateNumber, fuelLevel, extra1, cars);
                    break;
            }
            if (vehicle != null && manager.registerVehicle(vehicle)) {
                updateVehicleList();
                log("Vehicle added: " + plateNumber + " [" + type + "]");
                return "Success: " + type + " " + plateNumber + " added";
            }
            return "Error: Vehicle " + plateNumber + " already exists";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String removeVehicle(Vehicle vehicle) {
        if (vehicle == null) return "Error: No vehicle selected";
        if (manager.unregisterVehicle(vehicle)) {
            updateVehicleList();
            log("Vehicle removed: " + vehicle.getPlateNumber());
            return "Success: Vehicle " + vehicle.getPlateNumber() + " removed";
        }
        return "Error: Vehicle not found";
    }

    public String searchVehicle(String plateNumber) {
        if (plateNumber == null || plateNumber.trim().isEmpty()) return "Error: Enter a plate number to search";
        for (Vehicle v : manager.getAllVehicles()) {
            if (v.getPlateNumber().equalsIgnoreCase(plateNumber.trim())) {
                return "Found: " + v.toString();
            }
        }
        return "Error: No vehicle found with plate: " + plateNumber;
    }

    public void sortVehiclesByPlate() {
        Platform.runLater(() -> {
            List<Vehicle> sorted = new ArrayList<>(vehicleList);
            sorted.sort(Comparator.comparing(Vehicle::getPlateNumber));
            vehicleList.setAll(sorted);
        });
    }

    public void sortVehiclesByFuel() {
        Platform.runLater(() -> {
            List<Vehicle> sorted = new ArrayList<>(vehicleList);
            sorted.sort(Comparator.comparingInt(Vehicle::getFuelLevel).reversed());
            vehicleList.setAll(sorted);
        });
    }

    public ObservableList<Vehicle> getVehicleList() { return vehicleList; }

    private void updateVehicleList() {
        Platform.runLater(() -> {
            vehicleList.clear();
            vehicleList.addAll(manager.getAllVehicles());
        });
    }

    // ── PASSENGER ────────────────────────────────────────────────────────────

    public String addPassenger(String name, String id) {
        try {
            Passenger passenger = new Passenger(name, id);
            manager.addPassenger(passenger);
            updatePassengerList();
            log("Passenger added: " + name + " [" + id + "]");
            return "Success: Passenger " + name + " added";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String removePassenger(Passenger passenger) {
        if (passenger == null) return "Error: No passenger selected";
        if (passenger.isOnTrip()) return "Error: Cannot remove passenger currently on a trip";
        if (manager.removePassenger(passenger)) {
            updatePassengerList();
            log("Passenger removed: " + passenger.getName());
            return "Success: Passenger " + passenger.getName() + " removed";
        }
        return "Error: Passenger not found";
    }

    public String searchPassenger(String id) {
        if (id == null || id.trim().isEmpty()) return "Error: Enter a passenger ID to search";
        Passenger p = manager.findPassengerById(id.trim());
        if (p != null) return "Found: " + p.toString();
        return "Error: No passenger found with ID: " + id;
    }

    public void sortPassengersByName() {
        Platform.runLater(() -> {
            List<Passenger> sorted = new ArrayList<>(passengerList);
            sorted.sort(Comparator.comparing(Passenger::getName));
            passengerList.setAll(sorted);
        });
    }

    public ObservableList<Passenger> getPassengerList() { return passengerList; }

    private void updatePassengerList() {
        Platform.runLater(() -> {
            passengerList.clear();
            passengerList.addAll(manager.getAllPassengers());
        });
    }

    // ── ROUTE ────────────────────────────────────────────────────────────────

    public String addRoute(String routeId, String origin, String destination, double distance) {
        try {
            if (routeId == null || routeId.trim().isEmpty()) return "Error: Route ID cannot be empty";
            if (manager.hasRoute(routeId)) return "Error: Route ID '" + routeId + "' already exists";
            Route route = new Route(origin, destination, distance);
            manager.addRoute(routeId, route);
            updateRouteList();
            log("Route added: " + routeId + " [" + origin + " to " + destination + ", " + distance + "km]");
            return "Success: Route " + routeId + " added (" + origin + " to " + destination + ")";
        } catch (InvalidRouteException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String removeRoute(String routeId) {
        if (routeId == null || routeId.trim().isEmpty()) return "Error: No route selected";
        if (manager.removeRoute(routeId) != null) {
            updateRouteList();
            log("Route removed: " + routeId);
            return "Success: Route " + routeId + " removed";
        }
        return "Error: Route not found";
    }

    public String searchRoute(String routeId) {
        if (routeId == null || routeId.trim().isEmpty()) return "Error: Enter a route ID to search";
        Route r = manager.getRoute(routeId.trim());
        if (r != null) return "Found: " + routeId + " - " + r.toString();
        return "Error: No route found with ID: " + routeId;
    }

    public void sortRoutesByDistance() {
        Platform.runLater(() -> {
            List<String> sorted = new ArrayList<>(routeList);
            sorted.sort((a, b) -> {
                double da = extractDistance(a);
                double db = extractDistance(b);
                return Double.compare(da, db);
            });
            routeList.setAll(sorted);
        });
    }

    private double extractDistance(String routeStr) {
        try {
            String part = routeStr.substring(routeStr.lastIndexOf("(") + 1, routeStr.lastIndexOf(" km)"));
            return Double.parseDouble(part);
        } catch (Exception e) {
            return 0;
        }
    }

    public ObservableList<String> getRouteList() { return routeList; }

    private void updateRouteList() {
        Platform.runLater(() -> {
            routeList.clear();
            for (String routeId : manager.getAllRouteIds()) {
                Route route = manager.getRoute(routeId);
                routeList.add(routeId + ": " + route.toString());
            }
        });
    }

    // ── TAXI ─────────────────────────────────────────────────────────────────

    public ObservableList<String> getTaxisInZone(String zone) {
        ObservableList<String> taxiList = FXCollections.observableArrayList();
        for (Taxi taxi : manager.getTaxisInZone(zone)) {
            taxiList.add(taxi.toString());
        }
        return taxiList;
    }

    public String bookTaxi(Passenger passenger, String zone, Route route) {
        try {
            if (passenger == null) return "Error: No passenger selected";
            if (route == null) return "Error: No route selected";
            if (passenger.isOnTrip()) return "Error: " + passenger.getName() + " is already on a trip";
            List<Taxi> available = manager.getAvailableTaxisInZone(zone);
            if (available.isEmpty()) return "Error: No available taxis in zone " + zone;
            Taxi taxi = available.get(0);
            taxi.acceptBooking(passenger, route);
            updateVehicleList();
            updatePassengerList();
            log("Taxi booked: " + taxi.getPlateNumber() + " for " + passenger.getName() + " on route " + route);
            return "Success: Taxi " + taxi.getPlateNumber() + " booked for " + passenger.getName();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String completeTaxiTrip(Taxi taxi) {
        try {
            if (taxi == null) return "Error: No taxi selected";
            if (taxi.isAvailable()) return "Error: Taxi is not currently on a trip";
            String passengerName = taxi.getCurrentPassenger() != null ? taxi.getCurrentPassenger().getName() : "unknown";
            taxi.completeTrip();
            updateVehicleList();
            updatePassengerList();
            log("Trip completed: " + taxi.getPlateNumber() + " dropped off " + passengerName);
            return "Success: Trip completed. Taxi " + taxi.getPlateNumber() + " is now available";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ── BUS BOOKING ──────────────────────────────────────────────────────────

    public String boardBus(Passenger passenger, Bus bus) {
        try {
            if (passenger == null) return "Error: No passenger selected";
            if (bus == null) return "Error: No bus selected";
            if (passenger.isOnTrip()) return "Error: " + passenger.getName() + " is already on a trip";
            if (!bus.isInService()) return "Error: Bus " + bus.getPlateNumber() + " is not in service";
            bus.addPassenger(passenger);
            updateVehicleList();
            updatePassengerList();
            log("Passenger boarded: " + passenger.getName() + " on bus " + bus.getPlateNumber()
                    + " (" + bus.getPassengerCount() + "/" + bus.getCapacity() + ")");
            return "Success: " + passenger.getName() + " boarded bus " + bus.getPlateNumber()
                    + " [" + bus.getPassengerCount() + "/" + bus.getCapacity() + " seats]";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String alightBus(Passenger passenger, Bus bus) {
        try {
            if (passenger == null) return "Error: No passenger selected";
            if (bus == null) return "Error: No bus selected";
            if (!bus.getPassengers().contains(passenger))
                return "Error: " + passenger.getName() + " is not on bus " + bus.getPlateNumber();
            bus.removePassenger(passenger);
            updateVehicleList();
            updatePassengerList();
            log("Passenger alighted: " + passenger.getName() + " from bus " + bus.getPlateNumber());
            return "Success: " + passenger.getName() + " alighted from bus " + bus.getPlateNumber();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    // ── FILE I/O ─────────────────────────────────────────────────────────────

    public void saveAllData() {
        manager.saveAllData();
        log("All data saved to files");
        updateLogList();
    }

    public void loadAllData() {
        manager.loadAllData();
        updateVehicleList();
        updatePassengerList();
        updateRouteList();
        log("All data loaded from files");
        updateLogList();
    }

    // ── LOGGING ──────────────────────────────────────────────────────────────

    private void log(String message) {
        try {
            DataPersistence.appendLog(message);
        } catch (IOException e) {
            System.err.println("Log error: " + e.getMessage());
        }
        updateLogList();
    }

    private void updateLogList() {
        Platform.runLater(() -> {
            try {
                List<String> logs = DataPersistence.readLogs();
                logList.setAll(logs);
            } catch (IOException e) {
                System.err.println("Error reading logs: " + e.getMessage());
            }
        });
    }

    public ObservableList<String> getLogList() { return logList; }

    // ── STATS ─────────────────────────────────────────────────────────────────

    public String getSystemStats() {
        int total = manager.getTotalVehicleCount();
        int buses = 0, taxis = 0, trains = 0, available = 0;
        for (Vehicle v : manager.getAllVehicles()) {
            if (v instanceof Bus) buses++;
            else if (v instanceof Taxi) { taxis++; if (((Taxi) v).isAvailable()) available++; }
            else if (v instanceof Train) trains++;
        }
        int passengers = manager.getAllPassengers().size();
        int onTrip = 0;
        for (Passenger p : manager.getAllPassengers()) { if (p.isOnTrip()) onTrip++; }
        int routes = manager.getAllRouteIds().size();

        return "Vehicles: " + total + "  (Buses: " + buses + ", Taxis: " + taxis
                + ", Trains: " + trains + ")  |  Available Taxis: " + available
                + "  |  Passengers: " + passengers + "  (On Trip: " + onTrip + ")"
                + "  |  Routes: " + routes;
    }

    public TransportManager getManager() { return manager; }
}
