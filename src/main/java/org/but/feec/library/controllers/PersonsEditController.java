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
import org.but.feec.library.api.PersonBasicView;
import org.but.feec.library.api.PersonEditView;
import org.but.feec.library.data.PersonRepository;
import org.but.feec.library.services.PersonService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class PersonsEditController {
    private static final Logger logger = LoggerFactory.getLogger(PersonsEditController.class);

    @FXML
    public Button editPersonButton;
    @FXML
    public TextField idTextField;
    @FXML
    private TextField isbnTextField;
    @FXML
    private TextField bookTitleTextField;
    @FXML
    private TextField authorNameTextField;
    @FXML
    private TextField authorSurnameTextField;


    private PersonService personService;
    private PersonRepository personRepository;
    private ValidationSupport validation;

    // used to reference the stage and to get passed data through it
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        personRepository = new PersonRepository();
        personService = new PersonService(personRepository);

        validation = new ValidationSupport();
        validation.registerValidator(idTextField, Validator.createEmptyValidator("The id must not be empty."));
        idTextField.setEditable(false);
        validation.registerValidator(isbnTextField, Validator.createEmptyValidator("The isbn must not be empty."));
        validation.registerValidator(bookTitleTextField, Validator.createEmptyValidator("The title must not be empty."));
        validation.registerValidator(authorNameTextField, Validator.createEmptyValidator("The name must not be empty."));
        validation.registerValidator(authorSurnameTextField, Validator.createEmptyValidator("The surname must not be empty."));

        editPersonButton.disableProperty().bind(validation.invalidProperty());

        loadPersonsData();

        logger.info("PersonsEditController initialized");
    }

    /**
     * Load passed data from Persons controller. Check this tutorial explaining how to pass the data between controllers: https://dev.to/devtony101/javafx-3-ways-of-passing-information-between-scenes-1bm8
     */
    private void loadPersonsData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof PersonBasicView) {
            PersonBasicView personBasicView = (PersonBasicView) stage.getUserData();
            idTextField.setText(String.valueOf(personBasicView.getId()));
            isbnTextField.setText(String.valueOf(personBasicView.getIsbn()));
            bookTitleTextField.setText(personBasicView.getBookTitle());
            authorNameTextField.setText(personBasicView.getAuthorName());
            authorSurnameTextField.setText(personBasicView.getAuthorSurname());
        }
    }

    @FXML
    public void handleEditPersonButton(ActionEvent event) {
        // can be written easier, its just for better explanation here on so many lines
        Long id = Long.valueOf(idTextField.getText());
        Long isbn = Long.valueOf(isbnTextField.getText());
        String bookTitle = bookTitleTextField.getText();
        String authorName = authorNameTextField.getText();
        String authorSurname = authorSurnameTextField.getText();

        PersonEditView personEditView = new PersonEditView();
        personEditView.setId(id);
        personEditView.setIsbn(isbn);
        personEditView.setBookTitle(bookTitle);
        personEditView.setAuthorName(authorName);
        personEditView.setAuthorSurname(authorSurname);

       personService.editPerson(personEditView);

        personEditedConfirmationDialog();
    }

    private void personEditedConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Book edit confirmation");
        alert.setHeaderText("Your book was successfully edited.");

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
