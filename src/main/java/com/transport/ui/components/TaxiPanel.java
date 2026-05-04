package com.transport.ui.components;

import com.transport.backend.*;
import com.transport.controller.TransportController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TaxiPanel {

    private VBox root;
    private TransportController controller;
    private ListView<String> taxiListView;
    private Label statusLabel;

    public TaxiPanel(TransportController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        VBox content = new VBox(15);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE);");

        Label title = new Label("Taxi Zone Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");

        // --- SEARCH ZONE SECTION ---
        Label searchTitle = new Label("Search Taxis by Zone");
        searchTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E; -fx-font-size: 14px;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);

        TextField zoneField = new TextField();
        zoneField.setPromptText("e.g. Central");
        HBox.setHgrow(zoneField, Priority.ALWAYS);

        Button searchBtn = new Button("Search Zone");
        searchBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;");
        searchBox.getChildren().addAll(zoneField, searchBtn);

        Label taxiListLabel = new Label("Taxis in Zone:");
        taxiListLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E;");

        taxiListView = new ListView<>();
        taxiListView.setPrefHeight(200);
        taxiListView.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(taxiListView, Priority.ALWAYS);
        taxiListView.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE); " +
                "-fx-border-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-border-width: 2; -fx-border-radius: 10;");

        searchBtn.setOnAction(e -> {
            String zone = zoneField.getText().trim();
            if (zone.isEmpty()) {
                showStatus("Error: Enter a zone name to search", false);
                return;
            }
            taxiListView.setItems(controller.getTaxisInZone(zone));
            if (taxiListView.getItems().isEmpty()) {
                showStatus("No taxis found in zone: " + zone, false);
            } else {
                showStatus("Found " + taxiListView.getItems().size() + " taxi(s) in zone: " + zone, true);
            }
        });

        // --- BOOK TAXI SECTION ---
        Separator sep1 = new Separator();

        Label bookTitle = new Label("Book a Taxi for a Passenger");
        bookTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E; -fx-font-size: 14px;");

        GridPane bookGrid = new GridPane();
        bookGrid.setHgap(10);
        bookGrid.setVgap(10);
        bookGrid.setMaxWidth(Double.MAX_VALUE);

        Label passengerIdLabel = new Label("Passenger ID:");
        passengerIdLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        TextField passengerIdField = new TextField();
        passengerIdField.setPromptText("e.g. P001");
        passengerIdField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(passengerIdField, Priority.ALWAYS);

        Label zoneBookLabel = new Label("Zone:");
        zoneBookLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        TextField zoneBookField = new TextField();
        zoneBookField.setPromptText("e.g. Central");
        zoneBookField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(zoneBookField, Priority.ALWAYS);

        Label routeIdLabel = new Label("Route ID:");
        routeIdLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        TextField routeIdField = new TextField();
        routeIdField.setPromptText("e.g. R001");
        routeIdField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(routeIdField, Priority.ALWAYS);

        ColumnConstraints col1 = new ColumnConstraints(120);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.ALWAYS);
        bookGrid.getColumnConstraints().addAll(col1, col2);

        bookGrid.add(passengerIdLabel, 0, 0);
        bookGrid.add(passengerIdField, 1, 0);
        bookGrid.add(zoneBookLabel, 0, 1);
        bookGrid.add(zoneBookField, 1, 1);
        bookGrid.add(routeIdLabel, 0, 2);
        bookGrid.add(routeIdField, 1, 2);

        Button bookBtn = new Button("Book Taxi");
        bookBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 30;");

        bookBtn.setOnAction(e -> {
            String passengerId = passengerIdField.getText().trim();
            String zone = zoneBookField.getText().trim();
            String routeId = routeIdField.getText().trim();

            if (passengerId.isEmpty() || zone.isEmpty() || routeId.isEmpty()) {
                showStatus("Error: Passenger ID, Zone and Route ID are all required", false);
                return;
            }

            Passenger passenger = controller.getManager().findPassengerById(passengerId);
            if (passenger == null) {
                showStatus("Error: Passenger '" + passengerId + "' not found. Add passenger first.", false);
                return;
            }

            if (passenger.isOnTrip()) {
                showStatus("Error: " + passenger.getName() + " is already ON A TRIP. Cannot book again.", false);
                return;
            }

            Route route = controller.getManager().getRoute(routeId);
            if (route == null) {
                showStatus("Error: Route '" + routeId + "' not found. Add route first in Routes tab.", false);
                return;
            }

            String result = controller.bookTaxi(passenger, zone, route);
            showStatus(result, result.startsWith("Success"));

            if (result.startsWith("Success")) {
                passengerIdField.clear();
                zoneBookField.clear();
                routeIdField.clear();
            }
        });

        // --- COMPLETE TRIP SECTION ---
        Separator sep2 = new Separator();

        Label completeTitle = new Label("Complete a Taxi Trip");
        completeTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E; -fx-font-size: 14px;");

        GridPane completeGrid = new GridPane();
        completeGrid.setHgap(10);
        completeGrid.setVgap(10);
        completeGrid.setMaxWidth(Double.MAX_VALUE);

        ColumnConstraints col3 = new ColumnConstraints(120);
        ColumnConstraints col4 = new ColumnConstraints();
        col4.setHgrow(Priority.ALWAYS);
        completeGrid.getColumnConstraints().addAll(col3, col4);

        Label plateLabel = new Label("Taxi Plate:");
        plateLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        TextField plateField = new TextField();
        plateField.setPromptText("e.g. TAXI-101");
        plateField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(plateField, Priority.ALWAYS);

        completeGrid.add(plateLabel, 0, 0);
        completeGrid.add(plateField, 1, 0);

        Button completeBtn = new Button("Complete Trip");
        completeBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 30;");

        completeBtn.setOnAction(e -> {
            String plate = plateField.getText().trim();
            if (plate.isEmpty()) {
                showStatus("Error: Enter the taxi plate number", false);
                return;
            }

            Taxi taxi = findTaxiByPlate(plate);
            if (taxi == null) {
                showStatus("Error: Taxi '" + plate + "' not found", false);
                return;
            }

            if (taxi.isAvailable()) {
                showStatus("Error: Taxi " + plate + " is not currently on a trip", false);
                return;
            }

            String result = controller.completeTaxiTrip(taxi);
            showStatus(result, result.startsWith("Success"));

            if (result.startsWith("Success")) {
                plateField.clear();
            }
        });

        // Status label
        statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        content.getChildren().addAll(
                title,
                searchTitle,
                searchBox,
                taxiListLabel,
                taxiListView,
                sep1,
                bookTitle,
                bookGrid,
                bookBtn,
                sep2,
                completeTitle,
                completeGrid,
                completeBtn,
                statusLabel
        );

        // Wrap in ScrollPane so nothing gets cut off
        ScrollPane scrollPane = new ScrollPane(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(false);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: transparent;");

        root = new VBox();
        root.getChildren().add(scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);
    }

    private Taxi findTaxiByPlate(String plate) {
        for (Vehicle v : controller.getManager().getAllVehicles()) {
            if (v instanceof Taxi && v.getPlateNumber().equalsIgnoreCase(plate)) {
                return (Taxi) v;
            }
        }
        return null;
    }

    private void showStatus(String message, boolean success) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: "
                + (success ? "#1B5E20;" : "#B71C1C;"));
    }

    public VBox getView() {
        return root;
    }
}
