package nl.feedme.api.exceptionHandler.GuestExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class GuestNotFoundException extends RuntimeException {
    public GuestNotFoundException(long id) {
        super("Guest with id " + id + " not found");
    }
}
