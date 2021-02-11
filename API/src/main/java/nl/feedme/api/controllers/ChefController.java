package nl.feedme.api.controllers;

import nl.feedme.api.models.Chef;
import nl.feedme.api.services.ChefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/api")
public class ChefController {
    @Autowired
    private ChefService chefService;

    @GetMapping("/chefs")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(chefService.loadAll());
    }

    @PostMapping("/chefs")
    public ResponseEntity registerChef(@RequestBody Chef chef) {
        return ResponseEntity.ok(chefService.saveChef(chef));
    }

    //@GetMapping("/chefs/{id}")
    //public ResponseEntity getChefById(@PathVariable long id) {
        //return ResponseEntity.noContent().build();
    //}

    @GetMapping("/chefs/{username}")
    public ResponseEntity getChefByUsername(@PathVariable String username) {
        return ResponseEntity.ok(chefService.loadUserByUsername(username));
    }
}
