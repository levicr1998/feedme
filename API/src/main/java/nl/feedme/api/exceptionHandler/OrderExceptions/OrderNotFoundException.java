package nl.feedme.api.exceptionHandler.OrderExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OrderNotFoundException extends RuntimeException{
    public OrderNotFoundException(long id){
        super("Order with id " + id + " does not exist");
    }
}

