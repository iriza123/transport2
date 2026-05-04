package com.transport.backend.io;


import java.io.*;

import java.util.*;

/**
 * DataPersistence - Handles reading and writing data to files
 * Implements File I/O operations for the transport system
 */
public class DataPersistence {
    
    private static final String DATA_DIRECTORY = "data/";
    private static final String VEHICLES_FILE = DATA_DIRECTORY + "vehicles.txt";
    private static final String ROUTES_FILE = DATA_DIRECTORY + "routes.txt";
    private static final String PASSENGERS_FILE = DATA_DIRECTORY + "passengers.txt";
    
    /**
     * Initialize data directory
     */
    public static void initializeDataDirectory() {
        File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
            System.out.println("Data directory created: " + DATA_DIRECTORY);
        }
    }
    
    // ========== VEHICLE DATA PERSISTENCE ==========
    
    /**
     * FILE I/O OPERATION: Writing vehicle data to file
     * Uses BufferedWriter for efficient writing
     */
    public static void saveVehicles(List<String> vehicleData) throws IOException {
        initializeDataDirectory();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VEHICLES_FILE))) {
            writer.write("=== TRANSPORT SYSTEM - VEHICLE DATA ===\n");
            writer.write("Generated: " + new Date() + "\n");
            writer.write("Total Vehicles: " + vehicleData.size() + "\n");
            writer.write("==========================================\n\n");
            
            for (String vehicle : vehicleData) {
                writer.write(vehicle);
                writer.newLine();
            }
            
            System.out.println("✓ Vehicle data saved to: " + VEHICLES_FILE);
        }
    }
    
    /**
     * FILE I/O OPERATION: Reading vehicle data from file
     * Uses BufferedReader for efficient reading
     */
    public static List<String> loadVehicles() throws IOException {
        List<String> vehicles = new ArrayList<>();
        File file = new File(VEHICLES_FILE);
        
        if (!file.exists()) {
            System.out.println("No vehicle data file found.");
            return vehicles;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean dataSection = false;
            
            while ((line = reader.readLine()) != null) {
                // Skip header lines
                if (line.startsWith("===") || line.startsWith("Generated") || 
                    line.startsWith("Total") || line.trim().isEmpty()) {
                    if (line.startsWith("===") && dataSection) {
                        break; // End of data section
                    }
                    if (line.startsWith("Total")) {
                        dataSection = true;
                    }
                    continue;
                }
                
                if (dataSection) {
                    vehicles.add(line);
                }
            }
            
            System.out.println("✓ Loaded " + vehicles.size() + " vehicles from: " + VEHICLES_FILE);
        }
        
        return vehicles;
    }
    
    // ========== ROUTE DATA PERSISTENCE ==========
    
    /**
     * FILE I/O OPERATION: Writing route data to file
     */
    public static void saveRoutes(Map<String, String> routeData) throws IOException {
        initializeDataDirectory();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ROUTES_FILE))) {
            writer.write("=== TRANSPORT SYSTEM - ROUTE DATA ===\n");
            writer.write("Generated: " + new Date() + "\n");
            writer.write("Total Routes: " + routeData.size() + "\n");
            writer.write("======================================\n\n");
            
            for (Map.Entry<String, String> entry : routeData.entrySet()) {
                writer.write(entry.getKey() + "|" + entry.getValue());
                writer.newLine();
            }
            
            System.out.println("✓ Route data saved to: " + ROUTES_FILE);
        }
    }
    
    /**
     * FILE I/O OPERATION: Reading route data from file
     */
    public static Map<String, String> loadRoutes() throws IOException {
        Map<String, String> routes = new HashMap<>();
        File file = new File(ROUTES_FILE);
        
        if (!file.exists()) {
            System.out.println("No route data file found.");
            return routes;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean dataSection = false;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("===") || line.startsWith("Generated") || 
                    line.startsWith("Total") || line.trim().isEmpty()) {
                    if (line.startsWith("Total")) {
                        dataSection = true;
                    }
                    continue;
                }
                
                if (dataSection && line.contains("|")) {
                    String[] parts = line.split("\\|", 2);
                    if (parts.length == 2) {
                        routes.put(parts[0], parts[1]);
                    }
                }
            }
            
            System.out.println("✓ Loaded " + routes.size() + " routes from: " + ROUTES_FILE);
        }
        
        return routes;
    }
    
    // ========== PASSENGER DATA PERSISTENCE ==========
    
    /**
     * FILE I/O OPERATION: Writing passenger data to file
     */
    public static void savePassengers(List<String> passengerData) throws IOException {
        initializeDataDirectory();
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PASSENGERS_FILE))) {
            writer.write("=== TRANSPORT SYSTEM - PASSENGER DATA ===\n");
            writer.write("Generated: " + new Date() + "\n");
            writer.write("Total Passengers: " + passengerData.size() + "\n");
            writer.write("=========================================\n\n");
            
            for (String passenger : passengerData) {
                writer.write(passenger);
                writer.newLine();
            }
            
            System.out.println("✓ Passenger data saved to: " + PASSENGERS_FILE);
        }
    }
    
    /**
     * FILE I/O OPERATION: Reading passenger data from file
     */
    public static List<String> loadPassengers() throws IOException {
        List<String> passengers = new ArrayList<>();
        File file = new File(PASSENGERS_FILE);
        
        if (!file.exists()) {
            System.out.println("No passenger data file found.");
            return passengers;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean dataSection = false;
            
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("===") || line.startsWith("Generated") || 
                    line.startsWith("Total") || line.trim().isEmpty()) {
                    if (line.startsWith("Total")) {
                        dataSection = true;
                    }
                    continue;
                }
                
                if (dataSection) {
                    passengers.add(line);
                }
            }
            
            System.out.println("✓ Loaded " + passengers.size() + " passengers from: " + PASSENGERS_FILE);
        }
        
        return passengers;
    }
    
    // ========== UTILITY METHODS ==========
    
    /**
     * FILE I/O OPERATION: Append log entry to a log file
     */
    public static void appendLog(String logMessage) throws IOException {
        initializeDataDirectory();
        String logFile = DATA_DIRECTORY + "system.log";
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write("[" + new Date() + "] " + logMessage);
            writer.newLine();
        }
    }
    
    /**
     * FILE I/O OPERATION: Read all logs from log file
     */
    public static List<String> readLogs() throws IOException {
        List<String> logs = new ArrayList<>();
        String logFile = DATA_DIRECTORY + "system.log";
        File file = new File(logFile);
        
        if (!file.exists()) {
            return logs;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
        }
        
        return logs;
    }
    
    /**
     * Clear all data files
     */
    public static void clearAllData() {
        File vehiclesFile = new File(VEHICLES_FILE);
        File routesFile = new File(ROUTES_FILE);
        File passengersFile = new File(PASSENGERS_FILE);
        
        if (vehiclesFile.exists()) vehiclesFile.delete();
        if (routesFile.exists()) routesFile.delete();
        if (passengersFile.exists()) passengersFile.delete();
        
        System.out.println("✓ All data files cleared.");
    }
}

