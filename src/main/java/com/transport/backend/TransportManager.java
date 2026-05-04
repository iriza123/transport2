package com.transport.backend;

import com.transport.backend.exceptions.*;
import com.transport.backend.io.DataPersistence;
import java.util.*;
import java.io.IOException;

public class TransportManager {
    
    private Set<Vehicle> registeredVehicles;
    private Map<String, List<Taxi>> taxisByZone;
    private Map<String, Route> routes;
    private List<Passenger> passengers;

    public TransportManager() {
        this.registeredVehicles = new HashSet<>();
        this.taxisByZone = new HashMap<>();
        this.routes = new HashMap<>();
        this.passengers = new ArrayList<>();
    }

    public boolean registerVehicle(Vehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }
        boolean added = registeredVehicles.add(vehicle);
        if (added && vehicle instanceof Taxi) {
            addTaxiToZone((Taxi) vehicle, "Central");
        }
        return added;
    }

    public boolean unregisterVehicle(Vehicle vehicle) {
        boolean removed = registeredVehicles.remove(vehicle);
        if (removed && vehicle instanceof Taxi) {
            removeTaxiFromAllZones((Taxi) vehicle);
        }
        return removed;
    }

    public Set<Vehicle> getAllVehicles() {
        return new HashSet<>(registeredVehicles);
    }

    public int getTotalVehicleCount() {
        return registeredVehicles.size();
    }

    public void addTaxiToZone(Taxi taxi, String zone) {
        if (taxi == null || zone == null || zone.trim().isEmpty()) {
            throw new IllegalArgumentException("Taxi and zone cannot be null");
        }
        taxisByZone.putIfAbsent(zone, new ArrayList<>());
        List<Taxi> taxisInZone = taxisByZone.get(zone);
        if (!taxisInZone.contains(taxi)) {
            taxisInZone.add(taxi);
        }
    }

    public List<Taxi> getTaxisInZone(String zone) {
        List<Taxi> taxisInZone = taxisByZone.get(zone);
        return taxisInZone != null ? new ArrayList<>(taxisInZone) : new ArrayList<>();
    }

    public List<Taxi> getAvailableTaxisInZone(String zone) {
        List<Taxi> available = new ArrayList<>();
        List<Taxi> taxisInZone = getTaxisInZone(zone);
        for (Taxi taxi : taxisInZone) {
            if (taxi.isAvailable()) {
                available.add(taxi);
            }
        }
        return available;
    }

    private void removeTaxiFromAllZones(Taxi taxi) {
        for (List<Taxi> taxiList : taxisByZone.values()) {
            taxiList.remove(taxi);
        }
    }

    public Set<String> getAllZones() {
        return new HashSet<>(taxisByZone.keySet());
    }

    public void addRoute(String routeId, Route route) {
        if (routeId == null || route == null) {
            throw new IllegalArgumentException("Route ID and route cannot be null");
        }
        routes.put(routeId, route);
    }

    public Route getRoute(String routeId) {
        return routes.get(routeId);
    }

    public boolean hasRoute(String routeId) {
        return routes.containsKey(routeId);
    }

    public Route removeRoute(String routeId) {
        return routes.remove(routeId);
    }

    public Set<String> getAllRouteIds() {
        return new HashSet<>(routes.keySet());
    }

    public Map<String, Route> getAllRoutes() {
        return new HashMap<>(routes);
    }

    public void addPassenger(Passenger passenger) {
        if (passenger == null) {
            throw new IllegalArgumentException("Passenger cannot be null");
        }
        passengers.add(passenger);
    }

    public boolean removePassenger(Passenger passenger) {
        return passengers.remove(passenger);
    }

    public List<Passenger> getAllPassengers() {
        return new ArrayList<>(passengers);
    }

    public Passenger findPassengerById(String id) {
        for (Passenger p : passengers) {
            if (p.getId().equals(id)) {
                return p;
            }
        }
        return null;
    }

    public void saveAllData() {
        try {
            List<String> vehicleData = new ArrayList<>();
            for (Vehicle vehicle : registeredVehicles) {
                String vehicleInfo = vehicle.getPlateNumber() + "|" + 
                                   vehicle.getClass().getSimpleName() + "|" + 
                                   vehicle.getFuelLevel();
                vehicleData.add(vehicleInfo);
            }
            DataPersistence.saveVehicles(vehicleData);
            
            Map<String, String> routeData = new HashMap<>();
            for (Map.Entry<String, Route> entry : routes.entrySet()) {
                routeData.put(entry.getKey(), entry.getValue().toFileString());
            }
            DataPersistence.saveRoutes(routeData);
            
            List<String> passengerData = new ArrayList<>();
            for (Passenger p : passengers) {
                passengerData.add(p.toFileString());
            }
            DataPersistence.savePassengers(passengerData);
            
            DataPersistence.appendLog("System data saved successfully");
            
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    public void loadAllData() {
        try {
            Map<String, String> routeData = DataPersistence.loadRoutes();
            for (Map.Entry<String, String> entry : routeData.entrySet()) {
                try {
                    Route route = Route.fromFileString(entry.getValue());
                    routes.put(entry.getKey(), route);
                } catch (Exception e) {
                    System.err.println("Error loading route: " + entry.getKey());
                }
            }
            
            List<String> passengerData = DataPersistence.loadPassengers();
            for (String passengerStr : passengerData) {
                try {
                    Passenger p = Passenger.fromFileString(passengerStr);
                    passengers.add(p);
                } catch (Exception e) {
                    System.err.println("Error loading passenger");
                }
            }
            
            DataPersistence.appendLog("System data loaded successfully");
            
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }
}
