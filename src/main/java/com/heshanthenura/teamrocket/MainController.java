package com.heshanthenura.teamrocket;

import com.heshanthenura.teamrocket.Database.DBConnection;
import com.heshanthenura.teamrocket.Database.DBServices;
import com.heshanthenura.teamrocket.Entity.Person;
import com.heshanthenura.teamrocket.Services.CSVService;
import com.opencsv.exceptions.CsvException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainController implements Initializable {

    @FXML
    private AnchorPane root;


    @FXML
    private TableView<Person> dataTable;
    @FXML
    private TableColumn<Person, String> nameColumn;
    @FXML
    private TableColumn<Person, String> netWorthColumn;
    @FXML
    private TableColumn<Person, Integer> ageColumn;
    @FXML
    private TableColumn<Person, String> countryOrTerritoryColumn;
    @FXML
    private TableColumn<Person, String> sourceColumn;
    @FXML
    private TableColumn<Person, String> industryColumn;


    @FXML
    private TextField ageField;
    @FXML
    private TextField countryOrTerritoryField;
    @FXML
    private Button deleteBtn;
    @FXML
    private TextField industryField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField netWorthField;
    @FXML
    private Button searchBtn;
    @FXML
    private TextField sourceField;
    @FXML
    private Button updateBtn;

    ObservableList<Person> personList = FXCollections.observableArrayList();
    DBServices dbServices = new DBServices();
    Logger logger = Logger.getLogger("info");

    @FXML
    void deleteSelectedPerson(MouseEvent event) {
        Person selectedPerson = dataTable.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            dbServices.deletePerson(selectedPerson);
            personList.remove(selectedPerson);
            clearFormFields();
        }
    }


    @FXML
    void searchPersons(MouseEvent event) {
        String name = nameField.getText();
        String netWorth = netWorthField.getText();
        Integer age = (ageField.getText().isEmpty()) ? null : Integer.parseInt(ageField.getText());
        String countryOrTerritory = countryOrTerritoryField.getText();
        String source = sourceField.getText();
        String industry = industryField.getText();
        ObservableList<Person> searchResults = dbServices.searchPersons(name, netWorth, age, countryOrTerritory, source, industry);
        personList.setAll(searchResults);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
            populateTable();
            getSelectedPersonData();
            dataTable.setItems(personList);
            root.setOnDragOver(this::onDragOver);
            root.setOnDragDropped(e -> {
                try {
                    onDragDropped(e);
                } catch (IOException | CsvException ex) {
                    throw new RuntimeException(ex);
                }
            });
            updateBtn.setOnAction(event -> updateSelectedPerson());
        });

    }

    void populateTable() {
        Platform.runLater(() -> {
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            netWorthColumn.setCellValueFactory(new PropertyValueFactory<>("netWorth"));
            ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
            countryOrTerritoryColumn.setCellValueFactory(new PropertyValueFactory<>("countryOrTerritory"));
            sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
            industryColumn.setCellValueFactory(new PropertyValueFactory<>("industry"));
            new DBConnection().createPersonTable();

            personList.addAll(dbServices.getAllPersons());

            dataTable.setItems(personList);
        });
    }

    void updateSelectedPerson() {
        Person selectedPerson = dataTable.getSelectionModel().getSelectedItem();

        if (selectedPerson != null) {
            String updatedName = nameField.getText().isEmpty() ? selectedPerson.getName() : nameField.getText();
            String updatedNetWorth = netWorthField.getText().isEmpty() ? selectedPerson.getNetWorth() : netWorthField.getText();
            int updatedAge = ageField.getText().isEmpty() ? selectedPerson.getAge() : Integer.parseInt(ageField.getText());
            String updatedCountryOrTerritory = countryOrTerritoryField.getText().isEmpty() ? selectedPerson.getCountryOrTerritory() : countryOrTerritoryField.getText();
            String updatedSource = sourceField.getText().isEmpty() ? selectedPerson.getSource() : sourceField.getText();
            String updatedIndustry = industryField.getText().isEmpty() ? selectedPerson.getIndustry() : industryField.getText();

            // Create an updated Person object
            Person updatedPerson = new Person(updatedName, updatedNetWorth, updatedAge, updatedCountryOrTerritory, updatedSource, updatedIndustry);

            // Update the database
            dbServices.updatePerson(selectedPerson, updatedPerson);

            // Update the ObservableList
            int selectedIndex = personList.indexOf(selectedPerson);
            personList.set(selectedIndex, updatedPerson);

            // Clear the form fields
            clearFormFields();
        }
    }


    void clearFormFields() {
        nameField.clear();
        netWorthField.clear();
        ageField.clear();
        countryOrTerritoryField.clear();
        sourceField.clear();
        industryField.clear();
    }

    void onDragOver(DragEvent event) {
        if (event.getGestureSource() != root && event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
        }
        event.consume();
    }

    void onDragDropped(DragEvent event) throws IOException, CsvException {
        Dragboard dragboard = event.getDragboard();
        boolean success = false;
        if (dragboard.hasFiles()) {
            List<File> files = dragboard.getFiles();
            if (!files.isEmpty()) {
                String filePath = files.get(0).getAbsolutePath();
                logger.info("File dropped: " + filePath);
                personList.addAll(new CSVService().getPersonData(filePath));
                logger.info("Done");
                logger.info(String.valueOf(personList.size()));
            }
            success = true;
        }
        event.setDropCompleted(success);
        event.consume();
    }

    void getSelectedPersonData() {

        dataTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) { // Check for a single click
                Person selectedPerson = dataTable.getSelectionModel().getSelectedItem();
                if (selectedPerson != null) {
                    nameField.setText(selectedPerson.getName());
                    netWorthField.setText(selectedPerson.getNetWorth());
                    ageField.setText(String.valueOf(selectedPerson.getAge()));
                    countryOrTerritoryField.setText(selectedPerson.getCountryOrTerritory());
                    sourceField.setText(selectedPerson.getSource());
                    industryField.setText(selectedPerson.getIndustry());
                }
            }
        });

    }

}
