package org.but.feec.library.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.but.feec.library.App;
import org.but.feec.library.api.LibraryBasicView;
import org.but.feec.library.api.LibraryDetailView;
import org.but.feec.library.data.LibraryRepository;
import org.but.feec.library.exceptions.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.but.feec.library.services.LibraryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

public class LibraryController {

    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

    @FXML
    public Button addPersonButton;
    @FXML
    public Button refreshButton;
    @FXML
    public Button filterButton;
    @FXML
    public TextField searchBar;
    @FXML
    private TableColumn<LibraryBasicView, Long> personsId;
    @FXML
    private TableColumn<LibraryBasicView, Long> isbn;
    @FXML
    private TableColumn<LibraryBasicView, String> authorName;
    @FXML
    private TableColumn<LibraryBasicView, String> bookTitle;
    @FXML
    private TableColumn<LibraryBasicView, String> authorSurname;
    @FXML
    private TableColumn<LibraryBasicView, String> datePublished;
    @FXML
    private TableView<LibraryBasicView> systemPersonsTableView;

//    @FXML
//    public MenuItem exitMenuItem;

    private LibraryService libraryService;
    private LibraryRepository libraryRepository;



    @FXML
    private void initialize() {
        libraryRepository = new LibraryRepository();
        libraryService = new LibraryService(libraryRepository);
//        GlyphsDude.setIcon(exitMenuItem, FontAwesomeIcon.CLOSE, "1em");

        personsId.setCellValueFactory(new PropertyValueFactory<LibraryBasicView, Long>("id"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<LibraryBasicView, String>("bookTitle"));
        isbn.setCellValueFactory(new PropertyValueFactory<LibraryBasicView, Long>("isbn"));
        authorName.setCellValueFactory(new PropertyValueFactory<LibraryBasicView, String>("authorName"));
        authorSurname.setCellValueFactory(new PropertyValueFactory<LibraryBasicView, String>("authorSurname"));
        datePublished.setCellValueFactory(new PropertyValueFactory<LibraryBasicView, String>("datePublished"));



        ObservableList<LibraryBasicView> observablePersonList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonList);

        systemPersonsTableView.getSortOrder().add(personsId);

        initializeTableViewSelection();
        //  loadIcons();

        logger.info("PersonsController initialized");
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit book");
        MenuItem detailedView = new MenuItem("Detailed book view");
        MenuItem delete = new MenuItem ("Delete book");
            edit.setOnAction((ActionEvent event) -> {
            LibraryBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/LibraryEdit.fxml"));
                Stage stage = new Stage();
                stage.setUserData(personView);
                stage.setTitle("Library");

                LibraryEditController controller = new LibraryEditController();
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
            LibraryBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/LibraryDetailView.fxml"));
                Stage stage = new Stage();

                Long personId = personView.getId();
                LibraryDetailView libraryDetailView = libraryService.getPersonDetailView(personId);

                stage.setUserData(libraryDetailView);
                stage.setTitle("Library book detailed view");

                LibraryDetailViewController controller = new LibraryDetailViewController();
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
            LibraryBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            Long personsId = personView.getId();
            libraryRepository.removeBook(personsId);


        });

        ContextMenu menu = new ContextMenu();
        menu.getItems().add(edit);
        menu.getItems().addAll(detailedView);
        menu.getItems().add(delete);
        systemPersonsTableView.setContextMenu(menu);
    }

    private ObservableList<LibraryBasicView> initializePersonsData() {
        List<LibraryBasicView> persons = libraryService.getPersonsBasicView();
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
            fxmlLoader.setLocation(App.class.getResource("fxml/LibraryCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("Library create book entry");
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
        ObservableList<LibraryBasicView> observablePersonsList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonsList);
        systemPersonsTableView.refresh();
        systemPersonsTableView.sort();
    }

    public void handleFilterButton(ActionEvent actionEvent){
        try {
            String text = searchBar.getText();
            System.out.println("handler" +text);
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/BookFilter.fxml"));
            Stage stage = new Stage();
            LibraryFilterController libraryFilterController = new LibraryFilterController();
            stage.setUserData(text);
            libraryFilterController.setStage(stage);
            fxmlLoader.setController(libraryFilterController);
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);


            stage.setTitle("Filtered view");
            stage.setScene(scene);
            stage.show();

        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }

    }
    public void handleInjectionButton(ActionEvent actionEvent){
        try{

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/injectionTraining.fxml"));
            Stage stage = new Stage();
            InjectionController injectionController = new InjectionController();

            injectionController.setStage(stage);
            fxmlLoader.setController(injectionController);
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);


            stage.setTitle("SQL Injection training");
            stage.setScene(scene);
            stage.show();
        }catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }
}
