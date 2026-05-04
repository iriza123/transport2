# Transport Management System

A comprehensive JavaFX-based transport management system implementing OOP principles, collections framework, exception handling, and file I/O operations.

## Features

### Core Functionality
- Vehicle management (Bus, Taxi, Train)
- Passenger management
- Route management
- Taxi zone operations
- Real-time availability tracking
- Data persistence with file I/O

### Technical Implementation
- Object-Oriented Programming with inheritance and polymorphism
- Generic collections for type safety
- Custom exception handling
- JavaFX user interface with MVC architecture
- Thread-safe UI updates
- Real-life transportation logic

## Project Structure

```
src/main/java/com/transport/
├── backend/              - Business logic layer
│   ├── Vehicle.java      - Abstract base class
│   ├── Bus.java          - Bus implementation
│   ├── Taxi.java         - Taxi implementation
│   ├── Train.java        - Train implementation
│   ├── Passenger.java    - Passenger entity
│   ├── Route.java        - Route entity
│   ├── TransportManager.java - Main manager class
│   ├── exceptions/       - Custom exceptions
│   └── io/              - File I/O operations
├── controller/          - MVC controller layer
│   └── TransportController.java
└── ui/                  - JavaFX UI layer
    ├── TransportApp.java
    ├── MainView.java
    └── components/      - UI panels
```

## Technologies Used

- Java 11
- JavaFX 17.0.2
- Maven 3.x
- Collections Framework (List, Set, Map)
- BufferedReader/BufferedWriter for file I/O

## Requirements

- JDK 11 or higher
- Maven 3.x
- JavaFX 17.0.2 (managed by Maven)

## Installation

1. Clone the repository
```bash
git clone https://github.com/iriza123/transport2.git
cd transport2
```

2. Build the project
```bash
mvn clean install
```

## Running the Application

### Using Maven
```bash
mvn javafx:run
```

### Using IntelliJ IDEA
1. Open the project in IntelliJ IDEA
2. Right-click on `TransportApp.java`
3. Select "Run 'TransportApp.main()'"

## Usage

### Adding Vehicles
1. Navigate to the Vehicles tab
2. Select vehicle type (Bus, Taxi, or Train)
3. Enter vehicle details
4. Click "Add Vehicle"

### Managing Passengers
1. Navigate to the Passengers tab
2. Enter passenger name and ID
3. Click "Add Passenger"

### Creating Routes
1. Navigate to the Routes tab
2. Enter route ID, origin, destination, and distance
3. Click "Add Route"

### Searching Taxi Zones
1. Navigate to the Taxi Zones tab
2. Enter zone name
3. Click "Search Zone"

### Saving and Loading Data
- Click "Save Data" button to persist all data to files
- Click "Load Data" button to restore data from files

## Data Persistence

The system saves data to the following files:
- `data/vehicles.txt` - Vehicle information
- `data/routes.txt` - Route information
- `data/passengers.txt` - Passenger information
- `data/system.log` - System activity log

## Collections Implementation

### List (One-to-Many Relationship)
- Bus passengers: Ordered collection of passengers on a bus
- Train stops: Sequential list of train stations

### Set (Unique Collection)
- Registered vehicles: Ensures no duplicate vehicles

### Map (Key-Value Lookup)
- Routes: Fast lookup by route ID
- Taxi zones: Groups taxis by zone for efficient searching

## Exception Handling

Custom exceptions for robust error control:
- `CapacityExceededException` - When vehicle capacity is exceeded
- `InsufficientFuelException` - When vehicle has insufficient fuel
- `InvalidRouteException` - When route validation fails
- `VehicleNotAvailableException` - When vehicle is not available for booking

## Real-Life Logic

### Passenger Management
- Tracks if passenger is currently on a trip
- Prevents double-booking
- Links passenger to current vehicle

### Taxi Operations
- Availability checking (not busy and sufficient fuel)
- Zone-based operations
- Earnings and trip tracking
- Automatic status updates

### Bus Operations
- In-service/out-of-service status
- Capacity management with validation
- Current stop tracking
- Cannot take bus offline with passengers on board

### Train Operations
- Operational/delayed status management
- Capacity calculation based on number of cars
- Stop sequence tracking
- Delay management with reason tracking

## Architecture

The system follows MVC (Model-View-Controller) architecture:

- **Model**: Backend classes (Vehicle, Passenger, Route, etc.)
- **View**: JavaFX UI components (panels, layouts)
- **Controller**: TransportController bridges UI and backend

### Thread Safety
UI updates are performed on the JavaFX Application Thread using `Platform.runLater()` to ensure thread safety.

## Documentation

Additional documentation files:
- `REQUIREMENTS_VERIFICATION.md` - Detailed requirements verification
- `REAL_LIFE_FEATURES.md` - Real-life transportation logic documentation
- `ENHANCEMENTS_SUMMARY.md` - Summary of system enhancements
- `QUICK_REFERENCE.md` - Quick reference guide
- `FINAL_REQUIREMENTS_CHECKLIST.md` - Complete requirements checklist

## Testing

Sample test data is provided in the documentation. The system includes:
- Input validation
- Error handling with clear messages
- Real-time UI updates
- Data persistence verification

## License

This project is created for educational purposes.

## Author

Transport Management System - JavaFX Implementation
