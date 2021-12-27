package org.but.feec.library.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.but.feec.library.api.LibraryDetailView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LibraryDetailViewController {
    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);

    @FXML
    private TextField idTextField;

    @FXML
    private TextField isbnTextField;

    @FXML
    private TextField bookTitleTextField;

    @FXML
    private TextField authorNameTextField;

    @FXML
    private TextField authorSurnameTextField;

    @FXML
    private TextField publishingHouseTextField;

    @FXML
    private TextField categoryNameTextField;
    @FXML
    private TextField datePublishedTextField;

    public Stage stage;

    public LibraryDetailViewController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        idTextField.setEditable(false);
        isbnTextField.setEditable(false);
        bookTitleTextField.setEditable(false);
        authorNameTextField.setEditable(false);
        authorSurnameTextField.setEditable(false);
        publishingHouseTextField.setEditable(false);
        categoryNameTextField.setEditable(false);
        datePublishedTextField.setEditable(false);

        loadPersonsData();

        logger.info("BookDetailViewController initialized");
    }




    private void loadPersonsData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof LibraryDetailView) {
            LibraryDetailView personBasicView = (LibraryDetailView) stage.getUserData();
            idTextField.setText(String.valueOf(personBasicView.getId()));
            isbnTextField.setText(String.valueOf(personBasicView.getIsbn()));
            bookTitleTextField.setText(String.valueOf((personBasicView.getBookTitle())));
            authorNameTextField.setText(personBasicView.getAuthorName());
            authorSurnameTextField.setText(personBasicView.getAuthorSurname());
            categoryNameTextField.setText(personBasicView.getCategoryName());
            publishingHouseTextField.setText(personBasicView.getPublishingHouse());
            datePublishedTextField.setText(personBasicView.getDatePublished());

        }
    }


   


}
