package com.transport.backend;

import com.transport.backend.exceptions.InvalidRouteException;

public class Route {
    private String origin;
    private String destination;
    private double distance;

    public Route(String origin, String destination, double distance) throws InvalidRouteException {
        if (origin == null || origin.trim().isEmpty()) {
            throw new InvalidRouteException("Origin cannot be null or empty");
        }
        if (destination == null || destination.trim().isEmpty()) {
            throw new InvalidRouteException("Destination cannot be null or empty");
        }
        if (distance <= 0) {
            throw new InvalidRouteException("Distance must be positive");
        }
        if (origin.equalsIgnoreCase(destination)) {
            throw new InvalidRouteException("Origin and destination cannot be the same");
        }
        this.origin = origin;
        this.destination = destination;
        this.distance = distance;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public double getDistance() {
        return distance;
    }

    public String toFileString() {
        return origin + "|" + destination + "|" + distance;
    }

    public static Route fromFileString(String fileString) throws InvalidRouteException {
        String[] parts = fileString.split("\\|");
        if (parts.length == 3) {
            return new Route(parts[0], parts[1], Double.parseDouble(parts[2]));
        }
        throw new IllegalArgumentException("Invalid route file format");
    }

    @Override
    public String toString() {
        return origin + " → " + destination + " (" + distance + " km)";
    }
}
