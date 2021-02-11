package nl.feedme.api.integrationTests;


import nl.feedme.api.ApiApplication;
import nl.feedme.api.models.Consumption;
import nl.feedme.api.models.Food;
import nl.feedme.api.models.FoodType;
import nl.feedme.api.models.Ingredient;
import nl.feedme.api.repositories.ConsumptionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = ApiApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class ConsumptionRestControllerIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ConsumptionRepository consumptionRepository;

    @Test
    public void getConsumptions_thenStatus200()
            throws Exception {
        List<Ingredient> ingredients = new ArrayList<>();
        Consumption consumption = consumptionRepository.save(new Food( 20.15,"Biefstuk", ingredients, 2, FoodType.MAIN));

        mvc.perform(get("/api/consumptions")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value("Biefstuk"));

    }
}
