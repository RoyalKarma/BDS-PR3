package org.but.feec.library.api;

public class PersonCreateView {
    private Long isbn;
    private String bookTitle;
//    private String authorName;
//    private String authorSurname;
//    private String datePublished;
   private Long publishingHouseId;

    public Long getIsbn() {return isbn; }

    public void setIsbn(Long isbn) {this.isbn = isbn; }


    public String getBookTitle() {return bookTitle;}

    public void setBookTitle(String bookTitle) {this.bookTitle = bookTitle;}


//    public String getAuthorName() {return authorName;}
//
//    public void setAuthorName(String authorName) {
//        this.authorName = authorName;
//    }
//
//
//    public String getAuthorSurname() {
//        return authorSurname;
//    }
//
//    public void setAuthorSurname(String authorSurname) {this.authorSurname = authorSurname;}
//
//
//    public String getDatePublished() {
//        return datePublished;
//    }
//
//    public void setDatePublished(String datePublished) {
//        this.datePublished    = datePublished;
//    }

    public Long getPublishingHouseId(){return publishingHouseId;}
    public void setPublishingHouseId(Long publishingHouseId) {this.publishingHouseId = publishingHouseId;}


    @Override
    public String toString() {
        return "PersonCreateView{" +
                "isbn='" + isbn + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", publishingHouseID='" + publishingHouseId + '\'' +
                '}';
    }
}