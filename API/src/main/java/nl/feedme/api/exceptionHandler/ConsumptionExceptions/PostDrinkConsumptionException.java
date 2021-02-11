package nl.feedme.api.exceptionHandler.ConsumptionExceptions;

import nl.feedme.api.ViewModel.DrinkViewModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class PostDrinkConsumptionException extends RuntimeException {
    public PostDrinkConsumptionException(DrinkViewModel drinkViewModel){
        super( drinkViewModel.getName() +" Is missing vital information or has conflicting information");
    }
}
