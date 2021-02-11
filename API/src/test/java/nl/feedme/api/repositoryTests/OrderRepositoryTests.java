package nl.feedme.api.repositoryTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.feedme.api.models.*;
import nl.feedme.api.repositories.GuestRepository;
import nl.feedme.api.repositories.OrderRepository;
import nl.feedme.api.repositories.RestaurantTableRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantTableRepository tableRepository;

    @Autowired
    private GuestRepository guestRepository;

    @Test
    public void getAllByRestaurantTable() {
        Consumption consumption1 = new Drink(2.5, "Iced Tea", new ArrayList<Ingredient>(), 0, DrinkSize.SMALL, DrinkType.COLD);
        consumption1 = entityManager.persist(consumption1);
        RestaurantTable restaurantTable1 = new RestaurantTable(new ArrayList<Guest>(), 2, null);
        restaurantTable1 = entityManager.persist(restaurantTable1);
        long tableId = restaurantTable1.getId();
        Guest guest = entityManager.persist(new Guest("Hey"));
        Order order1 = new Order(restaurantTable1, consumption1, " dffsdfd", guest);
        entityManager.persist(order1);
        entityManager.flush();

        List<Order> foundOrdersByRestaurantTable = orderRepository.getAllByRestaurantTable_Id(tableId);
        Assert.assertTrue(foundOrdersByRestaurantTable.size() > 0);

    }

    @Test
    public void findOrderById() {
        Consumption consumption1 = new Drink(2.5, "Iced Tea", new ArrayList<Ingredient>(), 0, DrinkSize.SMALL, DrinkType.COLD);
        consumption1 = entityManager.persist(consumption1);
        RestaurantTable restaurantTable1 = new RestaurantTable(new ArrayList<Guest>(), 2, null);
        restaurantTable1 = entityManager.persist(restaurantTable1);
        long tableId = restaurantTable1.getId();
        Guest guest = entityManager.persist(new Guest("Hey"));
        Order order1 = new Order(restaurantTable1, consumption1, " dffsdfd", guest);
        entityManager.persist(order1);
        entityManager.flush();

        Order order = orderRepository.findById(order1.getId());
        Assert.assertEquals(order, order1);


    }

    @Test
    public void findOrderedOrders() {
        Consumption consumption1 = new Drink(2.5, "Iced Tea", new ArrayList<Ingredient>(), 0, DrinkSize.SMALL, DrinkType.COLD);
        consumption1 = entityManager.persist(consumption1);
        RestaurantTable restaurantTable1 = new RestaurantTable(new ArrayList<Guest>(), 2, null);
        restaurantTable1 = entityManager.persist(restaurantTable1);
        Guest guest = new Guest("Hey");
        entityManager.persist(guest);
        long tableId = restaurantTable1.getId();
        Order order1 = new Order(restaurantTable1, consumption1, " dffsdfd", guest);
        Order order2 = new Order(restaurantTable1, consumption1, " dffsdfd", guest);
        order1.setOrdered(true);

        entityManager.persist(order2);
        entityManager.persist(order1);
        entityManager.flush();
        List<Order> orders = orderRepository.findAllByIsOrderedAndIsReadyAndIsPaid(true, false, false);
        Assert.assertSame(1,orders.size() );
    }

    @Test
    public void findByTableId() {
        Consumption consumption1 = new Drink(2.5, "Iced Tea", new ArrayList<Ingredient>(), 0, DrinkSize.SMALL, DrinkType.COLD);
        consumption1 = entityManager.persist(consumption1);
        RestaurantTable restaurantTable1 = new RestaurantTable(new ArrayList<Guest>(), 2, null);
        RestaurantTable restaurantTable2 = new RestaurantTable(new ArrayList<Guest>(), 3, null);
        restaurantTable1 = entityManager.persist(restaurantTable1);
        restaurantTable2 = entityManager.persist(restaurantTable2);
        Guest guest = new Guest("Hey");
        Guest guest2 = new Guest("Yo");
        entityManager.persist(guest);
        entityManager.persist(guest2);
        long tableId = restaurantTable1.getId();
        Order order1 = new Order(restaurantTable1, consumption1, " dffsdfd", guest);
        Order order2 = new Order(restaurantTable2, consumption1, " dffsdfd", guest);

        entityManager.persist(order2);
        entityManager.persist(order1);
        entityManager.flush();
        List<Order> orders = orderRepository.getAllByRestaurantTable_Id(tableId);
        Assert.assertSame(1,orders.size() );

    }



}
