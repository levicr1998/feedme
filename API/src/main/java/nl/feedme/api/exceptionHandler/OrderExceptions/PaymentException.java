package nl.feedme.api.exceptionHandler.OrderExceptions;

import com.paypal.base.rest.PayPalRESTException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PaymentException extends RuntimeException {
    public PaymentException(PayPalRESTException e) {
        super(e.getDetails().getDetails().get(0).getIssue());
    }
}
