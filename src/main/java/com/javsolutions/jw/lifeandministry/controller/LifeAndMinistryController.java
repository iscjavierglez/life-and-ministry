package com.javsolutions.jw.lifeandministry.controller;

import com.javsolutions.jw.lifeandministry.model.LifeAndMinistry;
import com.javsolutions.jw.lifeandministry.service.LifeAndMinistryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Controller;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class LifeAndMinistryController {
    @FXML
    private ComboBox<String> monthComboBox;
    @FXML
    private TableView<LifeAndMinistry> lifeAndMinistryTable;
    @FXML
    private TableColumn<LifeAndMinistry, String> titleColumn;
    @FXML
    private TableColumn<LifeAndMinistry, String> songsColumn;
    @FXML
    private TableColumn<LifeAndMinistry, String> treasuresColumn;
    @FXML
    private TableColumn<LifeAndMinistry, String> fieldMinistryColumn;
    @FXML
    private TableColumn<LifeAndMinistry, String> livingChristiansColumn;

    private final LifeAndMinistryService lifeAndMinistryService;
    private ObservableList<LifeAndMinistry> lifeAndMinistryList = FXCollections.observableArrayList();

    public LifeAndMinistryController(LifeAndMinistryService lifeAndMinistryService) {
        this.lifeAndMinistryService = lifeAndMinistryService;
    }

    @FXML
    public void initialize() {
        // Populate month combo box with all months
        List<String> months = Arrays.stream(java.time.Month.values())
                .map(m -> m.getDisplayName(TextStyle.FULL, Locale.getDefault()))
                .collect(Collectors.toList());
        monthComboBox.setItems(FXCollections.observableArrayList(months));
        String currentMonth = LocalDate.now().getMonth().getDisplayName(TextStyle.FULL, Locale.getDefault());
        monthComboBox.setValue(currentMonth);
        loadLifeAndMinistryForMonth(currentMonth);

        // Set up table columns
        titleColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitle()));
        songsColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.join(", ", cellData.getValue().getSongs())));
        treasuresColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getTreasuresFromGod().size())));
        fieldMinistryColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getApplyToTheFieldMinistry().size())));
        livingChristiansColumn.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(String.valueOf(cellData.getValue().getLivingAsChristians().size())));
    }

    @FXML
    protected void onLoadButtonClick() {
        String selectedMonth = monthComboBox.getValue();
        loadLifeAndMinistryForMonth(selectedMonth);
    }

    private void loadLifeAndMinistryForMonth(String month) {
        lifeAndMinistryList.clear();
        lifeAndMinistryList.addAll(lifeAndMinistryService.findAllByMonth(month));
        lifeAndMinistryTable.setItems(lifeAndMinistryList);
    }

    @FXML
    protected void onAddButtonClick() {
        // TODO: Show dialog/form to add new LifeAndMinistry entry
    }

    @FXML
    protected void onEditButtonClick() {
        // TODO: Show dialog/form to edit selected LifeAndMinistry entry
    }

    @FXML
    protected void onDeleteButtonClick() {
        LifeAndMinistry selected = lifeAndMinistryTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            lifeAndMinistryService.deleteById(selected.getId());
            loadLifeAndMinistryForMonth(monthComboBox.getValue());
        }
    }
}
