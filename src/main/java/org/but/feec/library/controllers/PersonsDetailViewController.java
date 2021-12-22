package org.but.feec.library.controllers;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.but.feec.library.api.PersonBasicView;
import org.but.feec.library.api.PersonDetailView;
import org.but.feec.library.data.PersonRepository;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.but.feec.library.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonsDetailViewController {
    private static final Logger logger = LoggerFactory.getLogger(PersonsController.class);
    @FXML
    private TableColumn<PersonDetailView, Long> personsId;
    @FXML
    private TableColumn<PersonDetailView, Long> isbn;
    @FXML
    private TableView<PersonDetailView> systemDetailedTableView;

    private PersonService personService;
    private PersonRepository personRepository;

    public PersonsDetailViewController() {
    }

    private void loadPersonData(){
       personsId.setCellValueFactory(new PropertyValueFactory<PersonDetailView, Long>("id"));
       isbn.setCellValueFactory(new PropertyValueFactory<PersonDetailView, Long>("isbn"));
    }

    public void setStage(Stage stage) {

    }

   /*public void handleDetailView(ActionEvent actionEvent) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/PersonsDetailView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("DetailedView");
            stage.setScene(scene);


            stage.show();
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }*/
}
