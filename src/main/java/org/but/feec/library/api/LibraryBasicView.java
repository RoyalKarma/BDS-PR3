package org.but.feec.library.api;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LibraryBasicView {
    private final LongProperty id = new SimpleLongProperty();
    private final LongProperty isbn = new SimpleLongProperty();
    private final StringProperty bookTitle = new SimpleStringProperty();
    private final StringProperty authorName = new SimpleStringProperty();
    private final StringProperty authorSurname = new SimpleStringProperty();
    private final StringProperty datePublished = new SimpleStringProperty();



    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().setValue(id);
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

    public String getAuthorSurname() {
        return authorSurnameProperty().get();
    }

    public void setAuthorSurname(String authorSurname) {
        this.authorSurnameProperty().setValue(authorSurname);
    }

    public String getDatePublished() { return datePublishedProperty().get();}

    public void setDatePublished(String datePublished) {this.datePublishedProperty().setValue(datePublished);}

    public LongProperty idProperty() {
        return id;
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

    public StringProperty authorSurnameProperty() {
        return authorSurname;
    }
    public StringProperty datePublishedProperty(){
        return datePublished;
    }


}
