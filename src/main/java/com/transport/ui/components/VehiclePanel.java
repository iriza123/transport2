package com.transport.ui.components;

import com.transport.backend.Vehicle;
import com.transport.controller.TransportController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class VehiclePanel {

    private VBox root;
    private TransportController controller;
    private ListView<Vehicle> vehicleListView;
    private Label statusLabel;

    // Dynamic labels that change based on vehicle type
    private Label extra1Label;
    private Label extra2Label;
    private TextField extra1Field;
    private TextField extra2Field;

    public VehiclePanel(TransportController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE);");

        Label titleLabel = new Label("Vehicle Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(12);

        // Type selector
        Label typeLabel = new Label("Vehicle Type:");
        typeLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Bus", "Taxi", "Train");
        typeCombo.setValue("Bus");
        typeCombo.setPrefWidth(200);

        // Plate number
        Label plateLabel = new Label("Plate Number:");
        plateLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        TextField plateField = new TextField();
        plateField.setPromptText("e.g. BUS-001");
        plateField.setPrefWidth(200);

        // Fuel level
        Label fuelLabel = new Label("Fuel Level (L):");
        fuelLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        TextField fuelField = new TextField();
        fuelField.setPromptText("e.g. 80");
        fuelField.setPrefWidth(200);

        // Dynamic field 1
        extra1Label = new Label("Capacity:");
        extra1Label.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        extra1Field = new TextField();
        extra1Field.setPromptText("e.g. 50");
        extra1Field.setPrefWidth(200);

        // Dynamic field 2
        extra2Label = new Label("Route ID:");
        extra2Label.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        extra2Field = new TextField();
        extra2Field.setPromptText("e.g. R001  (must add route first)");
        extra2Field.setPrefWidth(200);

        // Update labels when type changes
        typeCombo.setOnAction(e -> updateFieldLabels(typeCombo.getValue()));

        inputGrid.add(typeLabel, 0, 0);
        inputGrid.add(typeCombo, 1, 0);
        inputGrid.add(plateLabel, 0, 1);
        inputGrid.add(plateField, 1, 1);
        inputGrid.add(fuelLabel, 0, 2);
        inputGrid.add(fuelField, 1, 2);
        inputGrid.add(extra1Label, 0, 3);
        inputGrid.add(extra1Field, 1, 3);
        inputGrid.add(extra2Label, 0, 4);
        inputGrid.add(extra2Field, 1, 4);

        // Buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("Add Vehicle");
        addBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;");

        Button removeBtn = new Button("Remove Selected");
        removeBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;");

        buttonBox.getChildren().addAll(addBtn, removeBtn);

        // Status label
        statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        statusLabel.setWrapText(true);

        // Add button action
        addBtn.setOnAction(e -> {
            String type = typeCombo.getValue();
            String plate = plateField.getText().trim();
            String fuelText = fuelField.getText().trim();
            String extra1 = extra1Field.getText().trim();
            String extra2 = extra2Field.getText().trim();

            // Validate required fields
            if (plate.isEmpty()) {
                showStatus("Error: Plate Number is required", false);
                return;
            }
            if (fuelText.isEmpty()) {
                showStatus("Error: Fuel Level is required (e.g. 80)", false);
                return;
            }
            if (extra1.isEmpty()) {
                showStatus("Error: " + extra1Label.getText() + " is required", false);
                return;
            }
            if (type.equals("Bus") && extra2.isEmpty()) {
                showStatus("Error: Route ID is required for Bus. Add a route first in the Routes tab.", false);
                return;
            }
            if (type.equals("Train") && extra2.isEmpty()) {
                showStatus("Error: Number of Cars is required for Train", false);
                return;
            }

            int fuel;
            try {
                fuel = Integer.parseInt(fuelText);
                if (fuel <= 0) {
                    showStatus("Error: Fuel must be greater than 0", false);
                    return;
                }
            } catch (NumberFormatException ex) {
                showStatus("Error: Fuel must be a whole number (e.g. 80)", false);
                return;
            }

            String result = controller.addVehicle(type, plate, fuel, extra1, extra2);
            showStatus(result, result.startsWith("Success"));

            if (result.startsWith("Success")) {
                plateField.clear();
                fuelField.clear();
                extra1Field.clear();
                extra2Field.clear();
            }
        });

        // Remove button action
        removeBtn.setOnAction(e -> {
            Vehicle selected = vehicleListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showStatus("Error: Please select a vehicle from the list to remove", false);
                return;
            }
            String result = controller.removeVehicle(selected);
            showStatus(result, result.startsWith("Success"));
        });

        // Vehicle list
        Label listLabel = new Label("Registered Vehicles:");
        listLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E;");

        vehicleListView = new ListView<>(controller.getVehicleList());
        vehicleListView.setPrefHeight(180);
        vehicleListView.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE); " +
                "-fx-border-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        root.getChildren().addAll(
                titleLabel,
                inputGrid,
                buttonBox,
                statusLabel,
                listLabel,
                vehicleListView
        );
    }

    // Updates field labels and hints based on selected vehicle type
    private void updateFieldLabels(String type) {
        switch (type) {
            case "Bus":
                extra1Label.setText("Capacity:");
                extra1Field.setPromptText("e.g. 50  (number of seats)");
                extra2Label.setText("Route ID:");
                extra2Field.setPromptText("e.g. R001  (add route first in Routes tab)");
                extra2Field.setDisable(false);
                break;
            case "Taxi":
                extra1Label.setText("Driver Name:");
                extra1Field.setPromptText("e.g. John Smith");
                extra2Label.setText("(not needed for Taxi)");
                extra2Field.setPromptText("leave empty");
                extra2Field.clear();
                extra2Field.setDisable(true);
                break;
            case "Train":
                extra1Label.setText("Schedule:");
                extra1Field.setPromptText("e.g. 08:00-20:00");
                extra2Label.setText("Number of Cars:");
                extra2Field.setPromptText("e.g. 8");
                extra2Field.setDisable(false);
                break;
        }
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
