package com.transport.ui.components;

import com.transport.controller.TransportController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class RoutePanel {

    private VBox root;
    private TransportController controller;
    private ListView<String> routeListView;
    private Label statusLabel;

    public RoutePanel(TransportController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE);");

        Label titleLabel = new Label("Route Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");

        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);

        TextField idField = new TextField();
        idField.setPromptText("e.g. R001");

        TextField originField = new TextField();
        originField.setPromptText("e.g. Downtown");

        TextField destField = new TextField();
        destField.setPromptText("e.g. Airport");

        TextField distField = new TextField();
        distField.setPromptText("e.g. 25.5  (numbers only)");

        Label idLabel = new Label("Route ID:");
        idLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        Label originLabel = new Label("Origin:");
        originLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        Label destLabel = new Label("Destination:");
        destLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        Label distLabel = new Label("Distance (km):");
        distLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");

        inputGrid.add(idLabel, 0, 0);
        inputGrid.add(idField, 1, 0);
        inputGrid.add(originLabel, 0, 1);
        inputGrid.add(originField, 1, 1);
        inputGrid.add(destLabel, 0, 2);
        inputGrid.add(destField, 1, 2);
        inputGrid.add(distLabel, 0, 3);
        inputGrid.add(distField, 1, 3);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("Add Route");
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
            String routeId = idField.getText().trim();
            String origin = originField.getText().trim();
            String dest = destField.getText().trim();
            String distText = distField.getText().trim();

            if (routeId.isEmpty() || origin.isEmpty() || dest.isEmpty() || distText.isEmpty()) {
                showStatus("Error: All fields are required", false);
                return;
            }

            double distance;
            try {
                distText = distText.replaceAll("[^0-9.]", "").trim();
                if (distText.isEmpty()) {
                    showStatus("Error: Distance must be a number (e.g. 25.5)", false);
                    return;
                }
                distance = Double.parseDouble(distText);
            } catch (NumberFormatException ex) {
                showStatus("Error: Distance must be a number (e.g. 25.5)", false);
                return;
            }

            String result = controller.addRoute(routeId, origin, dest, distance);
            showStatus(result, result.startsWith("Success"));

            if (result.startsWith("Success")) {
                idField.clear();
                originField.clear();
                destField.clear();
                distField.clear();
            }
        });

        removeBtn.setOnAction(e -> {
            String selected = routeListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showStatus("Error: Please select a route to remove", false);
                return;
            }
            String routeId = selected.split(":")[0].trim();
            String result = controller.removeRoute(routeId);
            showStatus(result, result.startsWith("Success"));
        });

        Label listLabel = new Label("Available Routes:");
        listLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E;");

        routeListView = new ListView<>(controller.getRouteList());
        routeListView.setPrefHeight(250);
        routeListView.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE); " +
                "-fx-border-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");

        root.getChildren().addAll(
                titleLabel,
                inputGrid,
                buttonBox,
                statusLabel,
                listLabel,
                routeListView
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
