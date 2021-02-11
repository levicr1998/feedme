package nl.feedme.api.exceptionHandler.RestaurantTableExceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TableOrderUserConflictException extends Exception {
    public TableOrderUserConflictException() {
        super("You are not allowed to do this...");
    }
}
