package nl.feedme.api.controllerTests;

import com.auth0.jwt.JWT;
import com.google.gson.Gson;
import nl.feedme.api.ViewModel.JoinTableViewModel;
import nl.feedme.api.controllers.GuestController;
import nl.feedme.api.controllers.OrderController;
import nl.feedme.api.models.*;
import nl.feedme.api.services.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(GuestController.class)
public class GuestRestControllerTests {
    @Autowired
    MockMvc mvc;

    @MockBean
    GuestService guestService;
    @MockBean
    ChefService chefService;
    @MockBean
    ConsumptionService consumptionService;
    @MockBean
    RestaurantTableService restaurantTableService;

    @Test
    public void getGuests_whenGet_ExpectAll() throws Exception {
        List<Guest> guests = new ArrayList<>();

        Guest guest = new Guest();
        guest.setName("Frits");
        guest.setId(2);
        guests.add(guest);

        Guest guest2 = new Guest();
        guest2.setName("Barend");
        guest2.setId(5);
        guests.add(guest2);

        given(guestService.allGuests()).willReturn(guests);
        mvc.perform(get("/api/guests")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(guests.get(0).getName()));
    }

    @Test
    public void postGuest_whenPost_ExpectGuest() throws Exception {
        Guest guest = new Guest();
        guest.setName("Karel");

        given(guestService.addGuest("Karel")).willReturn(guest);

        mvc.perform(MockMvcRequestBuilders
                .post("/api/guests")
                .param("name", guest.getName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(guest.getName()));
    }

    @Test
    public void postValidateGuest_whenPost_ExpectValidateGuest() throws Exception {
        Guest guest = new Guest("Levi");
        guest.setId(121);

        RestaurantTable table = new RestaurantTable();
        table.setId(1);
        table.setNumber(1);
        table.setSecretKey("SPOOKYKEY");
        JoinTableViewModel tableViewModel = new JoinTableViewModel(table.getSecretKey(), guest);

        given(guestService.findById(guest.getId())).willReturn(guest);
        given(restaurantTableService.getTableFromToken(table.getSecretKey())).willReturn(table);

        Gson gson = new Gson();
        String object = gson.toJson(tableViewModel);

        mvc.perform(MockMvcRequestBuilders
                .post("/api/guests/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(object))
                .andExpect(status().isOk());
    }
}
