package com.heshanthenura.teamrocket;

import com.heshanthenura.teamrocket.Database.DBServices;
import com.heshanthenura.teamrocket.Entity.Person;
import com.heshanthenura.teamrocket.Services.CSVService;
import com.opencsv.exceptions.CsvException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
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


    ObservableList<Person> personList = FXCollections.observableArrayList();
    DBServices dbServices = new DBServices();
    Logger logger = Logger.getLogger("info");

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(()->{

            populateTable();
            dataTable.setItems(personList);
            root.setOnDragOver(this::onDragOver);
            root.setOnDragDropped(e->{
                try {
                    onDragDropped(e);
                } catch (IOException | CsvException ex) {
                    throw new RuntimeException(ex);
                }
            });
        });
    }

    void populateTable(){
        Platform.runLater(()->{
            nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
            netWorthColumn.setCellValueFactory(new PropertyValueFactory<>("netWorth"));
            ageColumn.setCellValueFactory(new PropertyValueFactory<>("age"));
            countryOrTerritoryColumn.setCellValueFactory(new PropertyValueFactory<>("countryOrTerritory"));
            sourceColumn.setCellValueFactory(new PropertyValueFactory<>("source"));
            industryColumn.setCellValueFactory(new PropertyValueFactory<>("industry"));

//       personList.addAll(
//                new Person("John Doe", "1000000", 30, "USA", "Tech Corp", "IT"),
//                new Person("Jane Smith", "1500000", 35, "UK", "Finance Ltd", "Finance")
//        );
            personList.addAll(dbServices.getAllPersons());
            dataTable.setItems(personList);
        });
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

}
