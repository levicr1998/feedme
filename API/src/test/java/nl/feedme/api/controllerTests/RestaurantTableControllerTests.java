package nl.feedme.api.controllerTests;


import nl.feedme.api.controllers.OrderController;
import nl.feedme.api.controllers.RestaurantTableController;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.UnPaidOrdersException;
import nl.feedme.api.models.Guest;
import nl.feedme.api.models.RestaurantTable;
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
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

import static nl.feedme.api.controllerTests.ConsumptionRestControllerTest.asJsonString;
import static org.hamcrest.Matchers.any;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RestaurantTableController.class)
public class RestaurantTableControllerTests {
    @Autowired
    MockMvc mvc;

    @MockBean
    RestaurantTableService restaurantTableService;

    @MockBean
    ChefService chefService;

    @MockBean
    PaypalService paypalService;

    @MockBean
    GuestService guestService;

    @MockBean
    QRGeneratorService qrGeneratorService;

    @Test
    public void getTables_whenGet_ExpectAll() throws Exception {
//        List<RestaurantTable> tables = new ArrayList<>();
//
//        RestaurantTable table = new RestaurantTable();
//        table.setId(1);
//        table.setNumber(1);
//
//        RestaurantTable table2 = new RestaurantTable();
//        table2.setId(2);
//        table2.setNumber(2);
//
//        tables.add(table);
//        tables.add(table2);
//
//        given(restaurantTableService.getAllTables()).willReturn(tables);
//        mvc.perform(get("/api/tables")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(2)))
//                .andExpect(jsonPath("$[0].number").value(tables.get(0).getNumber()));
    }

    @Test
    public void getTable_whenGet_ExpectTable() throws Exception {
        RestaurantTable table = new RestaurantTable();
        table.setId(1);
        table.setNumber(1);
        table.setSecretKey("TEST!@!");

        RestaurantTable table2 = new RestaurantTable();
        table2.setId(2);
        table2.setNumber(2);
        table2.setSecretKey("TEST2!@!");

        given(restaurantTableService.getTableFromToken("TEST!@!")).willReturn(table);
        mvc.perform(get("/api/tables/" + table.getSecretKey())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.number").value(table.getNumber()));
    }

    @Test
    public void postTable_whenPost_ExpectTable() throws Exception {
        RestaurantTable table = new RestaurantTable();
        table.setId(1);
        table.setNumber(1);

        given(restaurantTableService.addTable(table.getNumber())).willReturn(table);
        mvc.perform(MockMvcRequestBuilders
                .post("/api/tables/" + table.getId())
                .content(asJsonString(table.getId()))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getOpenTable_whenGet_ExpectOpenTable() throws Exception {
        RestaurantTable table = new RestaurantTable();
        table.setNumber(1);
        table.setId(1);

        List<Guest> guests = new ArrayList<>();
        guests.add(new Guest("Frits"));
        guests.add(new Guest("Barend"));
        table.setGuests(guests);

        given(restaurantTableService.openRestaurantTable(table)).willReturn(table);

        mvc.perform(MockMvcRequestBuilders
                .get("/api/tables/open/" + table.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    public void postOpenTable_whenPost_ExpectOpenTable() throws Exception {
        RestaurantTable table = new RestaurantTable();
        table.setNumber(1);
        table.setId(1);
        table.setSecretKey("SPOOKYKEY");

        List<Guest> guests = new ArrayList<>();
        guests.add(new Guest("Frits"));
        guests.add(new Guest("Barend"));
        table.setGuests(guests);

        given(restaurantTableService.getRestaurantTable((Long)table.getId())).willReturn(table);
        given(restaurantTableService.openRestaurantTable(table)).willReturn(table);
        given(guestService.addGuest(guests.get(0).getName())).willReturn(guests.get(0));
        given(restaurantTableService.joinRestaurantTable("SPOOKYKEY", guests.get(0))).willReturn(table);
        mvc.perform(MockMvcRequestBuilders
                .post("/api/tables/open/" + table.getId())
                .content(guests.get(0).getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void postCloseTable_whenPost_ExpectCloseTable() throws UnPaidOrdersException, Exception {
        RestaurantTable table = new RestaurantTable();
        table.setNumber(1);
        table.setId(1);
        table.setSecretKey("SPOOKYKEY");

        List<Guest> guests = new ArrayList<>();
        guests.add(new Guest("Frits"));
        guests.add(new Guest("Barend"));
        table.setGuests(guests);

        given(restaurantTableService.closeRestaurantTable(table)).willReturn(table);

        mvc.perform(MockMvcRequestBuilders
                .post("/api/tables/close/" + table.getId()))
                .andExpect(status().isOk());
    }

    @Test
    public void postJoinTable_whenPost_ExpectJoinTable() throws Exception {
        Guest guest = new Guest("Pietje");

        RestaurantTable restaurantTable = new RestaurantTable();
        restaurantTable.setId(1);
        restaurantTable.setNumber(1);
        restaurantTable.setSecretKey("SPOOKYKEY");

        given(guestService.addGuest(guest.getName())).willReturn(guest);
        given(restaurantTableService.joinRestaurantTable(restaurantTable.getSecretKey(), guest)).willReturn(restaurantTable);

        mvc.perform(MockMvcRequestBuilders
                .post("/api/tables/token/{token}", restaurantTable.getSecretKey())
                .content(guest.getName())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.guest.name").value(guest.getName()));
    }
}