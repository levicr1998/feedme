package nl.feedme.api.controllerTests;

import nl.feedme.api.controllers.IngredientPropertyController;
import nl.feedme.api.controllers.OrderController;
import nl.feedme.api.models.Chef;
import nl.feedme.api.models.IngredientProperty;
import nl.feedme.api.services.ChefService;
import nl.feedme.api.services.IngredientPropertyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(IngredientPropertyController.class)
public class IngredientPropertyControllerTests {

    @Autowired
    MockMvc mvc;

    @MockBean
    IngredientPropertyService ingredientPropertyService;

    @MockBean
    ChefService chefService;

    @Test
    public void getIngredientProperties_whenGet_ExpectIngredientProperties() throws Exception {
        IngredientProperty[] ingredientProperties = new IngredientProperty[3];

        ingredientProperties[0] = IngredientProperty.GLUTEN;
        ingredientProperties[1] = IngredientProperty.MELK;
        ingredientProperties[2] = IngredientProperty.NOTEN;
        given(ingredientPropertyService.getAllProperties()).willReturn(ingredientProperties);

        mvc.perform(get("/api/ingredientproperties/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$.[0]").value(ingredientProperties[0].toString()));
    }
}
