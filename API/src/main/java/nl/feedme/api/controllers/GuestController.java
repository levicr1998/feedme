package nl.feedme.api.controllers;

import nl.feedme.api.ViewModel.JoinTableViewModel;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableExpiredException;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableNotOpenException;
import nl.feedme.api.models.Guest;
import nl.feedme.api.models.RestaurantTable;
import nl.feedme.api.repositories.GuestRepository;
import nl.feedme.api.services.GuestService;
import nl.feedme.api.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api")
public class GuestController {
    @Autowired
    private GuestService guestService;

    @Autowired
    private RestaurantTableService tableService;


    @GetMapping("/guests")
    public @ResponseBody List<Guest> getAll () {
        return guestService.allGuests();
    }

    @PostMapping(path = "/guests")
    public @ResponseBody Object addGuest(@RequestParam String name) {
        return guestService.addGuest(name);
    }

    @PostMapping("/guests/validate")
    public @ResponseBody Object validateGuest(@RequestBody JoinTableViewModel model) throws UnsupportedEncodingException, TableNotOpenException, TableExpiredException {
        Guest guest = guestService.findById(model.getGuest().getId());
        RestaurantTable table = tableService.getTableFromToken(model.getToken());
        return table.getGuests().contains(guest);
    }
}
