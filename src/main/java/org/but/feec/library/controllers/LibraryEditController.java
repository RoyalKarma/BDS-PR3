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
import org.but.feec.library.api.LibraryBasicView;
import org.but.feec.library.api.LibraryEditView;
import org.but.feec.library.data.LibraryRepository;
import org.but.feec.library.services.LibraryService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class LibraryEditController {
    private static final Logger logger = LoggerFactory.getLogger(LibraryEditController.class);

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


    private LibraryService libraryService;
    private LibraryRepository libraryRepository;
    private ValidationSupport validation;

    // used to reference the stage and to get passed data through it
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        libraryRepository = new LibraryRepository();
        libraryService = new LibraryService(libraryRepository);

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
        if (stage.getUserData() instanceof LibraryBasicView) {
            LibraryBasicView libraryBasicView = (LibraryBasicView) stage.getUserData();
            idTextField.setText(String.valueOf(libraryBasicView.getId()));
            isbnTextField.setText(String.valueOf(libraryBasicView.getIsbn()));
            bookTitleTextField.setText(libraryBasicView.getBookTitle());
            authorNameTextField.setText(libraryBasicView.getAuthorName());
            authorSurnameTextField.setText(libraryBasicView.getAuthorSurname());
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

        LibraryEditView libraryEditView = new LibraryEditView();
        libraryEditView.setId(id);
        libraryEditView.setIsbn(isbn);
        libraryEditView.setBookTitle(bookTitle);
        libraryEditView.setAuthorName(authorName);
        libraryEditView.setAuthorSurname(authorSurname);

       libraryService.editPerson(libraryEditView);

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
