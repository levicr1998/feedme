package nl.feedme.api.exceptionHandler.RestaurantTableExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TableExpiredException extends Exception {
    public TableExpiredException() {
        super("This table is not found");
    }

}
