package com.javsolutions.jw.lifeandministry.controller;

import com.javsolutions.jw.lifeandministry.model.Publisher;
import com.javsolutions.jw.lifeandministry.repository.PublisherRepository;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Controller for the publisher-form.fxml file.
 * This controller handles the form for adding new publishers.
 */
@Slf4j
@Controller
public class PublisherFormController {
    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField shortNameField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private ComboBox<String> privilegeComboBox;

    @FXML
    private CheckBox matriculatedCheckBox;

    @FXML
    private CheckBox activeCheckBox;

    @FXML
    private Label messageLabel;

    @Autowired
    private PublisherRepository publisherRepository;

    /**
     * Initialize the controller.
     * This method is called after the FXML fields have been injected.
     */
    @FXML
    public void initialize() {
        // Initialize gender options
        List<String> genderOptions = Arrays.asList("Male", "Female");
        genderComboBox.setItems(FXCollections.observableArrayList(genderOptions));

        // Initialize privilege options
        List<String> privilegeOptions = Arrays.asList("Publisher", "Auxiliary Pioneer", "Regular Pioneer", "Elder", "Ministerial Servant");
        privilegeComboBox.setItems(FXCollections.observableArrayList(privilegeOptions));

        // Set default values
        activeCheckBox.setSelected(true);

        // Clear the message label
        messageLabel.setText("");
    }

    /**
     * Handles the save button click event.
     * Validates the form and saves the publisher to the database.
     */
    @FXML
    protected void onSaveButtonClick() {
        log.info("Save button clicked");

        // Validate form
        if (!validateForm()) {
            return;
        }

        try {
            // Create a new publisher
            Publisher publisher = Publisher.builder()
                    .firstName(firstNameField.getText())
                    .lastName(lastNameField.getText())
                    .shortName(shortNameField.getText())
                    .gender(genderComboBox.getValue())
                    .privilege(privilegeComboBox.getValue())
                    .isMatriculated(matriculatedCheckBox.isSelected())
                    .active(activeCheckBox.isSelected())
                    .build();

            // Save the publisher to the database
            publisherRepository.save(publisher);

            // Show success message
            messageLabel.setText("Publisher saved successfully!");
            messageLabel.setStyle("-fx-text-fill: green;");

            // Clear the form
            clearForm();

            log.info("Publisher saved: {}", publisher);
        } catch (Exception e) {
            // Show error message
            messageLabel.setText("Error saving publisher: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
            log.error("Error saving publisher", e);
        }
    }

    /**
     * Handles the clear button click event.
     * Clears all form fields.
     */
    @FXML
    protected void onClearButtonClick() {
        log.info("Clear button clicked");
        clearForm();
        messageLabel.setText("");
    }

    /**
     * Validates the form fields.
     * 
     * @return true if the form is valid, false otherwise
     */
    private boolean validateForm() {
        // Check if required fields are filled
        if (firstNameField.getText().isEmpty()) {
            messageLabel.setText("First name is required");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (lastNameField.getText().isEmpty()) {
            messageLabel.setText("Last name is required");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (shortNameField.getText().isEmpty()) {
            messageLabel.setText("Short name is required");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (genderComboBox.getValue() == null) {
            messageLabel.setText("Gender is required");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        if (privilegeComboBox.getValue() == null) {
            messageLabel.setText("Privilege is required");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }

        return true;
    }

    /**
     * Clears all form fields.
     */
    private void clearForm() {
        firstNameField.clear();
        lastNameField.clear();
        shortNameField.clear();
        genderComboBox.setValue(null);
        privilegeComboBox.setValue(null);
        matriculatedCheckBox.setSelected(false);
        activeCheckBox.setSelected(true);
    }
}