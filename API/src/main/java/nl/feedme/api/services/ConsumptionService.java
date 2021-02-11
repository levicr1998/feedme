package nl.feedme.api.services;

import nl.feedme.api.ViewModel.DrinkViewModel;
import nl.feedme.api.ViewModel.FoodViewModel;
import nl.feedme.api.exceptionHandler.ConsumptionExceptions.ConsumptionNotFoundException;
import nl.feedme.api.exceptionHandler.ConsumptionExceptions.NoConsumptionsFoundException;
import nl.feedme.api.exceptionHandler.ConsumptionExceptions.PostDrinkConsumptionException;
import nl.feedme.api.exceptionHandler.ConsumptionExceptions.PostFoodConsumptionException;
import nl.feedme.api.models.Consumption;
import nl.feedme.api.models.Drink;
import nl.feedme.api.models.Food;
import nl.feedme.api.models.FoodType;
import nl.feedme.api.repositories.ConsumptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service("consumptionService")
public class ConsumptionService {
    @Autowired
    private ConsumptionRepository consumptionRepository;


    public ConsumptionService() {

    }

    //    @Secured("ROLE_ADMIN")
    public Food addFood(FoodViewModel foodViewModel) {
        try {
                Food food = new Food(foodViewModel.getPrice(), foodViewModel.getName(), null, 0, foodViewModel.getFoodType(), foodViewModel.getImage(), foodViewModel.getDescription());return consumptionRepository.save(food);

        } catch (NullPointerException e) {
            throw new PostFoodConsumptionException(foodViewModel);
        }
    }

    public List<Consumption> getAllConsumption() {
        List<Consumption> allConsumptions = (List<Consumption>) consumptionRepository.findAll();

        if (allConsumptions.size() > 0) {
            return allConsumptions;
        }
        throw new NoConsumptionsFoundException();
    }

    public List<Drink> getAllDrinks() {
        List<Drink> allDrinks = ((List<Consumption>) consumptionRepository.findAll()).stream().filter(consumption -> consumption instanceof Drink).map(consumption -> (Drink) consumption).collect(Collectors.toList());

        if (allDrinks.size() > 0) {
            return allDrinks;
        }

        throw new NoConsumptionsFoundException();
    }

    public List<Food> getAllFood() {
        List<Food> allFood = ((List<Consumption>) consumptionRepository.findAll()).stream().filter(consumption -> consumption instanceof Food).map(consumption -> (Food) consumption).collect(Collectors.toList());

        if (allFood.size() > 0) {
            return allFood;
        }

        throw new NoConsumptionsFoundException();
    }

    //
//    @Secured({"ROLE_ADMIN"})
    public Drink addDrink(DrinkViewModel drinkViewModel) {
        try {
                Drink drink = new Drink(drinkViewModel.getPrice(), drinkViewModel.getName(), drinkViewModel.getIngredients(), 1, drinkViewModel.getDrinkSize(), drinkViewModel.getDrinkType(), drinkViewModel.getImage(), drinkViewModel.getDescription());
                return consumptionRepository.save(drink);
        } catch (NullPointerException e) {
            throw new PostDrinkConsumptionException(drinkViewModel);
        }
}

    public Consumption getConsumption(long consumptionId) {
        Optional<Consumption> consumption = consumptionRepository.findById(consumptionId);

        if (consumption.isPresent()) {
            return consumption.get();
        }
        throw new ConsumptionNotFoundException(consumptionId);
    }

//    @Secured("ROLE_ADMIN")
    public String deleteConsumption(long consumptionId) {

        if (!consumptionRepository.existsById(consumptionId))
            throw new ConsumptionNotFoundException(consumptionId);

        consumptionRepository.deleteById(consumptionId);
        return "Consumption has been deleted";

    }
}
