package nl.feedme.api.exceptionHandler.ConsumptionExceptions;

import nl.feedme.api.ViewModel.FoodViewModel;
import nl.feedme.api.models.Food;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class PostFoodConsumptionException extends RuntimeException {
    public PostFoodConsumptionException(FoodViewModel food){
        super(food.getName() + " Is missing vital information or has conflicting information");
    }
}
