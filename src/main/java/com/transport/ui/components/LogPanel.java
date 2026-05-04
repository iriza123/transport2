package com.transport.ui.components;

import com.transport.controller.TransportController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class LogPanel {

    private VBox root;
    private TransportController controller;
    private ListView<String> logListView;

    public LogPanel(TransportController controller) {
        this.controller = controller;
        initializeUI();
    }

    private void initializeUI() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #E8EAF6, #FFEBEE);");

        Label titleLabel = new Label("System Activity Log");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #1A237E;");

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setStyle("-fx-background-color: linear-gradient(to right, #EF5350, #5C6BC0); " +
                "-fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10; -fx-padding: 8 20;");

        logListView = new ListView<>(controller.getLogList());
        logListView.setMaxWidth(Double.MAX_VALUE);
        VBox.setVgrow(logListView, Priority.ALWAYS);
        logListView.setStyle("-fx-background-color: #1A1A2E; -fx-border-color: #5C6BC0; " +
                "-fx-border-width: 2; -fx-border-radius: 5;");
        logListView.setCellFactory(lv -> new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("-fx-background-color: #1A1A2E;");
                } else {
                    setText(item);
                    setStyle("-fx-text-fill: #90CAF9; -fx-background-color: #1A1A2E; -fx-font-family: monospace;");
                }
            }
        });

        refreshBtn.setOnAction(e -> controller.loadAllData());

        HBox topBar = new HBox(10);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.getChildren().addAll(titleLabel, new Region() {{ HBox.setHgrow(this, Priority.ALWAYS); }}, refreshBtn);

        root.getChildren().addAll(topBar, logListView);
    }

    public VBox getView() { return root; }
}
