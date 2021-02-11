package nl.feedme.api.controllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.feedme.api.ViewModel.DrinkViewModel;
import nl.feedme.api.ViewModel.FoodViewModel;
import nl.feedme.api.controllers.ConsumptionController;
import nl.feedme.api.exceptionHandler.ConsumptionExceptions.PostDrinkConsumptionException;
import nl.feedme.api.exceptionHandler.ConsumptionExceptions.PostFoodConsumptionException;
import nl.feedme.api.models.*;
import nl.feedme.api.services.ChefService;
import nl.feedme.api.services.ConsumptionService;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ConsumptionController.class)
public class ConsumptionRestControllerTest {

    @Autowired
    MockMvc mvc;

    @MockBean
    ConsumptionService consumptionService;
    @MockBean
    ChefService chefService;

    @Test
    public void givenConsumptions_whenGet_ExpectAll() throws  Exception {
        List<Consumption> consumptions = new ArrayList<>();
        consumptions.add(new Drink(2.5, "Iced Tea", new ArrayList<Ingredient>(), 0, DrinkSize.SMALL, DrinkType.COLD));
        consumptions.add(new Drink(3, "Beer", new ArrayList<Ingredient>(), 3, DrinkSize.MEDIUM, DrinkType.COLD));
        consumptions.add(new Drink(1.75, "Coffee", new ArrayList<Ingredient>(), 0, DrinkSize.SMALL, DrinkType.HOT));

        given(consumptionService.getAllConsumption()).willReturn(consumptions);

        mvc.perform(get("/api/consumptions")
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].name").value(consumptions.get(0).getName()));
    }


    @Test
    public void postFood_whenPost_ExpectFood() throws  Exception {
       FoodViewModel foodViewModel = new FoodViewModel(FoodType.MAIN, 20.00, "Biefstuk", new ArrayList<>(),1,null,"Dodadas");
       Food food = new Food(0, 20.00, "Biefstuk", new ArrayList<>(),1,FoodType.MAIN, null,"Dodadas");
      given(consumptionService.addFood(any(FoodViewModel.class))).willReturn(food);

        mvc.perform(MockMvcRequestBuilders
                .post("/api/consumptions/food")
                .content(asJsonString(foodViewModel))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(food.getName()));

    }

    @Test
    public void postDrink_whenPost_ExpectDrink() throws  Exception {
        DrinkViewModel drinkViewModel = new DrinkViewModel(12.10,"Cola",new ArrayList<Ingredient>(),1,DrinkSize.SMALL,DrinkType.HOT,null,"Heerlijk");
        Drink drink = new Drink(0,12.10,"Cola",new ArrayList<Ingredient>(),1,DrinkSize.SMALL,DrinkType.HOT,null,"Heerlijk");
        given(consumptionService.addDrink(any(DrinkViewModel.class))).willReturn(drink);

        mvc.perform(MockMvcRequestBuilders
                .post("/api/consumptions/drink")
                .content(asJsonString(drinkViewModel))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(drink.getName()));
    }

    @Test
    public void postDrink_whenPost_ExpectException() throws  Exception {
        DrinkViewModel drinkViewModel = new DrinkViewModel(12.10,"Cola",new ArrayList<Ingredient>(),1,DrinkSize.SMALL,DrinkType.HOT,null,"Heerlijk");
        Drink drink = new Drink(0,12.10,"Cola",new ArrayList<Ingredient>(),1,DrinkSize.SMALL,DrinkType.HOT,null,"Heerlijk");
        given(consumptionService.addDrink(any())).willThrow(new PostDrinkConsumptionException(drinkViewModel));

        mvc.perform(MockMvcRequestBuilders
                .post("/api/consumptions/drink")
                .content(asJsonString(new DrinkViewModel()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void postFood_whenPost_ExpectException() throws  Exception {
        FoodViewModel foodViewModel = new FoodViewModel(FoodType.MAIN, 20.00, "Biefstuk", new ArrayList<>(),1,null,"Dodadas");
        Food food = new Food(0, 20.00, "Biefstuk", new ArrayList<>(),1,FoodType.MAIN, null,"Dodadas");
        given(consumptionService.addFood(any(FoodViewModel.class))).willThrow(new PostFoodConsumptionException(foodViewModel));

        mvc.perform(MockMvcRequestBuilders
                .post("/api/consumptions/food")
                .content(asJsonString(new FoodViewModel()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotAcceptable());
    }

    @Test
    public void getConsumption_whenGet_ExpectConsumption() throws Exception {
        List<Consumption> consumptions = new ArrayList<>();

        consumptions.add(new Food(31, 20.00, "Biefstuk", new ArrayList<>(),1,FoodType.MAIN, null,"Dodadas"));
        consumptions.add(new Drink(5,12.10,"Cola", new ArrayList<>(),1,DrinkSize.SMALL,DrinkType.HOT,null,"Heerlijk"));

        given(consumptionService.getConsumption(5)).willReturn(consumptions.get(0));
        mvc.perform(MockMvcRequestBuilders.get("/api/consumptions/" + 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(consumptions.get(0).getId()));
    }


    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
