package nl.feedme.api.exceptionHandler.RestaurantTableExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_ACCEPTABLE)
public class NickNameAlreadyJoinedTableException extends RuntimeException {
    public NickNameAlreadyJoinedTableException() {
        super("This nickname is already taken on this table.");
    }
}
