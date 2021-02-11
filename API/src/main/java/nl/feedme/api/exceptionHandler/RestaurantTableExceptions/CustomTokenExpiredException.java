package nl.feedme.api.exceptionHandler.RestaurantTableExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class CustomTokenExpiredException extends RuntimeException {
    public CustomTokenExpiredException() {
        super("This token is not valid anymore");
    }
}
