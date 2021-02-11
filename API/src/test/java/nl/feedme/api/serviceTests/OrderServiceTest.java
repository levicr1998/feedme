package nl.feedme.api.serviceTests;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.feedme.api.ViewModel.OrderViewModel;
import nl.feedme.api.exceptionHandler.OrderExceptions.NoOrdersFoundException;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.CustomTokenExpiredException;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableExpiredException;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableNotOpenException;
import nl.feedme.api.models.*;
import nl.feedme.api.repositories.ConsumptionRepository;
import nl.feedme.api.repositories.GuestRepository;
import nl.feedme.api.repositories.OrderRepository;
import nl.feedme.api.repositories.RestaurantTableRepository;
import nl.feedme.api.services.OrderService;
import nl.feedme.api.services.RestaurantTableService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static nl.feedme.api.security.SecurityConstants.EXPIRATION_TIME;
import static nl.feedme.api.security.SecurityConstants.TABLE_SECRET;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ServiceTestConfiguration.class)
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantTableService restaurantTableService;

    @Autowired
    private RestaurantTableRepository restaurantTableRepository;

    @Autowired
    private ConsumptionRepository consumptionRepository;

    @Autowired
            private GuestRepository guestRepository;

    long order_id_276 = 276;
    long order_id_280 = 280;
    File orderDrinkFile = new File("src/test/resources/orderDrink.json");
    File orderFoodFile = new File("src/test/resources/orderFood.json");
    File ordersFile = new File("src/test/resources/orders.json");
    ObjectMapper objectMapper = new ObjectMapper();
    Order orderDrink = null;
    Order orderFood = null;
    Order orderTest = null;
    OrderViewModel orderViewModelTest = null;
    List<Order> orders = new ArrayList<>();
    Guest guest;
    Optional<Guest> guestOptional;
    List<Order> ordersByRestaurantTable = new ArrayList<>();

    static String token = JWT.create()
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .withClaim("tableId", 2)
            .sign(Algorithm.HMAC512(TABLE_SECRET.getBytes()));

    @Before
    public void setUp(){

        try {
            orderDrink = objectMapper.readValue(new FileReader(orderDrinkFile), Order.class);
            orderFood = objectMapper.readValue(new FileReader(orderFoodFile), Order.class);
            orders = objectMapper.readValue(new FileReader(ordersFile), new TypeReference<List<Order>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }

        Consumption consumption1 = new Drink(2.5, "Iced Tea", new ArrayList<Ingredient>(), 0, DrinkSize.SMALL, DrinkType.COLD);
        consumption1.setId(1);
        RestaurantTable restaurantTable1 = new RestaurantTable(new ArrayList<Guest>(), 2, null);
        restaurantTable1.setId(2);
        restaurantTable1.setSecretKey("Cool");

        guest = new Guest ("Hey");
        guestOptional = Optional.of(guest);
        orderTest = new Order(2,restaurantTable1,consumption1,"GFDFGFGFG" , guest);
        orderViewModelTest = new OrderViewModel(1,token,"GFDFGFGFG");

        ordersByRestaurantTable.add(orderFood);
        Mockito.when(orderRepository.findById(order_id_276)).thenReturn(orderDrink);
        Mockito.when((orderRepository.findAllByRestaurantTable_IdAndIsPaid(1, false))).thenReturn(ordersByRestaurantTable);
        Mockito.when(orderRepository.findAll()).thenReturn(orders);
        Mockito.when(guestRepository.findById(guest.getId())).thenReturn(guestOptional);
        Mockito.when(orderRepository.getAllByRestaurantTable_Id(1)).thenReturn(ordersByRestaurantTable);
        Mockito.when(orderRepository.save(any(Order.class))).thenReturn(orderTest);
        Mockito.when(restaurantTableRepository.findById(restaurantTable1.getId())).thenReturn((Optional.of(restaurantTable1)));
        Mockito.when(consumptionRepository.findById(consumption1.getId())).thenReturn(Optional.of(consumption1));

    }

    @Test
    public void getOrderbyId() {
        Order getOrderById = orderService.getOrder(276);
        assertThat(getOrderById).isEqualTo(orderDrink);
    }

    @Test
    public void getOrdersbyTableId(){
        long id = 1;
        List<Order> getOrdersByTableId = orderService.getUnpaidOrdersByTableId(id);
        assertThat(getOrdersByTableId).isEqualTo(ordersByRestaurantTable);

    }

    @Test
    public void getOrdersbyTableIdError() {
        long id = 2;
        try {
        List<Order> getOrdersByTableId = orderService.getUnpaidOrdersByTableId(id);
    } catch(Exception e) {
assertThat(e.getClass()).isEqualTo(NoOrdersFoundException.class);
    }
    }


    @Test
    public void getOrders(){
        List<Order> getOrders = orderService.getAllOrders();
        assertThat(getOrders).isEqualTo(orders);
    }

    @Test
    public void addOrder() throws TableExpiredException, TableNotOpenException, UnsupportedEncodingException {

        Order order = orderService.addOrder(orderViewModelTest);
        assertThat(order).isEqualTo(orderTest);

    }

    @Test
    public void addOrderNullException() {
        try {
            Order postOrder = orderService.addOrder(new OrderViewModel());
        } catch(Exception e) {
            assertThat(e.getClass()).isEqualTo(CustomTokenExpiredException.class);
        }
    }

    //postorderfout



}