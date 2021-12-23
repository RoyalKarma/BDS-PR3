package org.but.feec.library.data;


import org.but.feec.library.api.PersonAuthView;
import org.but.feec.library.api.PersonBasicView;
import org.but.feec.library.api.PersonDetailView;
import org.but.feec.library.config.DataSourceConfig;
import org.but.feec.library.exceptions.DataAccessException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonRepository {

    public PersonAuthView findPersonByEmail(String email) {
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
            throw new DataAccessException("Find person by ID with addresses failed.", e);
        }

        return null;
    }

    public PersonDetailView findPersonDetailedView(Long id) {
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
            throw new DataAccessException("Find person by ID with addresses failed.", e);
        }
        return null;
    }

    /**
     * What will happen if we do not use LEFT JOIN? What persons will be returned? Ask your self and repeat JOIN from the presentations
     *
     * @return list of persons
     */
    public List<PersonBasicView> getPersonsBasicView() {
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
            List<PersonBasicView> personBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                personBasicViews.add(mapToPersonBasicView(resultSet));
            }
            return personBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Persons basic view could not be loaded.", e);
        }
    }

//    public void createPerson(PersonCreateView personCreateView) {
//        String insertPersonSQL = "INSERT INTO library.book (isbn, book_name, date_published pwd, surname) VALUES (?,?,?,?,?)";
//        try (Connection connection = DataSourceConfig.getConnection();
//             // would be beneficial if I will return the created entity back
//             PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
//            // set prepared statement variables
//            preparedStatement.setString(1, personCreateView.getEmail());
//            preparedStatement.setString(2, personCreateView.getFirstName());
//            preparedStatement.setString(3, personCreateView.getNickname());
//            preparedStatement.setString(4, String.valueOf(personCreateView.getPwd()));
//            preparedStatement.setString(5, personCreateView.getSurname());
//
//            int affectedRows = preparedStatement.executeUpdate();
//
//            if (affectedRows == 0) {
//                throw new DataAccessException("Creating person failed, no rows affected.");
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException("Creating person failed operation on the database failed.");
//        }
//    }
//
//    public void editPerson(PersonEditView personEditView) {
//        String insertPersonSQL = "UPDATE bds.person p SET email = ?, first_name = ?, nickname = ?, surname = ? WHERE p.id_person = ?";
//        String checkIfExists = "SELECT email FROM bds.person p WHERE p.id_person = ?";
//        try (Connection connection = DataSourceConfig.getConnection();
//             // would be beneficial if I will return the created entity back
//             PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
//            // set prepared statement variables
//            preparedStatement.setString(1, personEditView.getEmail());
//            preparedStatement.setString(2, personEditView.getFirstName());
//            preparedStatement.setString(3, personEditView.getNickname());
//            preparedStatement.setString(4, personEditView.getSurname());
//            preparedStatement.setLong(5, personEditView.getId());
//
//            try {
//                // TODO set connection autocommit to false
//                /* HERE */
//                try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
//                    ps.setLong(1, personEditView.getId());
//                    ps.execute();
//                } catch (SQLException e) {
//                    throw new DataAccessException("This person for edit do not exists.");
//                }
//
//                int affectedRows = preparedStatement.executeUpdate();

//                if (affectedRows == 0) {
//                    throw new DataAccessException("Creating person failed, no rows affected.");
//                }
//                // TODO commit the transaction (both queries were performed)
//                /* HERE */
//            } catch (SQLException e) {
//                // TODO rollback the transaction if something wrong occurs
//                /* HERE */
//            } finally {
//                // TODO set connection autocommit back to true
//                /* HERE */
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException("Creating person failed operation on the database failed.");
//        }
//    }


    /**
     * <p>
     * Note: In practice reflection or other mapping frameworks can be used (e.g., MapStruct)
     * </p>
     */
    private PersonAuthView mapToPersonAuth(ResultSet rs) throws SQLException {
        PersonAuthView person = new PersonAuthView();
        person.setEmail(rs.getString("email"));
        person.setPassword(rs.getString("pwd"));
        return person;
    }


    private PersonBasicView mapToPersonBasicView(ResultSet rs) throws SQLException {
        PersonBasicView personBasicView = new PersonBasicView();
        personBasicView.setId(rs.getLong("book_id"));
        personBasicView.setIsbn(rs.getLong("isbn"));
        personBasicView.setBookTitle(rs.getString("book_title"));
        personBasicView.setAuthorName(rs.getString("author_name"));
        personBasicView.setAuthorSurname(rs.getString("author_surname"));
        personBasicView.setDatePublished(rs.getString("date_published"));
        return personBasicView;
    }

    private PersonDetailView mapToPersonDetailView(ResultSet rs) throws SQLException {
        PersonDetailView personDetailView = new PersonDetailView();
        personDetailView.setId(rs.getLong("book_id"));
        personDetailView.setIsbn(rs.getLong("isbn"));
        personDetailView.setBookTitle(rs.getString("book_title"));
        personDetailView.setAuthorName(rs.getString("author_name"));
        personDetailView.setAuthorSurname(rs.getString("author_surname"));
        personDetailView.setPublishingHouse(rs.getString("publishing_house_name"));
        personDetailView.setCategoryName(rs.getString("category_name"));
        personDetailView.setDatePublished(rs.getString("date_published"));
        System.out.println(personDetailView);
        return personDetailView;
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
