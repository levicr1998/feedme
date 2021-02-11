package nl.feedme.api.integrationTests;


import nl.feedme.api.ApiApplication;
import nl.feedme.api.models.*;
import nl.feedme.api.repositories.ConsumptionRepository;
import nl.feedme.api.repositories.GuestRepository;
import nl.feedme.api.repositories.OrderRepository;
import nl.feedme.api.repositories.RestaurantTableRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ApiApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class OrderIntegrationTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ConsumptionRepository consumptionRepository;

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private GuestRepository guestRepository;
    @Test
    public void getOrder_thenStatus200()
            throws Exception {

        RestaurantTable table = new RestaurantTable();
        restaurantTableRepository.save(table);
        Food food = new Food(20.15,"Biefstuk", null, 2, FoodType.MAIN);
        consumptionRepository.save(food);
        Guest guest = new Guest("Barrie Poter");
        guestRepository.save(guest);
        Order order = orderRepository.save(new Order(table, food, null, guest));

        mvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].consumption.name").value("Biefstuk"))
                .andExpect(jsonPath("$[0].guest.name").value("Barrie Poter"))
                .andExpect(status().isOk());

    }
}
