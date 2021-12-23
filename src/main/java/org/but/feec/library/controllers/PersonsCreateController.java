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
import javafx.util.Duration;
import org.but.feec.library.api.PersonCreateView;
import org.but.feec.library.data.PersonRepository;
import org.but.feec.library.services.PersonService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.Optional;

import static java.sql.Date.valueOf;

public class PersonsCreateController {

    private static final Logger logger = LoggerFactory.getLogger(PersonsCreateController.class);

    @FXML
    public Button newPersonCreatePerson;

    @FXML
    private TextField newPersonIsbn;

    @FXML
    private TextField newPersonBookTitle;
    @FXML
    private TextField newPersonPublishingHouseId;

//    @FXML
//    private TextField newBookAuthorName;
//
//    @FXML
//    private TextField newBookAuthorSurname;
//
    @FXML
    private TextField newPersonDatePublished;


    private PersonService personService;
    private PersonRepository personRepository;
    private ValidationSupport validation;

    @FXML
    public void initialize() {
        personRepository = new PersonRepository();
        personService = new PersonService(personRepository);

        validation = new ValidationSupport();
        validation.registerValidator(newPersonIsbn, Validator.createEmptyValidator("The email must not be empty."));
        validation.registerValidator(newPersonBookTitle, Validator.createEmptyValidator("The first name must not be empty."));
        validation.registerValidator(newPersonPublishingHouseId, Validator.createEmptyValidator("The ID must not be empty."));
//        validation.registerValidator(newBookAuthorName, Validator.createEmptyValidator("The last name must not be empty."));
//        validation.registerValidator(newBookAuthorSurname, Validator.createEmptyValidator("The nickname must not be empty."));
        validation.registerValidator(newPersonDatePublished, Validator.createEmptyValidator("The published date must not be empty."));

        newPersonCreatePerson.disableProperty().bind(validation.invalidProperty());

        logger.info("BookCreateController initialized");
    }

    @FXML
    void handleCreateNewPerson(ActionEvent event) {
        // can be written easier, its just for better explanation here on so many lines
        Long isbn = Long.valueOf(newPersonIsbn.getText());
        String bookTitle = newPersonBookTitle.getText();
        Long publishingHouseId = Long.valueOf(newPersonPublishingHouseId.getText());
        System.out.println(valueOf(newPersonDatePublished.getText()));
        Date datePublished = valueOf(newPersonDatePublished.getText());


        PersonCreateView personCreateView = new PersonCreateView();
        personCreateView.setIsbn(isbn);
        personCreateView.setBookTitle(bookTitle);
        personCreateView.setPublishingHouseId(publishingHouseId);
//        personCreateView.setAuthorName(authorName);
//        personCreateView.setAuthorSurname(authorSurname);
       personCreateView.setDatePublished(datePublished);


        personService.createPerson(personCreateView);

        bookCreatedConfirmationDialog();
    }

    private void bookCreatedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Book Created Confirmation");
        alert.setHeaderText("Your book was successfully created.");

        Timeline idlestage = new Timeline(new KeyFrame(Duration.seconds(3), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                alert.setResult(ButtonType.CANCEL);
                alert.hide();
            }
        }));
        idlestage.setCycleCount(1);
        idlestage.play();
        Optional<ButtonType> result = alert.showAndWait();
    }

}