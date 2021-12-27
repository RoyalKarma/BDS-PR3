package org.but.feec.library.services;

import org.but.feec.library.api.*;
import org.but.feec.library.data.LibraryRepository;

import at.favre.lib.crypto.bcrypt.BCrypt;

import java.util.List;

public class LibraryService {
    private LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }



    public LibraryDetailView getPersonDetailView(Long id) {
        return libraryRepository.findPersonDetailedView(id);
    }

    public List<LibraryBasicView> getPersonsBasicView() {
        return libraryRepository.getPersonsBasicView();
    }
    public List<LibraryFilterView> getBookFilterView(String text){
        return libraryRepository.getBookFilterView(text);
    }
    public List<InjectionView> getInjectionView(String input){
        return libraryRepository.getInjectionView(input);
    }
    public void createPerson(LibraryCreateView libraryCreateView) {
        // the following three lines can be written in one code line (only for more clear explanation it is written in three lines
//        char[] originalPassword = personCreateView.getPwd();
//        char[] hashedPassword = hashPassword(originalPassword);
//        personCreateView.setPwd(hashedPassword);

        libraryRepository.createPerson(libraryCreateView);

    }
    public void editPerson(LibraryEditView libraryEditView) {
        libraryRepository.editPerson(libraryEditView);
    }

//    public void editPerson(PersonEditView personEditView) {
//        personRepository.editPerson(personEditView);
//    }

    /**
     * <p>
     * Note: For implementation details see: https://github.com/patrickfav/bcrypt
     * </p>
     *
     * @param password to be hashed
     * @return hashed password
     */
    private char[] hashPassword(char[] password) {
        return BCrypt.withDefaults().hashToChar(12, password);
    }



}
