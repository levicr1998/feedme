package nl.feedme.api.controllerTests;

import nl.feedme.api.ViewModel.ChefViewModel;
import nl.feedme.api.ViewModel.FoodViewModel;
import nl.feedme.api.controllers.ChefController;
import nl.feedme.api.exceptionHandler.ConsumptionExceptions.PostFoodConsumptionException;
import nl.feedme.api.models.Chef;
import nl.feedme.api.services.ChefService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static nl.feedme.api.controllerTests.ConsumptionRestControllerTest.asJsonString;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ChefController.class)
public class ChefRestControllerTest {
    @Autowired
    MockMvc mvc;

    @MockBean
    ChefService chefService;

    @Test
    public void givenChefs_whenGet_ExpectAll() throws Exception {
        List<Chef> chefs = new ArrayList<>();
        chefs.add(new Chef("Lars", "JDIVJE(B&#H@C#(DJ#DJC"));
        chefs.add(new Chef("Levi", "JDIVJE(B&#H@C#(DJ#DJC"));

        given(chefService.loadAll()).willReturn(chefs);

        mvc.perform(get("/api/chefs")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].username").value(chefs.get(0).getUsername()));

    }

    @Test
    public void givesChefs_whenGet_getChefById() throws Exception {
        List<Chef> chefs = new ArrayList<>();

        chefs.add(new Chef("Lars", "JDIVJE(B&#H@C#(DJ#DJC"));
        chefs.add(new Chef("Levi", "JDIVJE(B&#H@C#(DJ#DJC"));

        given(chefService.loadUserByUsername("Lars")).willReturn(chefs.get(0));

        mvc.perform(get("/api/chefs/"+chefs.get(0).getUsername())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(chefs.get(0).getUsername()));
    }

    @Test
    public void postChef_whenPost_registerChef() throws Exception {
        Chef chef = new Chef();
        chef.setUsername("Pietje");
        chef.setId(15);
        chef.setPassword("38382xKXKKXJXJ@#@");
        ChefViewModel chefViewModel = new ChefViewModel(chef);
        given(chefService.saveChef(chef)).willReturn(chef);

        mvc.perform(MockMvcRequestBuilders
                .post("/api/chefs")
                .content(asJsonString(chefViewModel))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}