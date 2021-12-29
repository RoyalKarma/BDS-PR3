package org.but.feec.library.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.but.feec.library.api.InjectionView;
import org.but.feec.library.data.LibraryRepository;
import org.but.feec.library.services.LibraryService;

import java.util.List;

public class InjectionController {
    @FXML
    private TableColumn<InjectionView, Long> id;
    @FXML
    private TableColumn<InjectionView, Long> age;
    @FXML
    private TableColumn<InjectionView, String> name;
    @FXML
    private TableColumn<InjectionView, String> surname;

    @FXML
    private TableView<InjectionView> systemPersonsTableView;
    @FXML
    private TextField inputField;

    private LibraryService libraryService;
    private LibraryRepository libraryRepository;

    public Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        libraryRepository = new LibraryRepository();
        libraryService = new LibraryService(libraryRepository);
//        GlyphsDude.setIcon(exitMenuItem, FontAwesomeIcon.CLOSE, "1em");

        id.setCellValueFactory(new PropertyValueFactory<InjectionView, Long>("id"));
        name.setCellValueFactory(new PropertyValueFactory<InjectionView, String>("name"));
        surname.setCellValueFactory(new PropertyValueFactory<InjectionView, String>("surname"));
        age.setCellValueFactory(new PropertyValueFactory<InjectionView, Long>("age"));



    }
    private ObservableList<InjectionView> initializePersonsData() {

        String input = inputField.getText();
        List<InjectionView> persons = libraryService.getInjectionView(input);
        return FXCollections.observableArrayList(persons);
    }
    public void handleConfirmButton(ActionEvent actionEvent){
        ObservableList<InjectionView> observablePersonList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonList);

    }


}