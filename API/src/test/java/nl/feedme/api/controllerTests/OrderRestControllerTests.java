package nl.feedme.api.controllerTests;

import nl.feedme.api.ViewModel.OrderViewModel;
import nl.feedme.api.controllers.OrderController;
import nl.feedme.api.models.*;
import nl.feedme.api.services.ChefService;
import nl.feedme.api.services.ConsumptionService;
import nl.feedme.api.services.OrderService;
import nl.feedme.api.services.RestaurantTableService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static nl.feedme.api.controllerTests.ConsumptionRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(OrderController.class)
public class OrderRestControllerTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    OrderService orderService;
    @MockBean
    ChefService chefService;
    @MockBean
    ConsumptionService consumptionService;
    @MockBean
    RestaurantTableService restaurantTableService;
    @Test
    public void givenOrders_whenGet_ExpectAll() throws  Exception {
        List<Order> orders = new ArrayList<>();
        Consumption consumption = new Drink(2.5, "Iced Tea", new ArrayList<Ingredient>(), 0, DrinkSize.SMALL, DrinkType.COLD);
        RestaurantTable restaurantTable = new RestaurantTable(null,1,null);
        orders.add(new Order(restaurantTable, consumption, "fdffsd", new Guest("Hey")));

        given(orderService.getAllOrders()).willReturn(orders);
        mvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].consumption.name").value(orders.get(0).getConsumption().getName()));
    }
    @Test
   public void getOrders_whenGet_ExpectOrdersByTable() throws Exception{
        List<Order> orders = new ArrayList<>();
        Consumption consumption = new Drink(3.5, "Fanta", new ArrayList<>(), 0, DrinkSize.MEDIUM, DrinkType.COLD);
        Consumption consumption2 = new Drink(2.5, "Coffee", new ArrayList<>(), 0, DrinkSize.LARGE, DrinkType.HOT);

        RestaurantTable restaurantTable = new RestaurantTable(null, 1, null);
        RestaurantTable restaurantTable2 = new RestaurantTable(null, 5, null);

        orders.add(new Order(restaurantTable, consumption, "Geen noot", new Guest("Hey")));
        orders.add(new Order(restaurantTable2, consumption2, "Geen noot", new Guest("Hey")));

        given(orderService.getUnpaidOrdersByTableId(5)).willReturn(orders.subList(1,2));

        mvc.perform(get("/api/orders/table/"+5)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
//                .andExpect(jsonPath("$[0].consumption.name").value(orders.get(1).getConsumption().getName()));
    }
    @Test
    public void postOrder_whenPost_ExpectOrder() throws Exception{
        Consumption consumption = new Drink(2.5, "Iced Tea", new ArrayList<Ingredient>(), 0, DrinkSize.SMALL, DrinkType.COLD);
        RestaurantTable restaurantTable = new RestaurantTable(null,1,null);
        Order order = new Order(restaurantTable, consumption, "fdffsd", new Guest("Hey"));
        given(orderService.addOrder(any(OrderViewModel.class))).willReturn(order);

        mvc.perform(MockMvcRequestBuilders
                .post("/api/orders")
                .content(asJsonString(order))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getOrder_whenGet_OrderById() throws Exception {
        List<Order> orders = new ArrayList<>();
        Consumption consumption = new Drink(2.5, "Iced Tea", new ArrayList<Ingredient>(), 0, DrinkSize.SMALL, DrinkType.COLD);
        Consumption consumption2 = new Drink(2.5, "Cola", new ArrayList<Ingredient>(), 0, DrinkSize.LARGE, DrinkType.COLD);

        RestaurantTable restaurantTable = new RestaurantTable(null, 1, null);

        Order order = new Order(restaurantTable,consumption,"NOT", new Guest("Hey"));
        orders.add(new Order(restaurantTable, consumption, "NootNoot", new Guest("Hey")));
        orders.add(new Order(restaurantTable, consumption2, "NootNoot", new Guest("Hey")));

        given(orderService.getOrder(1)).willReturn(order);

        mvc.perform(get("/api/orders/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consumption.name").value(order.getConsumption().getName()));
    }

    @Test
    public void deleteOrder_whenDelete_OrderById() throws Exception {
        List<Order> orders = new ArrayList<>();

        Order order = new Order(
                new RestaurantTable(null, 1, null),
                new Drink(2.0, "Appelsap", new ArrayList<>(), 0, DrinkSize.MEDIUM, DrinkType.COLD),
                "Niks",
                new Guest("Hey"));
        Order order1 = new Order(
                new RestaurantTable(null, 2, null),
                new Drink(2.0, "Appelsap", new ArrayList<>(), 0, DrinkSize.MEDIUM, DrinkType.COLD),
                "Niks",
                new Guest("Hey"));
        orders.add(order);
        orders.add(order1);

        given(orderService.deleteOrder(1)).willReturn( new BasicJsonParser().parseMap("{ \"message\": \"Consumption has been deleted\" }"));

        mvc.perform(get("/api/orders/1")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
