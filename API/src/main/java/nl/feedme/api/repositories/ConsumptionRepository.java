package nl.feedme.api.repositories;

import nl.feedme.api.models.Consumption;
import nl.feedme.api.models.Ingredient;
import nl.feedme.api.models.IngredientProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface ConsumptionRepository extends CrudRepository<Consumption, Long> {

    List<Consumption> findAllByIngredientsIsNot(List<Ingredient> ingredients);
}
