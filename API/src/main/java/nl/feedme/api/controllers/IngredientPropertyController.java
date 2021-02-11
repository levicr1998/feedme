package nl.feedme.api.controllers;

import nl.feedme.api.services.IngredientPropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class IngredientPropertyController {

    @Autowired
    private IngredientPropertyService ingredientPropertyService;

    @GetMapping("/ingredientproperties")
    public ResponseEntity getAllUniqueIngredientProperties() {
        return ResponseEntity.ok(ingredientPropertyService.getAllProperties());
    }
}
