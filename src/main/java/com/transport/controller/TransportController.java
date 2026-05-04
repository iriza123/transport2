package com.transport.controller;

import com.transport.backend.*;
import com.transport.backend.exceptions.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.List;

public class TransportController {
    
    private TransportManager manager;
    private ObservableList<Vehicle> vehicleList;
    private ObservableList<Passenger> passengerList;
    private ObservableList<String> routeList;

    public TransportController() {
        this.manager = new TransportManager();
        this.vehicleList = FXCollections.observableArrayList();
        this.passengerList = FXCollections.observableArrayList();
        this.routeList = FXCollections.observableArrayList();
    }

    public String addVehicle(String type, String plateNumber, int fuelLevel, String extra1, String extra2) {
        try {
            Vehicle vehicle = null;
            
            switch (type) {
                case "Bus":
                    int capacity = Integer.parseInt(extra1);
                    Route route = manager.getRoute(extra2);
                    if (route == null) {
                        return "Error: Route not found";
                    }
                    vehicle = new Bus(plateNumber, fuelLevel, capacity, route);
                    break;
                    
                case "Taxi":
                    String driverName = extra1;
                    vehicle = new Taxi(plateNumber, fuelLevel, driverName, true);
                    break;
                    
                case "Train":
                    String schedule = extra1;
                    int cars = Integer.parseInt(extra2);
                    vehicle = new Train(plateNumber, fuelLevel, schedule, cars);
                    break;
            }
            
            if (vehicle != null && manager.registerVehicle(vehicle)) {
                updateVehicleList();
                return "Success: Vehicle added";
            }
            return "Error: Vehicle already exists";
            
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String removeVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            return "Error: No vehicle selected";
        }
        if (manager.unregisterVehicle(vehicle)) {
            updateVehicleList();
            return "Success: Vehicle removed";
        }
        return "Error: Vehicle not found";
    }

    public ObservableList<Vehicle> getVehicleList() {
        return vehicleList;
    }

    private void updateVehicleList() {
        Platform.runLater(() -> {
            vehicleList.clear();
            vehicleList.addAll(manager.getAllVehicles());
        });
    }

    public String addPassenger(String name, String id) {
        try {
            Passenger passenger = new Passenger(name, id);
            manager.addPassenger(passenger);
            updatePassengerList();
            return "Success: Passenger added";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String removePassenger(Passenger passenger) {
        if (passenger == null) {
            return "Error: No passenger selected";
        }
        if (manager.removePassenger(passenger)) {
            updatePassengerList();
            return "Success: Passenger removed";
        }
        return "Error: Passenger not found";
    }

    public ObservableList<Passenger> getPassengerList() {
        return passengerList;
    }

    private void updatePassengerList() {
        Platform.runLater(() -> {
            passengerList.clear();
            passengerList.addAll(manager.getAllPassengers());
        });
    }

    public String addRoute(String routeId, String origin, String destination, double distance) {
        try {
            Route route = new Route(origin, destination, distance);
            manager.addRoute(routeId, route);
            updateRouteList();
            return "Success: Route added";
        } catch (InvalidRouteException e) {
            return "Error: " + e.getMessage();
        }
    }

    public String removeRoute(String routeId) {
        if (routeId == null || routeId.trim().isEmpty()) {
            return "Error: No route selected";
        }
        if (manager.removeRoute(routeId) != null) {
            updateRouteList();
            return "Success: Route removed";
        }
        return "Error: Route not found";
    }

    public ObservableList<String> getRouteList() {
        return routeList;
    }

    private void updateRouteList() {
        Platform.runLater(() -> {
            routeList.clear();
            for (String routeId : manager.getAllRouteIds()) {
                Route route = manager.getRoute(routeId);
                routeList.add(routeId + ": " + route.toString());
            }
        });
    }

    public String addTaxiToZone(Taxi taxi, String zone) {
        try {
            manager.addTaxiToZone(taxi, zone);
            return "Success: Taxi added to zone";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public ObservableList<String> getTaxisInZone(String zone) {
        ObservableList<String> taxiList = FXCollections.observableArrayList();
        for (Taxi taxi : manager.getTaxisInZone(zone)) {
            taxiList.add(taxi.getPlateNumber() + " - " + taxi.getDriverName() + 
                        " (Available: " + taxi.isAvailable() + ")");
        }
        return taxiList;
    }

    public void saveAllData() {
        manager.saveAllData();
    }

    public void loadAllData() {
        manager.loadAllData();
        updateVehicleList();
        updatePassengerList();
        updateRouteList();
    }

    public TransportManager getManager() {
        return manager;
    }

    public String bookTaxi(Passenger passenger, String zone, Route route) {
        try {
            if (passenger == null) {
                return "Error: No passenger selected";
            }
            if (route == null) {
                return "Error: No route selected";
            }
            if (passenger.isOnTrip()) {
                return "Error: Passenger is already on a trip";
            }

            List<Taxi> availableTaxis = manager.getAvailableTaxisInZone(zone);
            if (availableTaxis.isEmpty()) {
                return "Error: No available taxis in zone " + zone;
            }

            Taxi taxi = availableTaxis.get(0);
            taxi.acceptBooking(passenger, route);
            updateVehicleList();
            updatePassengerList();
            return "Success: Taxi " + taxi.getPlateNumber() + " booked for " + passenger.getName();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String completeTaxiTrip(Taxi taxi) {
        try {
            if (taxi == null) {
                return "Error: No taxi selected";
            }
            if (taxi.isAvailable()) {
                return "Error: Taxi is not on a trip";
            }

            taxi.completeTrip();
            updateVehicleList();
            updatePassengerList();
            return "Success: Trip completed. Taxi is now available";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String boardBus(Passenger passenger, Bus bus) {
        try {
            if (passenger == null) {
                return "Error: No passenger selected";
            }
            if (bus == null) {
                return "Error: No bus selected";
            }
            if (passenger.isOnTrip()) {
                return "Error: Passenger is already on a trip";
            }
            if (!bus.isInService()) {
                return "Error: Bus is not in service";
            }

            bus.addPassenger(passenger);
            updateVehicleList();
            updatePassengerList();
            return "Success: Passenger boarded bus " + bus.getPlateNumber();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public String alightBus(Passenger passenger, Bus bus) {
        try {
            if (passenger == null) {
                return "Error: No passenger selected";
            }
            if (bus == null) {
                return "Error: No bus selected";
            }

            bus.removePassenger(passenger);
            updateVehicleList();
            updatePassengerList();
            return "Success: Passenger alighted from bus";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}
