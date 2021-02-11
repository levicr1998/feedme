package nl.feedme.api.repositoryTests;


import nl.feedme.api.models.Consumption;
import nl.feedme.api.models.Food;
import nl.feedme.api.models.FoodType;
import nl.feedme.api.models.Ingredient;
import nl.feedme.api.repositories.ConsumptionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class ConsumptionRepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ConsumptionRepository consumptionRepository;


    @Test
    public void findAllConsumptions(){
        List<Ingredient> ingredients = new ArrayList<>();
      Consumption consumption = new Food( 20.15,"Biefstuk", ingredients, 2, FoodType.MAIN);

      entityManager.persist(consumption);
      entityManager.flush();
      List<Consumption> found  = (List<Consumption>) consumptionRepository.findAll();
        for (Consumption c:found) {
            assertThat(c.getName()).isEqualTo(consumption.getName());
        }
    }
}
