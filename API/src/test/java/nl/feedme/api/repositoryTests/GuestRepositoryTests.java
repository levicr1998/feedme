package nl.feedme.api.repositoryTests;

import nl.feedme.api.models.Guest;
import nl.feedme.api.repositories.GuestRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;

import java.util.Optional;
import java.util.UUID;

@RunWith(SpringRunner.class)
@DataJpaTest
public class GuestRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GuestRepository guestRepository;

    @Test
    public void findGuestById() {
        Guest guest = new Guest("Lars");
        long id = entityManager.persist(guest).getId();
        entityManager.flush();
        Optional<Guest> found = guestRepository.findById(id);
        Assert.isTrue(found.isPresent());
    }
}
