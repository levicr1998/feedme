package nl.feedme.api.exceptionHandler.RestaurantTableExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TableNotAvailebleException extends RuntimeException {
    public TableNotAvailebleException() {
        super("We couldn't find this table");
    }
}
