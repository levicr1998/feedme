package nl.feedme.api.repositories;

import nl.feedme.api.models.Consumption;
import nl.feedme.api.models.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

}
