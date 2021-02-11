package nl.feedme.api.serviceTests;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.*;
import nl.feedme.api.models.Guest;
import nl.feedme.api.models.RestaurantTable;
import nl.feedme.api.models.TableIdAndNumber;
import nl.feedme.api.repositories.GuestRepository;
import nl.feedme.api.repositories.RestaurantTableRepository;
import nl.feedme.api.services.RestaurantTableService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static nl.feedme.api.security.SecurityConstants.EXPIRATION_TIME;
import static nl.feedme.api.security.SecurityConstants.TABLE_SECRET;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class RestaurantTableServiceTest {

    static String token = JWT.create()
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .withClaim("tableId", 2)
            .sign(Algorithm.HMAC512(TABLE_SECRET.getBytes()));

    @Autowired
    private RestaurantTableService restaurantTableService;

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Qualifier("passwordEncoder")
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Before
    public void setUp() {
        // table closed
        RestaurantTable table1 = new RestaurantTable();
        table1.setId(1L);
        table1.setNumber(1);

        // table open
        RestaurantTable table2 = new RestaurantTable();
        table2.setId(2L);
        table2.setNumber(2);
        table2.setSecretKey(token);
        table2.addGuest(new Guest("Bert"));



        List<RestaurantTable> tableList = new ArrayList<>();
        tableList.add(table1);
        Mockito.when(restaurantTableRepository.findAll()).thenReturn(tableList);
        Mockito.when(restaurantTableRepository.findById(1L)).thenReturn(Optional.of(table1));
        Mockito.when(restaurantTableRepository.findById(2L)).thenReturn(Optional.of(table2));

        Mockito.when(restaurantTableRepository.save(any(RestaurantTable.class))).thenReturn(new RestaurantTable());
        Mockito.when(guestRepository.findById(1L)).thenReturn(Optional.of(new Guest("Lars")));
    }


    @Test
    public void testClosedTableToOpen() {

        RestaurantTable table = restaurantTableService.getRestaurantTable(1L);

        try {
            table = restaurantTableService.openRestaurantTable(table);
            Assert.assertNotNull(table.getSecretKey());
            System.err.println(table.getSecretKey());
        } catch (TableAlreadyOpenedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testNotExistingTable() {
        try {
            RestaurantTable table = restaurantTableService.getRestaurantTable(3L);
        } catch (Exception e) {
            Assert.assertSame(e.getClass(), RestaurantTableNotFoundException.class);
        }

    }

    @Test
    public void testOpenTableToOpen() {

        RestaurantTable table = restaurantTableService.getRestaurantTable(2L);
        Assert.assertNotNull(table);
        try {
            restaurantTableService.openRestaurantTable(table);
        } catch (Exception e) {
            Assert.assertSame(e.getClass(), TableAlreadyOpenedException.class);
        }

    }

    @Test
    public void joinOpenTable() throws TableExpiredException, TableNotOpenException, UnsupportedEncodingException {
        RestaurantTable table = restaurantTableService.joinRestaurantTable(token, new Guest("Lars"));
        Assert.assertEquals(2, table.getGuests().size()); // Lars en Bert
        for (Guest g :
                table.getGuests()) {
            System.err.println(g.getName());
        }

    }

    @Test
    public void joinOpenTableWithExistingName() {
        try {
            RestaurantTable table = restaurantTableService.joinRestaurantTable(token, new Guest("Bert"));
        } catch (Exception e) {
            Assert.assertSame(e.getClass(), NickNameAlreadyJoinedTableException.class);
        }
    }

    @Test
    public void listAll() {
        List<RestaurantTable> tables = (List<RestaurantTable>) restaurantTableService.getAllTables();
        Assert.assertTrue(tables.size()>0);
    }
}
