package nl.feedme.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.google.zxing.WriterException;
import nl.feedme.api.ViewModel.JoinTableViewModel;
import nl.feedme.api.ViewModel.PaypalSuccess;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.*;
import nl.feedme.api.models.Guest;
import nl.feedme.api.models.RestaurantTable;
import nl.feedme.api.models.Websockets.OberReceived;
import nl.feedme.api.models.Websockets.WebsocketOberRequest;
import nl.feedme.api.services.GuestService;
import nl.feedme.api.services.PaypalService;
import nl.feedme.api.services.QRGeneratorService;
import nl.feedme.api.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import static nl.feedme.api.security.SecurityConstants.HEADER_STRING;

@RestController
@RequestMapping(path = "/api")
public class RestaurantTableController {
    @Autowired
    private RestaurantTableService tableService;

    @Autowired
    private GuestService guestService;

    @Autowired
    private QRGeneratorService qr;

    @Autowired
    private PaypalService paypalService;

    @Value("${front-end-url}")
    private String frontendUrl;


    @Value("${api-url}")
    private String apiUrl;

    @GetMapping(value = "/tables/qr/{id}", produces = MediaType.IMAGE_PNG_VALUE )
    public @ResponseBody
    ResponseEntity getImage(@PathVariable long id, HttpServletRequest request) throws IOException, WriterException {
        byte[] image = qr.getPublicTableQR(id);
        System.out.println("Image (API call):");
        System.out.println(Base64.getEncoder().encodeToString(image));
        return ResponseEntity.ok(image);
    }

    @GetMapping(value = "/tables/private-qr/{token}", produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody
    byte[] getPrivateQr(@PathVariable String token, HttpServletRequest request) throws IOException, WriterException, TableExpiredException, TableNotOpenException {
        return tableService.getTableFromToken(token).getPrivateQrCode();
    }

    @GetMapping(value = "/tables/{token}")
    public ResponseEntity getTableByToken(@PathVariable String token) throws Exception {
        return ResponseEntity.ok(tableService.getTableFromToken(token));
    }

    //TODO: ONLY CHEF is allowed to do this.
    @PostMapping("/tables/{tableNumber}")
    public Object addTable(@PathVariable int tableNumber) {
        return tableService.addTable(tableNumber);
    }

    @GetMapping("/tables/open/{id}")
    public void openTable(@PathVariable int id, HttpServletResponse response) throws IOException {
        response.sendRedirect(frontendUrl + "/insert-name?open=" + id);
    }

    @PostMapping("/tables/open/{tableId}")
    public @ResponseBody
    JoinTableViewModel openTable(@PathVariable long tableId, @RequestBody String nickname, HttpServletResponse response) throws Exception {
        RestaurantTable table = tableService.getRestaurantTable(tableId);
        table = tableService.openRestaurantTable(table);
        Guest g = guestService.addGuest(nickname);
        table = tableService.joinRestaurantTable(table.getSecretKey(), g);
        JoinTableViewModel model = new JoinTableViewModel(table.getSecretKey(), g);
        return model;
    }

    @GetMapping("/tables/token/{token}")
    public void joinTableExternal(@PathVariable String token, HttpServletResponse response) throws TableExpiredException, IOException {
        response.sendRedirect(frontendUrl + "/insert-name?token=" + token);
    }

    @PostMapping("/tables/token/{token}")
    public @ResponseBody
    JoinTableViewModel joinTableInternal(@PathVariable String token, @RequestBody String guestName) throws TableExpiredException, TableNotOpenException, UnsupportedEncodingException {
        Guest guest = guestService.addGuest(guestName);
        tableService.joinRestaurantTable(token, guest);
        JoinTableViewModel model = new JoinTableViewModel(token, guest);
        return model;

    }

    @PostMapping("/tables/checkout")
    public ResponseEntity<Map<String, String>> startCheckoutTable(@RequestBody String token, HttpServletResponse response) throws IOException, TableExpiredException, TableNotOpenException {
        return ResponseEntity.ok(paypalService.createPayment(tableService.getTableFromToken(token).getId()));
    }

    @PostMapping("/tables/checkout/complete")
    public String completeCheckoutTable(@RequestBody PaypalSuccess payment) {
        return paypalService.completePayment(payment.getPaymentId(), payment.getPayerId());
    }

    @PostMapping("/tables/close/{id}")
    public void closeTable(@PathVariable long id) throws UnPaidOrdersException {
        tableService.closeRestaurantTable(tableService.getRestaurantTable(id));
    }

    @GetMapping(value = "/tables/chef", produces={"application/json"})
    public @ResponseBody
    Object listTablesForChef() throws JsonProcessingException {
        List<?> object = tableService.getAllTablesUnpaidOrders();
        ObjectMapper mapper = new ObjectMapper();
        FilterProvider filters = new SimpleFilterProvider().addFilter("consumption", SimpleBeanPropertyFilter.serializeAllExcept("image"));
        ObjectWriter writer = mapper.writer(filters);
        return ResponseEntity.ok(writer.writeValueAsString(object));
    }

    @GetMapping(value = "/tables/all")
    public @ResponseBody
    Object allTables() {
        return tableService.getAllTables();
    }


    @GetMapping("/tables/is-open/{tokenOrId}")
    public boolean isOpen(@PathVariable String tokenOrId) throws UnsupportedEncodingException, TableNotOpenException, TableExpiredException {
        RestaurantTable table;
        try {
            long i = Integer.parseInt(tokenOrId);
            table = tableService.getRestaurantTable(i);
            return table.isOpen();
        } catch (Exception ignored) {}
        try {
            table = tableService.getTableFromToken(tokenOrId);
            return table.isOpen();
        } catch (Exception ignored) {}
        throw new TableNotAvailebleException();
    }

    @MessageMapping("/newOberRequest")
    @SendTo("/topic/oberReceived")
    public OberReceived oberReceived(WebsocketOberRequest order) throws UnsupportedEncodingException, TableNotOpenException, TableExpiredException, TableOrderUserConflictException {
        return new OberReceived(order.getTableId(), tableService.getTableById((long) order.getTableId()).getNumber(), null);
    }
}
