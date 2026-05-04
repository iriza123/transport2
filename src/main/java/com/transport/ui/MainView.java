package com.transport.ui;

import com.transport.controller.TransportController;
import com.transport.ui.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class MainView {
    
    private BorderPane root;
    private TransportController controller;
    
    private VehiclePanel vehiclePanel;
    private PassengerPanel passengerPanel;
    private RoutePanel routePanel;
    private TaxiPanel taxiPanel;
    
    public MainView() {
        controller = new TransportController();
        initializeUI();
    }
    
    private void initializeUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #FFEBEE);");
        
        VBox header = createHeader();
        root.setTop(header);
        
        TabPane tabPane = createTabPane();
        root.setCenter(tabPane);
        
        HBox statusBar = createStatusBar();
        root.setBottom(statusBar);
    }
    
    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(20));
        header.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                       "-fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.3), 10, 0, 0, 3);");
        
        Label title = new Label("Transport Management System");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: white;");
        
        Label subtitle = new Label("Manage Vehicles, Routes, and Passengers");
        subtitle.setStyle("-fx-font-size: 14px; -fx-text-fill: #E3F2FD;");
        
        header.getChildren().addAll(title, subtitle);
        return header;
    }
    
    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        
        vehiclePanel = new VehiclePanel(controller);
        Tab vehicleTab = new Tab("Vehicles", vehiclePanel.getView());
        
        passengerPanel = new PassengerPanel(controller);
        Tab passengerTab = new Tab("Passengers", passengerPanel.getView());
        
        routePanel = new RoutePanel(controller);
        Tab routeTab = new Tab("Routes", routePanel.getView());
        
        taxiPanel = new TaxiPanel(controller);
        Tab taxiTab = new Tab("Taxi Zones", taxiPanel.getView());
        
        tabPane.getTabs().addAll(vehicleTab, passengerTab, routeTab, taxiTab);
        
        return tabPane;
    }
    
    private HBox createStatusBar() {
        HBox statusBar = new HBox(20);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setPadding(new Insets(10));
        statusBar.setStyle("-fx-background-color: linear-gradient(to right, #FFCDD2, #C5CAE9);");
        
        Label statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        
        Button saveButton = new Button("Save Data");
        saveButton.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-text-fill: white;");
        saveButton.setOnAction(e -> controller.saveAllData());
        
        Button loadButton = new Button("Load Data");
        loadButton.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); -fx-text-fill: white;");
        loadButton.setOnAction(e -> controller.loadAllData());
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        statusBar.getChildren().addAll(statusLabel, spacer, saveButton, loadButton);
        return statusBar;
    }
    
    public BorderPane getView() {
        return root;
    }
}
