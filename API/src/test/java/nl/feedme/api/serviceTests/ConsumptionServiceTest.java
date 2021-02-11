package nl.feedme.api.serviceTests;

import nl.feedme.api.ViewModel.DrinkViewModel;
import nl.feedme.api.ViewModel.FoodViewModel;
import nl.feedme.api.exceptionHandler.ConsumptionExceptions.PostDrinkConsumptionException;
import nl.feedme.api.exceptionHandler.ConsumptionExceptions.PostFoodConsumptionException;
import nl.feedme.api.models.*;
import nl.feedme.api.repositories.ConsumptionRepository;
import nl.feedme.api.services.ConsumptionService;
import nl.feedme.api.services.QRGeneratorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class ConsumptionServiceTest {

    @Autowired
    private ConsumptionService consumptionService;

    @Autowired
    private ConsumptionRepository consumptionRepository;

    DrinkViewModel drinkViewModel;
    Drink drink;
    FoodViewModel foodViewModel;
    Food food;
    @Before
    public void setUp() {
        Consumption cons = new Drink(
                1,
                2.3,
                "Coffee",
                new ArrayList<Ingredient>(),
                1,
                DrinkSize.SMALL,
                DrinkType.HOT);

        drinkViewModel = new DrinkViewModel(12.10,"Cola",new ArrayList<Ingredient>(),1,DrinkSize.SMALL,DrinkType.HOT,null,"Heerlijk");
        drink = new Drink(0,12.10,"Cola",new ArrayList<Ingredient>(),1,DrinkSize.SMALL,DrinkType.HOT,null,"Heerlijk");
        foodViewModel = new FoodViewModel(FoodType.MAIN, 20.00, "Biefstuk", new ArrayList<>(),1,null,"Dodadas");
        food = new Food(0, 20.00, "Biefstuk", new ArrayList<>(),1,FoodType.MAIN, null,"Dodadas");
        List<Consumption> list = new ArrayList<Consumption>();
        list.add(cons);
        Mockito.when(consumptionRepository.findAll()).thenReturn(list);
        Mockito.when(consumptionRepository.save(any(Drink.class))).thenReturn(drink);
        Mockito.when(consumptionRepository.save(any(Food.class))).thenReturn(food);
    }

    @Test
    public void getConsumption() {
        List<Consumption> consumptions = (List<Consumption>) consumptionService.getAllConsumption();
        Assert.assertTrue(consumptions.size() == 1);
    }

    //postfoodtest
@Test
    public void addDrink(){
Drink drinkTest = consumptionService.addDrink(drinkViewModel);
Assert.assertEquals(drink.getName(), drinkTest.getName());
}

    @Test
    public void addFood(){
Food foodTest = consumptionService.addFood(foodViewModel);
Assert.assertEquals(food.getName(), foodTest.getName());
    }

    @Test
    public void addDrinkNull(){
try {

} catch (Exception e){
    Assert.assertEquals(PostDrinkConsumptionException.class, e.getClass());
}
    }

    @Test
    public void addFoodNull(){
        try {
Food food = consumptionService.addFood(new FoodViewModel());
        } catch (Exception e){
            Assert.assertEquals(PostFoodConsumptionException.class, e.getClass());
        }
    }

}
