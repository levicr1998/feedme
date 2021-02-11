package nl.feedme.api.services;

import nl.feedme.api.models.IngredientProperty;
import nl.feedme.api.repositories.IngredientPropertyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ingredientPropertyService")
public class IngredientPropertyService {
    @Autowired
    IngredientPropertyRepository ingredientPropertyRepository;

    public IngredientPropertyService() {

    }

    public IngredientPropertyRepository getIngredientPropertyRepository() {
        return ingredientPropertyRepository;
    }

    public IngredientProperty[] getAllProperties(){
        return  IngredientProperty.values();
    }
}
