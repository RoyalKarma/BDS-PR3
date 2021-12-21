package org.but.feec.library.api;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PersonBasicView {
    private final LongProperty id = new SimpleLongProperty();
    private final StringProperty city = new SimpleStringProperty();
    private final LongProperty isbn = new SimpleLongProperty();
    private final StringProperty bookTitle = new SimpleStringProperty();
    private final StringProperty authorName = new SimpleStringProperty();



    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().setValue(id);
    }

    public String getCity() {
        return cityProperty().get();
    }

    public void setCity(String city) {
        this.cityProperty().setValue(city);
    }

    public Long getIsbn() {
        return isbnProperty().get();
    }

    public void setIsbn(Long isbn) {
        this.isbnProperty().setValue(isbn);
    }

    public String getBookTitle() {
        return bookTitleProperty().get();
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitleProperty().setValue(bookTitle);
    }

    public String getAuthorName() {
        return authorNameProperty().get();
    }

    public void setAuthorName(String authorName) {
        this.authorNameProperty().setValue(authorName);
    }


    public LongProperty idProperty() {
        return id;
    }

    public StringProperty cityProperty() {
        return city;
    }

    public LongProperty isbnProperty() {
        return isbn;
    }

    public StringProperty bookTitleProperty() {
        return bookTitle;
    }

    public StringProperty authorNameProperty() {
        return authorName;
    }



}
