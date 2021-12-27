package org.but.feec.library.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.but.feec.library.api.LibraryFilterView;
import org.but.feec.library.data.LibraryRepository;
import org.but.feec.library.services.LibraryService;

import java.util.List;

public class LibraryFilterController {
    @FXML
    public TableColumn<LibraryFilterView, Long> personsId;
    @FXML
    public TableColumn<LibraryFilterView, Long> isbn;
    @FXML
    public TableColumn<LibraryFilterView, String> bookTitle;
    @FXML
    public TableColumn<LibraryFilterView, String> authorName;
    @FXML
    public TableColumn<LibraryFilterView, String> authorSurname;
    @FXML
    public TableView<LibraryFilterView> systemPersonsTableView;

    public Stage stage;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private LibraryService libraryService;
    private LibraryRepository libraryRepository;
    @FXML
    private void initialize(){
        libraryRepository = new LibraryRepository();
        libraryService = new LibraryService(libraryRepository);

        personsId.setCellValueFactory(new PropertyValueFactory<LibraryFilterView, Long>("id"));
        bookTitle.setCellValueFactory(new PropertyValueFactory<LibraryFilterView, String>("bookTitle"));
        isbn.setCellValueFactory(new PropertyValueFactory<LibraryFilterView, Long>("isbn"));
        authorName.setCellValueFactory(new PropertyValueFactory<LibraryFilterView, String>("authorName"));
        authorSurname.setCellValueFactory(new PropertyValueFactory<LibraryFilterView, String>("authorSurname"));


        ObservableList<LibraryFilterView> observablePersonList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonList);

//        LoadPersonData();
    }
    private ObservableList<LibraryFilterView> initializePersonsData() {

        String text = (String) stage.getUserData();
        System.out.println("controller "+text);
        List<LibraryFilterView> persons = libraryService.getBookFilterView(text);
        return FXCollections.observableArrayList(persons);
    }
//    private void LoadPersonData(){
//        Stage stage =this.stage;
//        if (stage.getUserData() instanceof BookFilterView) {
//            PersonDetailView personBasicView = (PersonDetailView) stage.getUserData();
//            idTextField.setText(String.valueOf(personBasicView.getId()));
//            isbnTextField.setText(String.valueOf(personBasicView.getIsbn()));
//            bookTitleTextField.setText(String.valueOf((personBasicView.getBookTitle())));
//            authorNameTextField.setText(personBasicView.getAuthorName());
//            authorSurnameTextField.setText(personBasicView.getAuthorSurname());
//            categoryNameTextField.setText(personBasicView.getCategoryName());
//            publishingHouseTextField.setText(personBasicView.getPublishingHouse());
//            datePublishedTextField.setText(personBasicView.getDatePublished());
//
//        }

//    }

}
