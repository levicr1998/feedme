package nl.feedme.api.integrationTests;


import nl.feedme.api.ApiApplication;
import nl.feedme.api.models.Chef;
import nl.feedme.api.repositories.ChefRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(
        webEnvironment = WebEnvironment.MOCK, classes = ApiApplication.class)
@AutoConfigureMockMvc
@TestPropertySource(
        locations = "classpath:application-integrationtest.properties")
public class ChefRestControllerIntegrationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ChefRepository chefRepository;


    @Test
    public void getChef_thenStatus200()
            throws Exception {
        Chef chef = chefRepository.save(new Chef("Levi", "Hoi12341"));

        mvc.perform(get("/api/chefs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].username").value("Levi"));

    }
}
