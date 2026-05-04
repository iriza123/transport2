import exceptions.*;

public class TransportSystem {
    public static void main(String[] args) {
        try {
            System.out.println("╔════════════════════════════════════════════════════════════╗");
            System.out.println("║   TRANSPORT SYSTEM - JAVA COLLECTIONS FRAMEWORK DEMO      ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝\n");


            TransportManager manager = new TransportManager();

            // Create routes with validation
            Route route1 = new Route("Kigali", "Huye", 130);
            Route route2 = new Route("Kigali", "Musanze", 90);
            Route route3 = new Route("Huye", "Butare", 15);

            // Add routes to manager (Map demonstration)
            manager.addRoute("R001", route1);
            manager.addRoute("R002", route2);
            manager.addRoute("R003", route3);

            // Create vehicles
            Bus bus1 = new Bus("RAB123A", 50, 30, route1);
            Bus bus2 = new Bus("RAB456B", 60, 25, route2);
            Train train = new Train("RAD789C", 100, "10:00 AM", 8);
            Taxi taxi1 = new Taxi("RAC456B", 40, "Eric", true);
            Taxi taxi2 = new Taxi("RAC789D", 35, "Marie", true);
            Taxi taxi3 = new Taxi("RAC111E", 45, "David", false);

            // Register vehicles (Set demonstration - no duplicates)
            System.out.println("\n===== VEHICLE REGISTRATION (Set Collection) =====");
            manager.registerVehicle(bus1);
            manager.registerVehicle(bus2);
            manager.registerVehicle(train);
            manager.registerVehicle(taxi1);
            manager.registerVehicle(taxi2);
            manager.registerVehicle(taxi3);
            
            // Try to register duplicate (Set will prevent it)
            System.out.println("\nAttempting to register duplicate vehicle:");
            manager.registerVehicle(taxi1); // Should fail

            // Create passengers
            Passenger p1 = new Passenger("John", "P001");
            Passenger p2 = new Passenger("Alice", "P002");
            Passenger p3 = new Passenger("Bob", "P003");
            Passenger p4 = new Passenger("Sarah", "P004");
            Passenger p5 = new Passenger("Mike", "P005");

            // ===== DEMONSTRATION 1: List Collection (Bus Passengers) =====
            System.out.println("\n\n===== BUS PASSENGER MANAGEMENT (List Collection) =====");
            System.out.println("Relationship: One Bus → Many Passengers (Ordered)");
            
            bus1.addPassenger(p1);
            bus1.addPassenger(p2);
            bus1.addPassenger(p3);
            bus1.displayPassengers();
            
            System.out.println("\nRemoving a passenger:");
            bus1.removePassenger(p2);
            bus1.displayPassengers();
            
            System.out.println("\nAdding more passengers:");
            bus1.addPassenger(p4);
            bus1.addPassenger(p5);
            System.out.println("Available seats: " + bus1.getAvailableSeats());

            // ===== DEMONSTRATION 2: Map Collection (Taxis by Zone) =====
            System.out.println("\n\n===== TAXI ZONE MANAGEMENT (Map Collection) =====");
            System.out.println("Relationship: Zone → List of Taxis (Key-Value with One-to-Many)");
            
            manager.addTaxiToZone(taxi1, "Downtown");
            manager.addTaxiToZone(taxi2, "Downtown");
            manager.addTaxiToZone(taxi3, "Airport");
            
            manager.displayTaxisByZone();
            
            System.out.println("\nFinding available taxis in Downtown:");
            var availableTaxis = manager.getAvailableTaxisInZone("Downtown");
            System.out.println("Available taxis: " + availableTaxis.size());
            for (Taxi taxi : availableTaxis) {
                System.out.println("  - " + taxi.getPlateNumber() + " (Driver: " + taxi.getDriverName() + ")");
            }

            // ===== DEMONSTRATION 3: List Collection (Train Stops) =====
            System.out.println("\n\n===== TRAIN STOP MANAGEMENT (List Collection) =====");
            System.out.println("Relationship: One Train → Many Stops (Ordered Sequence)");
            
            train.addStop("Kigali Central");
            train.addStop("Rwamagana");
            train.addStop("Kayonza");
            train.addStop("Rusumo");
            train.displayStops();
            
            System.out.println("\nNext stop after position 1: " + train.getNextStop(1));

            // ===== DEMONSTRATION 4: Set Collection (Unique Vehicles) =====
            System.out.println("\n\n===== VEHICLE REGISTRY (Set Collection) =====");
            System.out.println("Relationship: Unique Collection (No Duplicates)");
            manager.displayAllVehicles();
            System.out.println("Total registered vehicles: " + manager.getTotalVehicleCount());

            // ===== DEMONSTRATION 5: Map Collection (Routes) =====
            System.out.println("\n\n===== ROUTE LOOKUP (Map Collection) =====");
            System.out.println("Relationship: Route ID → Route Object (Key-Value)");
            manager.displayAllRoutes();
            
            System.out.println("\nLooking up specific route:");
            Route foundRoute = manager.getRoute("R002");
            if (foundRoute != null) {
                System.out.print("Found: ");
                foundRoute.displayRoute();
            }

            // ===== OPERATIONS DEMONSTRATION =====
            System.out.println("\n\n===== COLLECTION OPERATIONS DEMO =====");
            
            System.out.println("\n1. Bus Movement with Passengers:");
            bus1.move();
            
            System.out.println("\n2. Taxi Booking:");
            taxi1.acceptBooking(p1, route2);
            
            System.out.println("\n3. Train Movement with Stops:");
            train.move();
            
            System.out.println("\n4. Unregistering a vehicle:");
            manager.unregisterVehicle(bus2);
            System.out.println("Remaining vehicles: " + manager.getTotalVehicleCount());

            // ===== SUMMARY =====
            System.out.println("\n\n╔════════════════════════════════════════════════════════════╗");
            System.out.println("║                    COLLECTIONS SUMMARY                     ║");
            System.out.println("╠════════════════════════════════════════════════════════════╣");
            System.out.println("║ 1. List<Passenger> in Bus                                 ║");
            System.out.println("║    → One-to-Many, Ordered, Allows operations               ║");
            System.out.println("║                                                            ║");
            System.out.println("║ 2. Set<Vehicle> in TransportManager                       ║");
            System.out.println("║    → Unique vehicles, No duplicates                        ║");
            System.out.println("║                                                            ║");
            System.out.println("║ 3. Map<String, List<Taxi>> in TransportManager            ║");
            System.out.println("║    → Zone-based taxi lookup, Key-Value with One-to-Many    ║");
            System.out.println("║                                                            ║");
            System.out.println("║ 4. Map<String, Route> in TransportManager                 ║");
            System.out.println("║    → Fast route lookup by ID                               ║");
            System.out.println("║                                                            ║");
            System.out.println("║ 5. List<String> in Train                                  ║");
            System.out.println("║    → Ordered stops sequence                                ║");
            System.out.println("╚════════════════════════════════════════════════════════════╝");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}