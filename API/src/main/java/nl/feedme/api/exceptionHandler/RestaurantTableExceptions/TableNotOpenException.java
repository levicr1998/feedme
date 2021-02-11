package nl.feedme.api.exceptionHandler.RestaurantTableExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TableNotOpenException extends Exception {
    public TableNotOpenException() {
        super("Table not opened!");
    }
}
