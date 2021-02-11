package nl.feedme.api.exceptionHandler.ChefExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ChefNotFoundException extends RuntimeException {

    public ChefNotFoundException(String username) {
        super("Username not found : " + username);
    }
}
