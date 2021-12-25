package org.but.feec.library.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.but.feec.library.App;
import org.but.feec.library.api.PersonBasicView;
import org.but.feec.library.api.PersonDetailView;
import org.but.feec.library.data.PersonRepository;
import org.but.feec.library.exceptions.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.but.feec.library.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class PersonsController {

    private static final Logger logger = LoggerFactory.getLogger(PersonsController.class);

    @FXML
    public Button addPersonButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button filterButton;
    @FXML
    public TextField searchBar;
    @FXML
    private TableColumn<PersonBasicView, Long> personsId;
    @FXML
    private TableColumn<PersonBasicView, Long> isbn;
    @FXML
    private TableColumn<PersonBasicView, String> authorName;
    @FXML
    private TableColumn<PersonBasicView, String> bookTitle;
    @FXML
    private TableColumn<PersonBasicView, String> authorSurname;
    @FXML
    private TableColumn<PersonBasicView, String> datePublished;
    @FXML
    private TableView<PersonBasicView> systemPersonsTableView;
//    @FXML
//    public MenuItem exitMenuItem;

    private PersonService personService;
    private PersonRepository personRepository;



    @FXML
    private void initialize() {
        personRepository = new PersonRepository();
        personService = new PersonService(personRepository);
//        GlyphsDude.setIcon(exitMenuItem, FontAwesomeIcon.CLOSE, "1em");

        personsId.setCellValueFactory(new PropertyValueFactory<PersonBasicView, Long>("id"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("bookTitle"));
        isbn.setCellValueFactory(new PropertyValueFactory<PersonBasicView, Long>("isbn"));
        authorName.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("authorName"));
        authorSurname.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("authorSurname"));
        datePublished.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("datePublished"));



        ObservableList<PersonBasicView> observablePersonList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonList);

        systemPersonsTableView.getSortOrder().add(personsId);

        initializeTableViewSelection();
        //  loadIcons();

        logger.info("PersonsController initialized");
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit person");
        MenuItem detailedView = new MenuItem("Detailed person view");
        MenuItem delete = new MenuItem ("Delete person");
            edit.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/PersonEdit.fxml"));
                Stage stage = new Stage();
                stage.setUserData(personView);
                stage.setTitle("BDS JavaFX Edit Person");

                PersonsEditController controller = new PersonsEditController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 600, 500);

                stage.setScene(scene);

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }
        });

        detailedView.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/PersonsDetailView.fxml"));
                Stage stage = new Stage();

                Long personId = personView.getId();
                PersonDetailView personDetailView = personService.getPersonDetailView(personId);

                stage.setUserData(personDetailView);
                stage.setTitle("BDS JavaFX Persons Detailed View");

                PersonsDetailViewController controller = new PersonsDetailViewController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 600, 500);

                stage.setScene(scene);

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }

        });


        delete.setOnAction((ActionEvent event)-> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            Long personsId = personView.getId();
            personRepository.removeBook(personsId);


        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(edit);
        menu.getItems().addAll(detailedView);
        menu.getItems().add(delete);
        systemPersonsTableView.setContextMenu(menu);
    }

    private ObservableList<PersonBasicView> initializePersonsData() {
        List<PersonBasicView> persons = personService.getPersonsBasicView();
        return FXCollections.observableArrayList(persons);
    }

    private void loadIcons() {
        Image vutLogoImage = new Image(App.class.getResourceAsStream("logos/vut-logo-eng.png"));
        ImageView vutLogo = new ImageView(vutLogoImage);
        vutLogo.setFitWidth(150);
        vutLogo.setFitHeight(50);
    }

    public void handleExitMenuItem(ActionEvent event) {
        System.exit(0);
    }

    public void handleAddPersonButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/PersonCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("BDS JavaFX Create Person");
            stage.setScene(scene);

//            Stage stageOld = (Stage) signInButton.getScene().getWindow();
//            stageOld.close();
//
//            stage.getIcons().add(new Image(App.class.getResourceAsStream("logos/vut.jpg")));
//            authConfirmDialog();

            stage.show();
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        ObservableList<PersonBasicView> observablePersonsList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonsList);
        systemPersonsTableView.refresh();
        systemPersonsTableView.sort();
    }

    public void handleFilterButton(ActionEvent actionEvent){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/BookFilter.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("filter");
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }

    }
}
