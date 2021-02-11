package nl.feedme.api.controllers;

import nl.feedme.api.ViewModel.DrinkViewModel;
import nl.feedme.api.ViewModel.FoodViewModel;
import nl.feedme.api.services.ConsumptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class ConsumptionController {
    @Autowired
    private ConsumptionService consumptionService;

    @GetMapping(path = "consumptions")
    public @ResponseBody ResponseEntity getAllConsumptions () {
        return ResponseEntity.ok(consumptionService.getAllConsumption());
    }

    @GetMapping(path = "consumptions/drink")
    public @ResponseBody ResponseEntity getAllDrinks() {
        return ResponseEntity.ok(consumptionService.getAllDrinks());
    }

    @GetMapping(path = "consumptions/food")
    public @ResponseBody ResponseEntity getAllFood() {
        return ResponseEntity.ok(consumptionService.getAllFood());
    }

    @PostMapping("consumptions/food")
    public ResponseEntity postFood(@RequestBody FoodViewModel foodViewModel) {
        return ResponseEntity.ok(consumptionService.addFood(foodViewModel));

    }
    @PostMapping("consumptions/drink")
    public ResponseEntity postDrink(@RequestBody DrinkViewModel drinkViewModel) {
        return ResponseEntity.ok(consumptionService.addDrink(drinkViewModel));
    }
    @GetMapping("consumptions/{consumptionId}")
    public ResponseEntity getConsumption(@PathVariable long consumptionId) {
        return ResponseEntity.ok(consumptionService.getConsumption(consumptionId));
    }

    @DeleteMapping("consumptions/{consumptionId}")
    public ResponseEntity removeConsumption(@PathVariable long consumptionId){
        return ResponseEntity.ok(consumptionService.deleteConsumption(consumptionId));
    }

    @GetMapping(value = "consumptions/image/{id}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity getImage(@PathVariable long id) {
        return ResponseEntity.ok(consumptionService.getConsumption(id).getImage());
    }
}
