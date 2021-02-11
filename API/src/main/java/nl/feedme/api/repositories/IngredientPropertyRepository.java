package nl.feedme.api.repositories;

import nl.feedme.api.models.Chef;
import nl.feedme.api.models.Ingredient;
import nl.feedme.api.models.IngredientProperty;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientPropertyRepository extends CrudRepository<Ingredient, Long> {

}
