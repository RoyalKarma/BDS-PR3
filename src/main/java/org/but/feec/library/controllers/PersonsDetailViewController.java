package org.but.feec.library.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.but.feec.library.api.PersonDetailView;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


public class PersonsDetailViewController {
    private static final Logger logger = LoggerFactory.getLogger(PersonsController.class);

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

    public PersonsDetailViewController() {
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
        if (stage.getUserData() instanceof PersonDetailView) {
            PersonDetailView personBasicView = (PersonDetailView) stage.getUserData();
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
