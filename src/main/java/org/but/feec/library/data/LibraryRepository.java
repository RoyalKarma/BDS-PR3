package org.but.feec.library.data;


import org.but.feec.library.api.*;
import org.but.feec.library.config.DataSourceConfig;
import org.but.feec.library.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibraryRepository {



    public LibraryAuthView findPersonByEmail(String email) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT email, pwd" +
                             " FROM library.member m" +
                             " LEFT JOIN library.member_contacts c on m.member_id = c.member_contacts_id " +
                             " WHERE c.email = ?")



        ) {
            preparedStatement.setString(1, email);
            System.out.println(preparedStatement);//??
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToPersonAuth(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find person by EMAIL with addresses failed.", e);
        }

        return null;
    }

    public LibraryDetailView findPersonDetailedView(Long id) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT b.book_id, isbn,book_title, author_name, author_surname, date_published, category_name, publishing_house_name" +
                             " FROM library.book b" +
                             " LEFT JOIN library.book_has_author ba on b.book_id=ba.book_book_id" +
                             " LEFT JOIN library.author a on ba.author_author_id=a.author_id" +
                             " LEFT JOIN library.book_has_literature_category bhlc on b.book_id =bhlc.book_book_id" +
                             " LEFT JOIN library.literature_category lc on bhlc.literature_category_literature_category=lc.literature_category_id" +
                             " LEFT JOIN library.publishing_house ph on b.publishing_house_id=ph.publishing_house_id" +
                             " WHERE b.book_id = ?");


//                             " LEFT JOIN bds.address a ON p.id_address = a.id_address" +
//                             " WHERE p.id_person = ?")
        ) {
            preparedStatement.setLong(1, id);
            System.out.println(preparedStatement);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {

                    return mapToPersonDetailView(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find book by ID with addresses failed.", e);
        }
        return null;
    }

    public void removeBook(Long id) {
        String deleteBookSQL =  "DELETE FROM library.book WHERE book_id = ?";
        System.out.println(deleteBookSQL);
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement prpstmt = connection.prepareStatement(deleteBookSQL)){

            prpstmt.setLong(1, id);
            prpstmt.executeUpdate();

        }
        catch (SQLException e) {
           System.out.println("failed");
        }

    }

    /**
     * What will happen if we do not use LEFT JOIN? What persons will be returned? Ask your self and repeat JOIN from the presentations
     *
     * @return list of persons
     */
    /* it wont work */
    public List<LibraryBasicView> getPersonsBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT book_id, isbn, book_title, author_name, author_surname, date_published" +
                             " FROM library.book b"  +
                             " LEFT JOIN library.book_has_author ba on b.book_id=ba.book_book_id " +
                             " LEFT  JOIN library.author a on ba.author_author_id=a.author_id "
                             );


