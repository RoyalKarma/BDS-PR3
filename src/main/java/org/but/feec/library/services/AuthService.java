package org.but.feec.library.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.but.feec.library.api.LibraryAuthView;
import org.but.feec.library.data.LibraryRepository;
import org.but.feec.library.exceptions.ResourceNotFoundException;

public class AuthService {
    private LibraryRepository libraryRepository;

    public AuthService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    private LibraryAuthView findPersonByEmail(String email) {
        return libraryRepository.findPersonByEmail(email);
    }

    public boolean authenticate(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return false;
        }

        LibraryAuthView libraryAuthView = findPersonByEmail(username);
        if (libraryAuthView == null) {
            throw new ResourceNotFoundException("Provided username is not found.");
        }

        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), libraryAuthView.getPassword());
        return result.verified;
    }
}



