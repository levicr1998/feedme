package nl.feedme.api.exceptionHandler.ConsumptionExceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ConsumptionNotFoundException extends RuntimeException {
    public ConsumptionNotFoundException(long id){
        super("Consumption with id  " + id + " does not exist");
    }
}