//             "SELECT id_person, email, first_name, surname, nickname, city" +
//                             " FROM bds.person p" +
//                             " LEFT JOIN bds.address a ON p.id_address = a.id_address");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            List<LibraryBasicView> libraryBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                libraryBasicViews.add(mapToPersonBasicView(resultSet));
            }
            return libraryBasicViews;
        }
        catch (SQLException e) {
            throw new DataAccessException("Librarz basic view could not be loaded.", e);
        }
    }
    public List<LibraryFilterView> getBookFilterView(String text){
        System.out.println(text);
        String filter = '%'+text+'%';
        try (Connection connection = DataSourceConfig.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT book_id, isbn, book_title, author_name, author_surname, date_published" +
                             " FROM library.book b"  +
                             " LEFT JOIN library.book_has_author ba on b.book_id=ba.book_book_id " +
                             " LEFT JOIN library.author a on ba.author_author_id=a.author_id " +
                             " where lower(book_title) like lower(?) "
             )


//             "SELECT id_person, email, first_name, surname, nickname, city" +
//                             " FROM bds.person p" +
//                             " LEFT JOIN bds.address a ON p.id_address = a.id_address");
             ) {
            preparedStatement.setString(1,filter);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<LibraryFilterView> libraryFilterViews = new ArrayList<>();
            while (resultSet.next()) {
                libraryFilterViews.add(mapToBookFilterView(resultSet));
            }
            return libraryFilterViews;
        }
        catch (SQLException e) {
            throw new DataAccessException("Library filter view could not be loaded.", e);
        }
    }
    public List<InjectionView> getInjectionView(String input){
        String query = "SELECT id,name,surname, age from injection.user u where u.id ="+input ;
        try (Connection connection = DataSourceConfig.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            List<InjectionView> injectionViews = new ArrayList<>();
            while (resultSet.next()) {
                injectionViews.add(mapToInjectionView(resultSet));
            }
            return injectionViews;
        } catch (SQLException e) {
            throw new DataAccessException("Find all users SQL failed.", e);
        }


    }

    public void createPerson(LibraryCreateView libraryCreateView) {
        //TODO Make this a transaction!!
        String insertPersonSQL = " INSERT INTO library.book (isbn, book_title, publishing_house_id, date_published) VALUES (?,?, ?, ?)\n";
        String insertAuthorSQL = " INSERT INTO library.author(author_name, author_surname, date_birth, date_death) VALUES (?, ?, null, null)\n";
        String insertConnectionSQL = "Insert into library.book_has_author(book_book_id, author_author_id) values ((SELECT book_id FROM library.book WHERE isbn=?)," +
                "(SELECT author_id FROM library.author WHERE author_name =? and author_surname = ?));\n";
        //book
        try (Connection connection = DataSourceConfig.getConnection();

             PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
            System.out.println(preparedStatement);
            // set prepared statement variables
            preparedStatement.setLong(1, libraryCreateView.getIsbn());
            preparedStatement.setString(2, libraryCreateView.getBookTitle());
            preparedStatement.setLong(3, libraryCreateView.getPublishingHouseId());
            preparedStatement.setDate(4, libraryCreateView.getDatePublished());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating book failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Creating book failed operation on the database failed.");
        }
        //author
        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(insertAuthorSQL, Statement.RETURN_GENERATED_KEYS)) {
            System.out.println(preparedStatement);
            // set prepared statement variables
            preparedStatement.setString(1, libraryCreateView.getAuthorName());
            preparedStatement.setString(2, libraryCreateView.getAuthorSurname());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating book failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Creating book failed operation on the database failed.");
        }
        //relation betweek book and author
        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(insertConnectionSQL, Statement.RETURN_GENERATED_KEYS)) {
            System.out.println(preparedStatement);
            // set prepared statement variables
            preparedStatement.setLong(1,   libraryCreateView.getIsbn());
            preparedStatement.setString(2, libraryCreateView.getAuthorName());
            preparedStatement.setString(3, libraryCreateView.getAuthorSurname());
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new DataAccessException("Creating book failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Creating book failed operation on the database failed.");
        }
        //im sorry

    }



    public void editPerson(LibraryEditView libraryEditView) {
        String insertPersonSQL =
                "begin;" +
                " UPDATE library.book b SET isbn = ?, book_title = ? WHERE b.book_id = ?;" +
                " UPDATE library.author a SET author_name = ?, author_surname = ? WHERE a.author_id =(SELECT b.book_id from library.author a" +
                " LEFT JOIN library.book_has_author bhs ON a.author_id =bhs.author_author_id" +
                " LEFT JOIN library.book b on bhs.book_book_id = b.book_id" +
                " WHERE b.book_id=?);"+
                        "commit";


        String checkIfExists = "SELECT isbn FROM library.book b WHERE b.book_id = ?";

        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
            // set prepared statement variables
            preparedStatement.setLong(1, libraryEditView.getIsbn());
            preparedStatement.setString(2, libraryEditView.getBookTitle());
            preparedStatement.setLong(3, libraryEditView.getId());
            preparedStatement.setString(4, libraryEditView.getAuthorName());
            preparedStatement.setString(5, libraryEditView.getAuthorSurname());
            preparedStatement.setLong(6, libraryEditView.getId());
            System.out.println((preparedStatement));
            try {

                connection.setAutoCommit(false);
                try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, libraryEditView.getId());

                    ps.execute();
                } catch (SQLException e) {
                    throw new DataAccessException("This book for edit do not exists.");
                }

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Creating book failed, no rows affected.");
                }

                System.out.println(connection);
                connection.commit();
            } catch (SQLException e) {

                connection.rollback();
            } finally {

                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Creating book failed operation on the database failed.");
        }
    }


    /**
     * <p>
     * Note: In practice reflection or other mapping frameworks can be used (e.g., MapStruct)
     * </p>
     */
    private LibraryAuthView mapToPersonAuth(ResultSet rs) throws SQLException {
        LibraryAuthView person = new LibraryAuthView();
        person.setEmail(rs.getString("email"));
        person.setPassword(rs.getString("pwd"));
        return person;
    }


    private LibraryBasicView mapToPersonBasicView(ResultSet rs) throws SQLException {
        LibraryBasicView libraryBasicView = new LibraryBasicView();
        libraryBasicView.setId(rs.getLong("book_id"));
        libraryBasicView.setIsbn(rs.getLong("isbn"));
        libraryBasicView.setBookTitle(rs.getString("book_title"));
        libraryBasicView.setAuthorName(rs.getString("author_name"));
        libraryBasicView.setAuthorSurname(rs.getString("author_surname"));
        libraryBasicView.setDatePublished(rs.getString("date_published"));
        return libraryBasicView;
    }

    private LibraryDetailView mapToPersonDetailView(ResultSet rs) throws SQLException {
        LibraryDetailView libraryDetailView = new LibraryDetailView();
        libraryDetailView.setId(rs.getLong("book_id"));
        libraryDetailView.setIsbn(rs.getLong("isbn"));
        libraryDetailView.setBookTitle(rs.getString("book_title"));
        libraryDetailView.setAuthorName(rs.getString("author_name"));
        libraryDetailView.setAuthorSurname(rs.getString("author_surname"));
        libraryDetailView.setPublishingHouse(rs.getString("publishing_house_name"));
        libraryDetailView.setCategoryName(rs.getString("category_name"));
        libraryDetailView.setDatePublished(rs.getString("date_published"));
        System.out.println(libraryDetailView);
        return libraryDetailView;
    }

   private LibraryFilterView mapToBookFilterView(ResultSet rs) throws SQLException{
        LibraryFilterView libraryFilterView = new LibraryFilterView();
       libraryFilterView.setId(rs.getLong("book_id"));
       libraryFilterView.setIsbn(rs.getLong("isbn"));
       libraryFilterView.setBookTitle(rs.getString("book_title"));
       libraryFilterView.setAuthorName(rs.getString("author_name"));
       libraryFilterView.setAuthorSurname(rs.getString("author_surname"));
       return libraryFilterView;
   }
   private InjectionView mapToInjectionView(ResultSet rs ) throws  SQLException{
        InjectionView injectionView = new InjectionView();
        injectionView.setId(rs.getLong("id"));
        injectionView.setName(rs.getString("name"));
        injectionView.setSurname(rs.getString("surname"));
        injectionView.setAge(rs.getLong("age"));
        return injectionView;
   }


//    "SELECT b.book_id, isbn,book_title, author_name, author_surname, date_published, category_name, publishing_house_name" +
//            " FROM library.book b" +
//            " LEFT JOIN library.book_has_author ba on b.book_id=ba.book_book_id" +
//            " LEFT JOIN library.author a on ba.author_author_id=a.author_id" +
//            " LEFT JOIN library.book_has_literature_category bhlc on b.book_id =bhlc.book_book_id" +
//            " LEFT JOIN library.literature_category lc on bhlc.literature_category_literature_category=lc.literature_category_id" +
//            " LEFT JOIN library.publishing_house ph on b.publishing_house_id=ph.publishing_house_id" +
//            " WHERE b.book_id = ?");
}
