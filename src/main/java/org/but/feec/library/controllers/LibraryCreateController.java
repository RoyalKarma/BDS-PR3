package org.but.feec.library.controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import org.but.feec.library.api.LibraryCreateView;
import org.but.feec.library.data.LibraryRepository;
import org.but.feec.library.services.LibraryService;
import org.controlsfx.validation.ValidationSupport;
import org.controlsfx.validation.Validator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.util.Optional;

import static java.sql.Date.valueOf;

public class LibraryCreateController {

    private static final Logger logger = LoggerFactory.getLogger(LibraryCreateController.class);

    @FXML
    public Button newPersonCreatePerson;

    @FXML
    private TextField newPersonIsbn;

    @FXML
    private TextField newPersonBookTitle;
    @FXML
    private TextField newPersonPublishingHouseId;

    @FXML
    private TextField newPersonAuthorName;

    @FXML
    private TextField newPersonAuthorSurname;

    @FXML
    private DatePicker newPersonDatePublished;


    private LibraryService libraryService;
    private LibraryRepository libraryRepository;
    private ValidationSupport validation;

    @FXML
    public void initialize() {
        libraryRepository = new LibraryRepository();
        libraryService = new LibraryService(libraryRepository);

        validation = new ValidationSupport();
        validation.registerValidator(newPersonIsbn, Validator.createEmptyValidator("The email must not be empty."));
        validation.registerValidator(newPersonBookTitle, Validator.createEmptyValidator("The book title must not be empty."));
        validation.registerValidator(newPersonPublishingHouseId, Validator.createEmptyValidator("The ID must not be empty."));
        validation.registerValidator(newPersonAuthorName, Validator.createEmptyValidator("The author name must not be empty."));
        validation.registerValidator(newPersonAuthorSurname, Validator.createEmptyValidator("The author surname must not be empty."));
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

        Date datePublished = valueOf(newPersonDatePublished.getValue());
        String authorName = newPersonAuthorName.getText();
        String authorSurname = newPersonAuthorSurname.getText();


        LibraryCreateView libraryCreateView = new LibraryCreateView();
        libraryCreateView.setIsbn(isbn);
        libraryCreateView.setBookTitle(bookTitle);
        libraryCreateView.setPublishingHouseId(publishingHouseId);
        libraryCreateView.setAuthorName(authorName);
        libraryCreateView.setAuthorSurname(authorSurname);
       libraryCreateView.setDatePublished(datePublished);


        libraryService.createPerson(libraryCreateView);

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