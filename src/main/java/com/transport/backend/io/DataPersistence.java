package com.transport.backend.io;

import java.io.*;
import java.util.*;

public class DataPersistence {

    private static final String DATA_DIRECTORY = "data/";
    private static final String VEHICLES_FILE = DATA_DIRECTORY + "vehicles.txt";
    private static final String ROUTES_FILE = DATA_DIRECTORY + "routes.txt";
    private static final String PASSENGERS_FILE = DATA_DIRECTORY + "passengers.txt";
    private static final String LOG_FILE = DATA_DIRECTORY + "system.log";

    public static void initializeDataDirectory() {
        File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void saveVehicles(List<String> vehicleData) throws IOException {
        initializeDataDirectory();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(VEHICLES_FILE))) {
            writer.write("=== VEHICLE DATA ===");
            writer.newLine();
            writer.write("Generated: " + new Date());
            writer.newLine();
            writer.write("Total: " + vehicleData.size());
            writer.newLine();
            writer.newLine();
            for (String vehicle : vehicleData) {
                writer.write(vehicle);
                writer.newLine();
            }
        }
    }

    public static List<String> loadVehicles() throws IOException {
        List<String> vehicles = new ArrayList<>();
        File file = new File(VEHICLES_FILE);
        if (!file.exists()) {
            return vehicles;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean dataSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Total:")) {
                    dataSection = true;
                    continue;
                }
                if (line.startsWith("===") || line.startsWith("Generated") || line.trim().isEmpty()) {
                    continue;
                }
                if (dataSection) {
                    vehicles.add(line);
                }
            }
        }
        return vehicles;
    }

    public static void saveRoutes(Map<String, String> routeData) throws IOException {
        initializeDataDirectory();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ROUTES_FILE))) {
            writer.write("=== ROUTE DATA ===");
            writer.newLine();
            writer.write("Generated: " + new Date());
            writer.newLine();
            writer.write("Total: " + routeData.size());
            writer.newLine();
            writer.newLine();
            for (Map.Entry<String, String> entry : routeData.entrySet()) {
                writer.write(entry.getKey() + "|" + entry.getValue());
                writer.newLine();
            }
        }
    }

    public static Map<String, String> loadRoutes() throws IOException {
        Map<String, String> routes = new HashMap<>();
        File file = new File(ROUTES_FILE);
        if (!file.exists()) {
            return routes;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean dataSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Total:")) {
                    dataSection = true;
                    continue;
                }
                if (line.startsWith("===") || line.startsWith("Generated") || line.trim().isEmpty()) {
                    continue;
                }
                if (dataSection && line.contains("|")) {
                    String[] parts = line.split("\\|", 2);
                    if (parts.length == 2) {
                        routes.put(parts[0], parts[1]);
                    }
                }
            }
        }
        return routes;
    }

    public static void savePassengers(List<String> passengerData) throws IOException {
        initializeDataDirectory();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PASSENGERS_FILE))) {
            writer.write("=== PASSENGER DATA ===");
            writer.newLine();
            writer.write("Generated: " + new Date());
            writer.newLine();
            writer.write("Total: " + passengerData.size());
            writer.newLine();
            writer.newLine();
            for (String passenger : passengerData) {
                writer.write(passenger);
                writer.newLine();
            }
        }
    }

    public static List<String> loadPassengers() throws IOException {
        List<String> passengers = new ArrayList<>();
        File file = new File(PASSENGERS_FILE);
        if (!file.exists()) {
            return passengers;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean dataSection = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Total:")) {
                    dataSection = true;
                    continue;
                }
                if (line.startsWith("===") || line.startsWith("Generated") || line.trim().isEmpty()) {
                    continue;
                }
                if (dataSection) {
                    passengers.add(line);
                }
            }
        }
        return passengers;
    }

    public static void appendLog(String logMessage) throws IOException {
        initializeDataDirectory();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write("[" + new Date() + "] " + logMessage);
            writer.newLine();
        }
    }

    public static List<String> readLogs() throws IOException {
        List<String> logs = new ArrayList<>();
        File file = new File(LOG_FILE);
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
}
