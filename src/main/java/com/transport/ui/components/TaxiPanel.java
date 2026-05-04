package com.transport.ui.components;

import com.transport.controller.TransportController;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TaxiPanel {
    
    private VBox root;
    private TransportController controller;
    
    public TaxiPanel(TransportController controller) {
        this.controller = controller;
        initializeUI();
    }
    
    private void initializeUI() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE);");
        
        Label title = new Label("Taxi Zone Management");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");
        
        TextField zoneField = new TextField();
        zoneField.setPromptText("Enter Zone Name");
        
        ListView<String> taxiListView = new ListView<>();
        taxiListView.setPrefHeight(300);
        taxiListView.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE); " +
                              "-fx-border-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                              "-fx-border-width: 2; -fx-border-radius: 10; -fx-background-radius: 10;");
        
        Button searchBtn = new Button("Search Zone");
        searchBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-text-fill: white;");
        searchBtn.setOnAction(e -> {
            String zone = zoneField.getText();
            if (!zone.isEmpty()) {
                taxiListView.setItems(controller.getTaxisInZone(zone));
            }
        });
        
        Label zoneLabel = new Label("Zone Name:");
        zoneLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E;");
        
        Label listLabel = new Label("Taxis in Zone:");
        listLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E;");
        
        root.getChildren().addAll(
            title,
            zoneLabel,
            zoneField,
            searchBtn,
            listLabel,
            taxiListView
        );
    }
    
    public VBox getView() {
        return root;
    }
}
