package com.javsolutions.jw.lifeandministry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Legacy JavaFX application class.
 * It's recommended to use LifeAndMinistryApplication instead, which integrates with Spring Boot.
 */
public class HelloApplication extends Application {
    @Override
    public void init() {
        // MongoDB connection is now managed by Spring Boot
        System.out.println("Application initializing");
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Life and Ministry");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        // MongoDB connection is now managed by Spring Boot
        System.out.println("Application stopping");
    }

    public static void main(String[] args) {
        launch();
    }
}
