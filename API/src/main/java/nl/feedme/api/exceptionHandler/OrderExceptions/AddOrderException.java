package nl.feedme.api.exceptionHandler.OrderExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class AddOrderException extends RuntimeException{
    public AddOrderException(){
        super("Order is missing vital information or has conflicting information");
    }
}