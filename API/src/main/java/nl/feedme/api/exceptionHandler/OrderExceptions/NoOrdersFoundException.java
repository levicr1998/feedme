package nl.feedme.api.exceptionHandler.OrderExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoOrdersFoundException extends RuntimeException{
    public NoOrdersFoundException(){
        super("No order found");
}
}