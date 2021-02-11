package nl.feedme.api.repositoryTests;

import nl.feedme.api.models.Chef;
import nl.feedme.api.repositories.ChefRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ChefRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ChefRepository chefRepository;


@Test
    public void findChefByUsername(){
    Chef chef = new Chef("Jozef", "password");
    entityManager.persist(chef);
    entityManager.flush();
    Chef found = chefRepository.findChefByUsername(chef.getUsername());
    assertThat(found.getUsername()).isEqualTo(chef.getUsername());
}

}
