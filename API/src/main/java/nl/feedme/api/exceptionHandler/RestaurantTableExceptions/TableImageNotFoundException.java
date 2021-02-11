package nl.feedme.api.exceptionHandler.RestaurantTableExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TableImageNotFoundException extends RuntimeException {
    public TableImageNotFoundException(String message) {
        super(message);
    }
}
