package com.javsolutions.jw.lifeandministry.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import com.javsolutions.jw.lifeandministry.FXMLLoaderFactory;

/**
 * Controller for the application-view.fxml file.
 * This is the main controller for the application.
 */
@Controller
public class ApplicationController {
    private static final Logger log = LoggerFactory.getLogger(ApplicationController.class);

    @FXML
    private Button homeButton;

    @FXML
    private Button publishersButton;

    @FXML
    private Button scheduleButton;

    @FXML
    private Button reportsButton;

    @FXML
    private Button settingsButton;

    @FXML
    private Label statusLabel;

    @FXML
    private BorderPane mainBorderPane;

    /**
     * Initialize method called after all @FXML annotated members have been injected.
     */
    @FXML
    public void initialize() {
        // Load the home view by default
        loadView("/com/javsolutions/jw/lifeandministry/home-view.fxml");
    }

    /**
     * Loads a view into the center of the main BorderPane.
     * 
     * @param fxmlPath the path to the FXML file to load
     */
    private void loadView(String fxmlPath) {
        try {
            FXMLLoader loader = FXMLLoaderFactory.createLoader(fxmlPath);
            Node view = loader.load();
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            log.error("Error loading view: " + fxmlPath, e);
            statusLabel.setText("Error loading view");
        }
    }


    /**
     * Handles the home button click event.
     */
    @FXML
    protected void onHomeButtonClick() {
        log.info("Home button clicked");
        statusLabel.setText("Home section selected");
        loadView("/com/javsolutions/jw/lifeandministry/home-view.fxml");
    }

    /**
     * Handles the publishers button click event.
     * Shows the publishers view in the center panel.
     */
    @FXML
    protected void onPublishersButtonClick() {
        log.info("Publishers button clicked");
        statusLabel.setText("Publishers section selected");
        loadView("/com/javsolutions/jw/lifeandministry/publishers-view.fxml");
    }

    /**
     * Handles the schedule button click event.
     */
    @FXML
    protected void onScheduleButtonClick() {
        log.info("Schedule button clicked");
        statusLabel.setText("Schedule section selected");
        loadView("/com/javsolutions/jw/lifeandministry/schedule-view.fxml");
    }

    /**
     * Handles the reports button click event.
     */
    @FXML
    protected void onReportsButtonClick() {
        log.info("Reports button clicked");
        statusLabel.setText("Reports section selected");
        loadView("/com/javsolutions/jw/lifeandministry/reports-view.fxml");
    }

    /**
     * Handles the settings button click event.
     */
    @FXML
    protected void onSettingsButtonClick() {
        log.info("Settings button clicked");
        statusLabel.setText("Settings section selected");
        loadView("/com/javsolutions/jw/lifeandministry/settings-view.fxml");
    }
}