package nl.feedme.api.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.*;
import nl.feedme.api.models.Guest;
import nl.feedme.api.models.Order;
import nl.feedme.api.models.RestaurantTable;
import nl.feedme.api.models.TableIdAndNumber;
import nl.feedme.api.repositories.RestaurantTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static nl.feedme.api.security.SecurityConstants.*;

@Service("restaurantTableService")
public class RestaurantTableService {
    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private GuestService guestService;

    @Qualifier("QRGeneratorService")
    @Autowired
    private QRGeneratorService qr;

    @Autowired
    private OrderService orderService;

    public RestaurantTableService(){

    }

    public RestaurantTable getRestaurantTable(long restaurantTableId){
        Optional<RestaurantTable> restaurantTable = restaurantTableRepository.findById(restaurantTableId);

        if (restaurantTable.isPresent()) {
            return restaurantTable.get();
        }

        throw new RestaurantTableNotFoundException(restaurantTableId);
    }

    public RestaurantTable openRestaurantTable(RestaurantTable table) throws TableAlreadyOpenedException, UnsupportedEncodingException {
        if (table.isOpen()) {
            throw new TableAlreadyOpenedException(table);
        }
        Date opened = new Date(System.currentTimeMillis());
        String generatedKey = JWT.create()
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("openedAt", opened)
                .withClaim("tableId", table.getId())
                .sign(Algorithm.HMAC512(TABLE_SECRET.getBytes()));
        table.setSecretKey(generatedKey);
        table.setLastOpened(opened);
        try {
            table.setPrivateQrCode(qr.getPrivateTableQr(table.getSecretKey()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        restaurantTableRepository.save(table);
        return table;
    }

    public RestaurantTable joinRestaurantTable(String token, Guest guest) throws TableExpiredException, TableNotOpenException, UnsupportedEncodingException {
        RestaurantTable table = getTableFromToken(token);
//        if (table.getGuests().contains(guest)) {
//            throw new NickNameAlreadyJoinedTableException();
//        }
        table.addGuest(guest);
        restaurantTableRepository.save(table);
        return table;

    }

    public RestaurantTable closeRestaurantTable(RestaurantTable table) throws UnPaidOrdersException {
        List<Order> unpaid = orderService.getUnpaidOrdersByTableId(table.getId());
        if (unpaid.size() > 0) {
            throw new UnPaidOrdersException(unpaid.get(0));
        }
        table.setSecretKey(null);
        table.setPrivateQrCode(null);
        while(table.getGuests().size() > 0) {
            Guest g = table.getGuests().get(0);
            table.removeGuestFromTable(g);
            //guestService.removeGuest(g);
        }
        return restaurantTableRepository.save(table);
    }



    public RestaurantTable addTable(int id) {
        return restaurantTableRepository.save(new RestaurantTable(id));
    }

    public Iterable<RestaurantTable> getAllTables() {
        return restaurantTableRepository.findAll();
    }

    public List<RestaurantTable> getAllTablesUnpaidOrders() {
        List<RestaurantTable> restaurantTables = new ArrayList<>();
        List<TableIdAndNumber> query = restaurantTableRepository.getAllNotDeep();

        for (int i = 0; i < query.size(); i++) {
            TableIdAndNumber idAndNumber = query.get(i);
            long id = idAndNumber.getTable_id();
            List<Order> orders = orderService.getOrderedUnpaidOrders(id);
            if (orders.size() > 0) {
                restaurantTables.add(new RestaurantTable(idAndNumber, orders));
            }
        }
        return restaurantTables;
    }

    private void addOrdersToTable(RestaurantTable table, List<Order> orders) {
        for (Order order :
            orders) {
            table.addOrder(order);

        }
    }

    public RestaurantTable getTableById(Long tableId) throws TableNotOpenException {
        Optional<RestaurantTable> tableOptional = restaurantTableRepository.findById(tableId);
        if (!tableOptional.isPresent()) {
            throw new RestaurantTableNotFoundException(tableId);
        }

        RestaurantTable table = tableOptional.get();
        if (!table.isOpen()) {
            throw new TableNotOpenException();
        }

        return tableOptional.get();
    }

    public RestaurantTable getTableFromToken(String token) throws TableExpiredException, TableNotOpenException, UnsupportedEncodingException {
        DecodedJWT jwt;
        try {
            jwt = JWT.require(Algorithm.HMAC512(TABLE_SECRET.getBytes()))
                    .build()
                    .verify(token);
        } catch (Exception e) {
            throw new CustomTokenExpiredException();
        }

        if (jwt.getExpiresAt().before(new Date(System.currentTimeMillis()))) {
            throw new TableExpiredException();
        }


        long tableId = jwt.getClaim("tableId").asLong();


        Optional<RestaurantTable> tableOptional = restaurantTableRepository.findById(tableId);
        if (!tableOptional.isPresent()) {
            throw new RestaurantTableNotFoundException(tableId);
        }

        RestaurantTable table = tableOptional.get();
        if (!table.isOpen()) {
            throw new TableNotOpenException();
        }

        return table;
    }
}
