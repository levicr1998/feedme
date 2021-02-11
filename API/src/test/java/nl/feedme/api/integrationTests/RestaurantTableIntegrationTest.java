package nl.feedme.api.integrationTests;


import nl.feedme.api.ApiApplication;
import nl.feedme.api.models.*;
import nl.feedme.api.repositories.RestaurantTableRepository;
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
public class RestaurantTableIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;
    @Test
    public void getTable_thenStatus200()
            throws Exception {

        RestaurantTable table = new RestaurantTable();
        table.setId(1);
        table.setNumber(1);

        restaurantTableRepository.save(table);
//        mvc.perform(get("/api/tables/all")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content()
//                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$[0].id").value("1"))
//                .andExpect(status().isOk());

    }

}
