package com.transport.ui.components;

import com.transport.backend.Passenger;
import com.transport.controller.TransportController;
import com.transport.ui.MainView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PassengerPanel {

    private VBox root;
    private TransportController controller;
    private MainView mainView;
    private ListView<Passenger> passengerListView;
    private Label statusLabel;

    public PassengerPanel(TransportController controller, MainView mainView) {
        this.controller = controller;
        this.mainView = mainView;
        initializeUI();
    }

    private void initializeUI() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE);");

        Label titleLabel = new Label("Passenger Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Alice Brown");

        TextField idField = new TextField();
        idField.setPromptText("e.g. P001");

        Label nameLabel = new Label("Name:");
        nameLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        Label idLabel = new Label("ID:");
        idLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");

        inputGrid.add(nameLabel, 0, 0);
        inputGrid.add(nameField, 1, 0);
        inputGrid.add(idLabel, 0, 1);
        inputGrid.add(idField, 1, 1);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("Add Passenger");
        addBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");

        Button removeBtn = new Button("Remove Selected");
        removeBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");

        buttonBox.getChildren().addAll(addBtn, removeBtn);

        statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        statusLabel.setWrapText(true);

        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            String id = idField.getText().trim();
            if (name.isEmpty() || id.isEmpty()) { showStatus("Error: Name and ID are required", false); return; }
            String result = controller.addPassenger(name, id);
            showStatus(result, result.startsWith("Success"));
            if (result.startsWith("Success")) { nameField.clear(); idField.clear(); mainView.refreshStats(); }
        });

        removeBtn.setOnAction(e -> {
            Passenger selected = passengerListView.getSelectionModel().getSelectedItem();
            if (selected == null) { showStatus("Error: Select a passenger to remove", false); return; }
            if (selected.isOnTrip()) { showStatus("Error: Cannot remove passenger currently on a trip", false); return; }
            String result = controller.removePassenger(selected);
            showStatus(result, result.startsWith("Success"));
            if (result.startsWith("Success")) mainView.refreshStats();
        });

        Label listLabel = new Label("Registered Passengers:");
        listLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E;");

        passengerListView = new ListView<>(controller.getPassengerList());
        passengerListView.setPrefHeight(220);
        passengerListView.setMaxWidth(Double.MAX_VALUE);
        passengerListView.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE); " +
                "-fx-border-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        HBox searchBox = new HBox(10);
        searchBox.setAlignment(Pos.CENTER_LEFT);
        TextField searchField = new TextField();
        searchField.setPromptText("Search by ID");
        searchField.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(searchField, Priority.ALWAYS);
        Button searchBtn = new Button("Search");
        searchBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;");
        searchBox.getChildren().addAll(searchField, searchBtn);

        Button sortBtn = new Button("Sort by Name");
        sortBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;");

        searchBtn.setOnAction(e -> { String r = controller.searchPassenger(searchField.getText()); showStatus(r, r.startsWith("Found")); });
        sortBtn.setOnAction(e -> { controller.sortPassengersByName(); showStatus("Sorted by name", true); });

        root.getChildren().addAll(
                titleLabel, inputGrid, buttonBox,
                new Separator(),
                searchBox, sortBtn,
                statusLabel, listLabel, passengerListView
        );
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
