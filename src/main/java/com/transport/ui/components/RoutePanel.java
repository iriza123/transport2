package com.transport.ui.components;

import com.transport.controller.TransportController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class RoutePanel {
    
    private VBox root;
    private TransportController controller;
    private ListView<String> routeListView;
    
    public RoutePanel(TransportController controller) {
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
        
        TextField idField = new TextField();
        idField.setPromptText("Route ID");
        
        TextField originField = new TextField();
        originField.setPromptText("Origin");
        
        TextField destField = new TextField();
        destField.setPromptText("Destination");
        
        TextField distField = new TextField();
        distField.setPromptText("Distance (km)");
        
        Button addBtn = new Button("Add Route");
        addBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-text-fill: white;");
        addBtn.setOnAction(e -> {
            String result = controller.addRoute(
                idField.getText(),
                originField.getText(),
                destField.getText(),
                Double.parseDouble(distField.getText())
            );
            showAlert(result);
        });
        
        inputGrid.add(new Label("Route ID:"), 0, 0);
        inputGrid.add(idField, 1, 0);
        inputGrid.add(new Label("Origin:"), 0, 1);
        inputGrid.add(originField, 1, 1);
        inputGrid.add(new Label("Destination:"), 0, 2);
        inputGrid.add(destField, 1, 2);
        inputGrid.add(new Label("Distance:"), 0, 3);
        inputGrid.add(distField, 1, 3);
        inputGrid.add(addBtn, 1, 4);
        
        routeListView = new ListView<>(controller.getRouteList());
        routeListView.setPrefHeight(300);
        routeListView.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE); " +
                               "-fx-border-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                               "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        Label titleLabel = new Label("Route Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");
        
        Label listLabel = new Label("Available Routes:");
        listLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E;");
        
        root.getChildren().addAll(
            titleLabel,
            inputGrid,
            listLabel,
            routeListView
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
