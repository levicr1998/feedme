package nl.feedme.api.exceptionHandler.ConsumptionExceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NoConsumptionsFoundException extends RuntimeException {
    public NoConsumptionsFoundException(){
        super("No consumption found");
    }
}
