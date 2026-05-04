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
    
    public VehiclePanel(TransportController controller) {
        this.controller = controller;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE);");
        
        GridPane inputGrid = createInputGrid();
        
        vehicleListView = new ListView<>(controller.getVehicleList());
        vehicleListView.setPrefHeight(300);
        vehicleListView.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE); " +
                                 "-fx-border-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                                 "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        HBox buttonBox = createButtonBox();
        
        Label titleLabel = new Label("Vehicle Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");
        
        Label listLabel = new Label("Registered Vehicles:");
        listLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E;");
        
        root.getChildren().addAll(
            titleLabel,
            inputGrid,
            listLabel,
            vehicleListView,
            buttonBox
        );
    }
    
    private GridPane createInputGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Bus", "Taxi", "Train");
        typeCombo.setValue("Bus");
        
        TextField plateField = new TextField();
        plateField.setPromptText("Plate Number");
        
        TextField fuelField = new TextField();
        fuelField.setPromptText("Fuel Level");
        
        TextField extra1Field = new TextField();
        extra1Field.setPromptText("Capacity/Driver/Schedule");
        
        TextField extra2Field = new TextField();
        extra2Field.setPromptText("Route ID/Cars");
        
        grid.add(new Label("Type:"), 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(new Label("Plate:"), 0, 1);
        grid.add(plateField, 1, 1);
        grid.add(new Label("Fuel:"), 0, 2);
        grid.add(fuelField, 1, 2);
        grid.add(new Label("Extra 1:"), 0, 3);
        grid.add(extra1Field, 1, 3);
        grid.add(new Label("Extra 2:"), 0, 4);
        grid.add(extra2Field, 1, 4);
        
        Button addBtn = new Button("Add Vehicle");
        addBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            String result = controller.addVehicle(
                typeCombo.getValue(),
                plateField.getText(),
                Integer.parseInt(fuelField.getText()),
                extra1Field.getText(),
                extra2Field.getText()
            );
            showAlert(result);
        });
        
        grid.add(addBtn, 1, 5);
        
        return grid;
    }
    
    private HBox createButtonBox() {
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        
        Button removeBtn = new Button("Remove Selected");
        removeBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-text-fill: white;");
        removeBtn.setOnAction(e -> {
            Vehicle selected = vehicleListView.getSelectionModel().getSelectedItem();
            String result = controller.removeVehicle(selected);
            showAlert(result);
        });
        
        box.getChildren().add(removeBtn);
        return box;
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(message.startsWith("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Vehicle Management");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public VBox getView() {
        return root;
    }
}
