package org.but.feec.library.api;

public class PersonEditView {




        private Long id;
        private Long isbn;
        private String bookTitle;
//        private String nickname;
//        private String familyName;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public Long getIsbn() {
            return isbn;
        }

        public void setIsbn(Long isbn) {
            this.isbn = isbn;
        }

        public String getBookTitle() {
            return bookTitle;
        }

        public void setBookTitle(String bookTitle) {
            this.bookTitle = bookTitle;
        }
//
//        public String getNickname() {
//            return nickname;
//        }
//
//        public void setNickname(String nickname) {
//            this.nickname = nickname;
//        }
//
//        public String getFamilyName() {
//            return familyName;
//        }
//
//        public void setFamilyName(String familyName) {
//            this.familyName = familyName;
//        }

        @Override
        public String toString() {
            return "PersonEditView{" +
                    "isbn='" + isbn + '\'' +
                    ", bookTitle='" + bookTitle + '\'' +
//                    ", nickname='" +  + '\'' +
//                    ", familyName='" + familyName + '\'' +
                    '}';
        }

}
