package com.transport.ui.components;

import com.transport.backend.*;
import com.transport.controller.TransportController;
import com.transport.ui.MainView;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class BookingPanel {

    private VBox root;
    private TransportController controller;
    private MainView mainView;
    private Label statusLabel;

    public BookingPanel(TransportController controller, MainView mainView) {
        this.controller = controller;
        this.mainView = mainView;
        initializeUI();
    }

    private void initializeUI() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE);");

        Label titleLabel = new Label("Booking Management");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");

        // --- BOARD BUS SECTION ---
        Label boardTitle = new Label("Board Passenger onto Bus");
        boardTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E; -fx-font-size: 14px;");

        GridPane boardGrid = new GridPane();
        boardGrid.setHgap(10);
        boardGrid.setVgap(10);
        boardGrid.setMaxWidth(Double.MAX_VALUE);
        ColumnConstraints bc1 = new ColumnConstraints(130);
        ColumnConstraints bc2 = new ColumnConstraints();
        bc2.setHgrow(Priority.ALWAYS);
        boardGrid.getColumnConstraints().addAll(bc1, bc2);

        Label passengerBoardLabel = new Label("Passenger ID:");
        passengerBoardLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        TextField passengerBoardField = new TextField();
        passengerBoardField.setPromptText("e.g. P001");
        passengerBoardField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(passengerBoardField, Priority.ALWAYS);

        Label busPlateLabel = new Label("Bus Plate:");
        busPlateLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        TextField busPlateField = new TextField();
        busPlateField.setPromptText("e.g. BUS-001");
        busPlateField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(busPlateField, Priority.ALWAYS);

        boardGrid.add(passengerBoardLabel, 0, 0);
        boardGrid.add(passengerBoardField, 1, 0);
        boardGrid.add(busPlateLabel, 0, 1);
        boardGrid.add(busPlateField, 1, 1);

        Button boardBtn = new Button("Board Bus");
        boardBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 30;");

        boardBtn.setOnAction(e -> {
            String passengerId = passengerBoardField.getText().trim();
            String busPlate = busPlateField.getText().trim();

            if (passengerId.isEmpty() || busPlate.isEmpty()) {
                showStatus("Error: Passenger ID and Bus Plate are required", false);
                return;
            }

            Passenger passenger = controller.getManager().findPassengerById(passengerId);
            if (passenger == null) {
                showStatus("Error: Passenger '" + passengerId + "' not found. Add passenger first.", false);
                return;
            }

            if (passenger.isOnTrip()) {
                showStatus("Error: " + passenger.getName() + " is already on a trip [ON TRIP]. Cannot board again.", false);
                return;
            }

            Bus bus = findBusByPlate(busPlate);
            if (bus == null) {
                showStatus("Error: Bus '" + busPlate + "' not found. Add bus first in Vehicles tab.", false);
                return;
            }

            if (!bus.isInService()) {
                showStatus("Error: Bus " + busPlate + " is OUT OF SERVICE and cannot accept passengers.", false);
                return;
            }

            if (bus.getAvailableSeats() == 0) {
                showStatus("Error: Bus " + busPlate + " is FULL (" + bus.getCapacity() + "/" + bus.getCapacity() + " seats taken).", false);
                return;
            }

            String result = controller.boardBus(passenger, bus);
            showStatus(result, result.startsWith("Success"));
            if (result.startsWith("Success")) { passengerBoardField.clear(); busPlateField.clear(); mainView.refreshStats(); }
        });

        // --- ALIGHT BUS SECTION ---
        Separator sep = new Separator();

        Label alightTitle = new Label("Alight Passenger from Bus");
        alightTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #1A237E; -fx-font-size: 14px;");

        GridPane alightGrid = new GridPane();
        alightGrid.setHgap(10);
        alightGrid.setVgap(10);
        alightGrid.setMaxWidth(Double.MAX_VALUE);
        ColumnConstraints ac1 = new ColumnConstraints(130);
        ColumnConstraints ac2 = new ColumnConstraints();
        ac2.setHgrow(Priority.ALWAYS);
        alightGrid.getColumnConstraints().addAll(ac1, ac2);

        Label passengerAlightLabel = new Label("Passenger ID:");
        passengerAlightLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        TextField passengerAlightField = new TextField();
        passengerAlightField.setPromptText("e.g. P001");
        passengerAlightField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(passengerAlightField, Priority.ALWAYS);

        Label busAlightLabel = new Label("Bus Plate:");
        busAlightLabel.setStyle("-fx-text-fill: #1A237E; -fx-font-weight: bold;");
        TextField busAlightField = new TextField();
        busAlightField.setPromptText("e.g. BUS-001");
        busAlightField.setMaxWidth(Double.MAX_VALUE);
        GridPane.setHgrow(busAlightField, Priority.ALWAYS);

        alightGrid.add(passengerAlightLabel, 0, 0);
        alightGrid.add(passengerAlightField, 1, 0);
        alightGrid.add(busAlightLabel, 0, 1);
        alightGrid.add(busAlightField, 1, 1);

        Button alightBtn = new Button("Alight from Bus");
        alightBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 30;");

        alightBtn.setOnAction(e -> {
            String passengerId = passengerAlightField.getText().trim();
            String busPlate = busAlightField.getText().trim();

            if (passengerId.isEmpty() || busPlate.isEmpty()) {
                showStatus("Error: Passenger ID and Bus Plate are required", false);
                return;
            }

            Passenger passenger = controller.getManager().findPassengerById(passengerId);
            if (passenger == null) {
                showStatus("Error: Passenger '" + passengerId + "' not found", false);
                return;
            }

            Bus bus = findBusByPlate(busPlate);
            if (bus == null) {
                showStatus("Error: Bus '" + busPlate + "' not found", false);
                return;
            }

            if (!bus.getPassengers().contains(passenger)) {
                showStatus("Error: " + passenger.getName() + " is not on bus " + busPlate, false);
                return;
            }

            String result = controller.alightBus(passenger, bus);
            showStatus(result, result.startsWith("Success"));
            if (result.startsWith("Success")) { passengerAlightField.clear(); busAlightField.clear(); mainView.refreshStats(); }
        });

        // Status label
        statusLabel = new Label("");
        statusLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        statusLabel.setWrapText(true);
        statusLabel.setMaxWidth(Double.MAX_VALUE);

        root.setMaxWidth(Double.MAX_VALUE);
        root.getChildren().addAll(
                titleLabel,
                new Separator(),
                boardTitle,
                boardGrid,
                boardBtn,
                sep,
                alightTitle,
                alightGrid,
                alightBtn,
                statusLabel
        );
    }

    private Bus findBusByPlate(String plate) {
        for (Vehicle v : controller.getManager().getAllVehicles()) {
            if (v instanceof Bus && v.getPlateNumber().equalsIgnoreCase(plate)) {
                return (Bus) v;
            }
        }
        return null;
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
