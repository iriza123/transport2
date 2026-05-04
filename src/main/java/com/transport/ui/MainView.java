package com.transport.ui;

import com.transport.controller.TransportController;
import com.transport.ui.components.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class MainView {

    private BorderPane root;
    private TransportController controller;

    public MainView() {
        controller = new TransportController();
        initializeUI();
    }

    private void initializeUI() {
        root = new BorderPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #FFEBEE);");
        root.setTop(createHeader());
        root.setCenter(createTabPane());
        root.setBottom(createStatusBar());
    }

    private VBox createHeader() {
        VBox header = new VBox(6);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(15, 20, 15, 20));
        header.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.3), 10, 0, 0, 3);");

        Label title = new Label("Transport Management System");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        title.setWrapText(true);

        header.getChildren().add(title);
        return header;
    }

    public void refreshStats() {
    }

    private TabPane createTabPane() {
        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabMinWidth(80);

        VehiclePanel vehiclePanel = new VehiclePanel(controller, this);
        Tab vehicleTab = new Tab("Vehicles", wrapInScroll(vehiclePanel.getView()));

        PassengerPanel passengerPanel = new PassengerPanel(controller, this);
        Tab passengerTab = new Tab("Passengers", wrapInScroll(passengerPanel.getView()));

        RoutePanel routePanel = new RoutePanel(controller, this);
        Tab routeTab = new Tab("Routes", wrapInScroll(routePanel.getView()));

        TaxiPanel taxiPanel = new TaxiPanel(controller);
        Tab taxiTab = new Tab("Taxi Zones", taxiPanel.getView());

        BookingPanel bookingPanel = new BookingPanel(controller, this);
        Tab bookingTab = new Tab("Bookings", wrapInScroll(bookingPanel.getView()));

        LogPanel logPanel = new LogPanel(controller);
        Tab logTab = new Tab("Activity Log", wrapInScroll(logPanel.getView()));

        tabPane.getTabs().addAll(vehicleTab, passengerTab, routeTab, taxiTab, bookingTab, logTab);
        return tabPane;
    }

    private ScrollPane wrapInScroll(VBox content) {
        content.setMaxWidth(Double.MAX_VALUE);
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setFitToHeight(false);
        scroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
        return scroll;
    }

    private HBox createStatusBar() {
        HBox statusBar = new HBox(15);
        statusBar.setAlignment(Pos.CENTER_LEFT);
        statusBar.setPadding(new Insets(10, 20, 10, 20));
        statusBar.setStyle("-fx-background-color: linear-gradient(to right, #FFCDD2, #C5CAE9);");

        Label statusLabel = new Label("Ready");
        statusLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold; -fx-font-size: 13px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button saveButton = new Button("Save Data");
        saveButton.setMinWidth(100);
        saveButton.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;");
        saveButton.setOnAction(e -> {
            controller.saveAllData();
            statusLabel.setText("Data saved");
            refreshStats();
        });

        Button loadButton = new Button("Load Data");
        loadButton.setMinWidth(100);
        loadButton.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;");
        loadButton.setOnAction(e -> {
            controller.loadAllData();
            statusLabel.setText("Data loaded");
            refreshStats();
        });

        statusBar.getChildren().addAll(statusLabel, spacer, saveButton, loadButton);
        return statusBar;
    }

    public BorderPane getView() { return root; }
}
