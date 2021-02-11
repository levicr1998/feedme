package nl.feedme.api.repositoryTests;

import nl.feedme.api.models.Guest;
import nl.feedme.api.models.Order;
import nl.feedme.api.models.RestaurantTable;
import nl.feedme.api.models.TableIdAndNumber;
import nl.feedme.api.repositories.OrderRepository;
import nl.feedme.api.repositories.RestaurantTableRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RestaurantTableRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantTableRepository tableRepository;

    @Test
    public void customQueryProjectionTest() {
        List<Guest> guests = new ArrayList<>();
        guests.add(new Guest("Peter"));
        guests.add(new Guest("Roel"));
        for (Guest g :
                guests) {
            entityManager.persist(g);
        }

        RestaurantTable table = new RestaurantTable(guests, 2, String.valueOf(2).getBytes());
        entityManager.persist(table);
        entityManager.flush();
        Collection<RestaurantTable> tables = (Collection<RestaurantTable>) tableRepository.findAll();
        List<TableIdAndNumber> idAndNumbers = (List<TableIdAndNumber>) tableRepository.getAllNotDeep();
        TableIdAndNumber idAndNumber = idAndNumbers.get(0);
        Assert.assertTrue(tables.size() > 0);
        int number = idAndNumber.getNumber();
        Assert.assertSame(table.getNumber(), number);
    }
}
