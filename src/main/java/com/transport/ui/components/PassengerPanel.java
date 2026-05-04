package com.transport.ui.components;

import com.transport.backend.Passenger;
import com.transport.controller.TransportController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PassengerPanel {
    
    private VBox root;
    private TransportController controller;
    private ListView<Passenger> passengerListView;
    
    public PassengerPanel(TransportController controller) {
        this.controller = controller;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE);");
        
        GridPane inputGrid = new GridPane();
        inputGrid.setHgap(10);
        inputGrid.setVgap(10);
        
        TextField nameField = new TextField();
        nameField.setPromptText("Passenger Name");
        
        TextField idField = new TextField();
        idField.setPromptText("Passenger ID");
        
        Button addBtn = new Button("Add Passenger");
        addBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            String result = controller.addPassenger(nameField.getText(), idField.getText());
            showAlert(result);
            nameField.clear();
            idField.clear();
        });
        
        inputGrid.add(new Label("Name:"), 0, 0);
        inputGrid.add(nameField, 1, 0);
        inputGrid.add(new Label("ID:"), 0, 1);
        inputGrid.add(idField, 1, 1);
        inputGrid.add(addBtn, 1, 2);
        
        passengerListView = new ListView<>(controller.getPassengerList());
        passengerListView.setPrefHeight(300);
        passengerListView.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE); " +
                                   "-fx-border-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                                   "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        Button removeBtn = new Button("Remove Selected");
        removeBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-text-fill: white;");
        removeBtn.setOnAction(e -> {
            Passenger selected = passengerListView.getSelectionModel().getSelectedItem();
            String result = controller.removePassenger(selected);
            showAlert(result);
        });
        
        Label titleLabel = new Label("Passenger Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");
        
        Label listLabel = new Label("Registered Passengers:");
        listLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E;");
        
        root.getChildren().addAll(
            titleLabel,
            inputGrid,
            listLabel,
            passengerListView,
            removeBtn
        );
    }
    
    private void showAlert(String message) {
        Alert alert = new Alert(message.startsWith("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    public VBox getView() {
        return root;
    }
}
