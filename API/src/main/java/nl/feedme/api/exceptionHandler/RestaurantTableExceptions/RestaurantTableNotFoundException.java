package nl.feedme.api.exceptionHandler.RestaurantTableExceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RestaurantTableNotFoundException extends RuntimeException {
    public RestaurantTableNotFoundException(long id){
        super("RestaurantTable with id  " + id + " does not exist");
    }
}
