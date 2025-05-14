package com.javsolutions.jw.lifeandministry.controller;

import com.javsolutions.jw.lifeandministry.model.Publisher;
import com.javsolutions.jw.lifeandministry.service.PublisherService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Controller for the publishers-view.fxml file.
 * This controller handles the display of publishers in a TableView.
 */
@Slf4j
@Controller
public class PublishersController {
    @FXML
    private TableView<Publisher> publishersTable;

    @FXML
    private TableColumn<Publisher, String> nameColumn;

    @FXML
    private TableColumn<Publisher, String> genderColumn;

    @FXML
    private TableColumn<Publisher, String> privilegeColumn;

    @FXML
    private TableColumn<Publisher, Boolean> activeColumn;

    @FXML
    private TextField searchField;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    private final PublisherService publisherService;

    private ObservableList<Publisher> publishersList = FXCollections.observableArrayList();

    public PublishersController(PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    /**
     * Initialize the controller.
     * This method is called after the FXML fields have been injected.
     */
    @FXML
    public void initialize() {
        log.info("Initializing PublishersController");

        // Configure the table columns
        nameColumn.setCellValueFactory(cellData -> {
            Publisher publisher = cellData.getValue();
            if (publisher == null) {
                return javafx.beans.binding.Bindings.createStringBinding(() -> "");
            }
            String firstName = publisher.getFirstName() != null ? publisher.getFirstName() : "";
            String lastName = publisher.getLastName() != null ? publisher.getLastName() : "";
            String fullName = firstName + " " + lastName;
            return javafx.beans.binding.Bindings.createStringBinding(() -> fullName);
        });

        genderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        privilegeColumn.setCellValueFactory(new PropertyValueFactory<>("privilege"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));

        // Format the active column to display checkmarks
        activeColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item ? "✓" : "");
                }
            }
        });

        // Load publishers from the database
        loadPublishers();

        // Set up search functionality
        searchField.textProperty()
                .addListener((observable, oldValue, newValue) -> filterPublishers(newValue));
    }

    /**
     * Loads publishers from the database and displays them in the TableView.
     */
    private void loadPublishers() {
        try {
            log.info("Loading publishers from database");
            List<Publisher> publishers = publisherService.findAll();
            publishersList.clear();
            publishersList.addAll(publishers);
            publishersTable.setItems(publishersList);
            log.info("Loaded {} publishers", publishers.size());
        } catch (Exception e) {
            log.error("Error loading publishers", e);
        }
    }

    /**
     * Filters the publishers list based on the search text.
     * 
     * @param searchText the text to search for
     */
    private void filterPublishers(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            publishersTable.setItems(publishersList);
            return;
        }

        ObservableList<Publisher> filteredList = FXCollections.observableArrayList();
        String lowerCaseFilter = searchText.toLowerCase();

        for (Publisher publisher : publishersList) {
            // Safely check if any field contains the filter text, handling null values
            if (containsIgnoreCase(publisher.getFirstName(), lowerCaseFilter) ||
                containsIgnoreCase(publisher.getLastName(), lowerCaseFilter) ||
                containsIgnoreCase(publisher.getShortName(), lowerCaseFilter) ||
                containsIgnoreCase(publisher.getPrivilege(), lowerCaseFilter)) {
                filteredList.add(publisher);
            }
        }

        publishersTable.setItems(filteredList);
    }

    /**
     * Handles the add button click event.
     */
    @FXML
    protected void onAddButtonClick() {
        log.info("Add button clicked");
        // This would typically open a dialog or navigate to a form to add a new publisher
    }

    /**
     * Handles the edit button click event.
     */
    @FXML
    protected void onEditButtonClick() {
        log.info("Edit button clicked");
        Publisher selectedPublisher = publishersTable.getSelectionModel().getSelectedItem();
        if (selectedPublisher != null) {
            // This would typically open a dialog or navigate to a form to edit the selected publisher
            log.info("Selected publisher for editing: {}", selectedPublisher);
        } else {
            log.info("No publisher selected for editing");
        }
    }

    /**
     * Handles the delete button click event.
     */
    @FXML
    protected void onDeleteButtonClick() {
        log.info("Delete button clicked");
        Publisher selectedPublisher = publishersTable.getSelectionModel().getSelectedItem();
        if (selectedPublisher != null) {
            // This would typically show a confirmation dialog before deleting
            log.info("Selected publisher for deletion: {}", selectedPublisher);
        } else {
            log.info("No publisher selected for deletion");
        }
    }

    /**
     * Refreshes the publishers list from the database.
     * This can be called from other controllers when a publisher is added or updated.
     */
    public void refreshPublishers() {
        loadPublishers();
    }

    /**
     * Helper method to check if a string contains another string, ignoring case.
     * Safely handles null values.
     * 
     * @param source the source string to check
     * @param target the substring to look for
     * @return true if source contains target (ignoring case), false otherwise or if either is null
     */
    private boolean containsIgnoreCase(String source, String target) {
        return source != null && target != null && source.toLowerCase().contains(target);
    }
}
