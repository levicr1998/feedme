package nl.feedme.api.exceptionHandler.ChefExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ChefConflictException extends RuntimeException {
    public ChefConflictException(String username) {
        super("Username " + username + " already in use");
    }
}