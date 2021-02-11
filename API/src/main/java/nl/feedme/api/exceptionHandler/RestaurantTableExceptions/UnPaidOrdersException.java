package nl.feedme.api.exceptionHandler.RestaurantTableExceptions;

import nl.feedme.api.models.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class UnPaidOrdersException extends RuntimeException {
    public UnPaidOrdersException(Order order) {
        super(order.getId() + " is not paid");
    }
}
