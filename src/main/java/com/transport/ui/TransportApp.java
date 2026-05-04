package com.transport.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TransportApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Transport Management System");
        
        MainView mainView = new MainView();
        Scene scene = new Scene(mainView.getView(), 1000, 700);
        
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
