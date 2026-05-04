package com.transport.ui.components;

import com.transport.backend.Vehicle;
import com.transport.controller.TransportController;
import com.transport.ui.MainView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class VehiclePanel {

    private VBox root;
    private TransportController controller;
    private MainView mainView;
    private ListView<Vehicle> vehicleListView;
    private Label statusLabel;
    private Label extra1Label;
    private Label extra2Label;
    private TextField extra1Field;
    private TextField extra2Field;

    public VehiclePanel(TransportController controller, MainView mainView) {
        this.controller = controller;
        this.mainView = mainView;
        initializeUI();
    }

    private void initializeUI() {
        root = new VBox(12);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE);");

        Label titleLabel = new Label("Vehicle Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(15);
        inputGrid.setVgap(10);
        ColumnConstraints c1 = new ColumnConstraints(130);
        ColumnConstraints c2 = new ColumnConstraints();
        c2.setHgrow(Priority.ALWAYS);
        inputGrid.getColumnConstraints().addAll(c1, c2);

        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Bus", "Taxi", "Train");
        typeCombo.setValue("Bus");
        typeCombo.setMaxWidth(Double.MAX_VALUE);

        TextField plateField = new TextField();
        plateField.setPromptText("e.g. BUS-001");
        plateField.setMaxWidth(Double.MAX_VALUE);

        TextField fuelField = new TextField();
        fuelField.setPromptText("e.g. 80");
        fuelField.setMaxWidth(Double.MAX_VALUE);

        extra1Label = new Label("Capacity:");
        extra1Label.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        extra1Field = new TextField();
        extra1Field.setPromptText("e.g. 50");
        extra1Field.setMaxWidth(Double.MAX_VALUE);

        extra2Label = new Label("Route ID:");
        extra2Label.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        extra2Field = new TextField();
        extra2Field.setPromptText("e.g. R001");
        extra2Field.setMaxWidth(Double.MAX_VALUE);

        typeCombo.setOnAction(e -> updateFieldLabels(typeCombo.getValue()));

        inputGrid.add(styledLabel("Type:"), 0, 0);        inputGrid.add(typeCombo, 1, 0);
        inputGrid.add(styledLabel("Plate Number:"), 0, 1); inputGrid.add(plateField, 1, 1);
        inputGrid.add(styledLabel("Fuel (L):"), 0, 2);    inputGrid.add(fuelField, 1, 2);
        inputGrid.add(extra1Label, 0, 3);                  inputGrid.add(extra1Field, 1, 3);
        inputGrid.add(extra2Label, 0, 4);                  inputGrid.add(extra2Field, 1, 4);

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search by plate number");
        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        Button searchBtn = new Button("Search");
        searchBtn.setStyle(btnStyle());
        searchBox.getChildren().addAll(searchField, searchBtn);

        HBox sortBox = new HBox(10);
        sortBox.setAlignment(Pos.CENTER_LEFT);
        Button sortPlateBtn = new Button("Sort by Plate");
        sortPlateBtn.setStyle(btnStyle());
        Button sortFuelBtn = new Button("Sort by Fuel");
        sortFuelBtn.setStyle(btnStyle());
        sortBox.getChildren().addAll(styledLabel("Sort:"), sortPlateBtn, sortFuelBtn);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);
        Button addBtn = new Button("Add Vehicle");
        addBtn.setStyle(btnStyle());
        Button removeBtn = new Button("Remove Selected");
        removeBtn.setStyle(btnStyle());
        buttonBox.getChildren().addAll(addBtn, removeBtn);

        statusLabel = new Label("");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        vehicleListView = new ListView<>(controller.getVehicleList());
        vehicleListView.setPrefHeight(200);
        vehicleListView.setMaxWidth(Double.MAX_VALUE);
        vehicleListView.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE); " +
                "-fx-border-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-border-width: 2;");

        addBtn.setOnAction(e -> {
            String plate = plateField.getText().trim();
            String fuelText = fuelField.getText().trim();
            String extra1 = extra1Field.getText().trim();
            String extra2 = extra2Field.getText().trim();
            if (plate.isEmpty() || fuelText.isEmpty() || extra1.isEmpty()) {
                showStatus("Error: Plate, Fuel and " + extra1Label.getText() + " are required", false);
                return;
            }
            int fuel;
            try { fuel = Integer.parseInt(fuelText); }
            catch (NumberFormatException ex) { showStatus("Error: Fuel must be a number (e.g. 80)", false); return; }
            String result = controller.addVehicle(typeCombo.getValue(), plate, fuel, extra1, extra2);
            showStatus(result, result.startsWith("Success"));
            if (result.startsWith("Success")) {
                plateField.clear(); fuelField.clear(); extra1Field.clear(); extra2Field.clear();
                mainView.refreshStats();
            }
        });

        removeBtn.setOnAction(e -> {
            Vehicle selected = vehicleListView.getSelectionModel().getSelectedItem();
            if (selected == null) { showStatus("Error: Select a vehicle to remove", false); return; }
            String result = controller.removeVehicle(selected);
            showStatus(result, result.startsWith("Success"));
            if (result.startsWith("Success")) mainView.refreshStats();
        });

        searchBtn.setOnAction(e -> {
            String result = controller.searchVehicle(searchField.getText());
            showStatus(result, result.startsWith("Found"));
        });

        sortPlateBtn.setOnAction(e -> { controller.sortVehiclesByPlate(); showStatus("Sorted by plate number", true); });
        sortFuelBtn.setOnAction(e -> { controller.sortVehiclesByFuel(); showStatus("Sorted by fuel level", true); });

        root.getChildren().addAll(
                titleLabel, inputGrid, buttonBox,
                new Separator(),
                searchBox, sortBox,
                statusLabel,
                styledLabel("Registered Vehicles:"),
                vehicleListView
        );
    }

    private void updateFieldLabels(String type) {
        switch (type) {
            case "Bus":
                extra1Label.setText("Capacity:");
                extra1Field.setPromptText("e.g. 50");
                extra2Label.setText("Route ID:");
                extra2Field.setPromptText("e.g. R001");
                extra2Field.setDisable(false);
                break;
            case "Taxi":
                extra1Label.setText("Driver Name:");
                extra1Field.setPromptText("e.g. John Smith");
                extra2Label.setText("(not needed)");
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

    private Label styledLabel(String text) {
        Label l = new Label(text);
        l.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        return l;
    }

    private String btnStyle() {
        return "-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;";
    }

    private void showStatus(String message, boolean success) {
        statusLabel.setText(message);
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: " + (success ? "#1B5E20;" : "#B71C1C;"));
    }

    public VBox getView() { return root; }
}
