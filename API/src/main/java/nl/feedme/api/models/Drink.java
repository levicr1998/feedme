package nl.feedme.api.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Entity
public class Drink extends Consumption {

    @Column
    @Enumerated(EnumType.STRING)
    private DrinkSize drinkSize;

    @Column
    @Enumerated(EnumType.STRING)
    private DrinkType drinkType;


    public Drink(int id, double price, String name, List<Ingredient> ingredients, int sortingId, DrinkSize drinkSize, DrinkType drinkType, byte[] image, String description) {
        super(id, price, name, ingredients, sortingId, image, description);
        this.drinkSize = drinkSize;
        this.drinkType = drinkType;
    }
    public Drink(int id, double price, String name, List<Ingredient> ingredients, int sortingId, DrinkSize drinkSize, DrinkType drinkType) {
        super(id, price, name, ingredients, sortingId);
        this.drinkSize = drinkSize;
        this.drinkType = drinkType;
    }
    public Drink(double price, String name, List<Ingredient> ingredients, int sortingId, DrinkSize drinkSize, DrinkType drinkType, byte[] image,String description) {
        super(price, name, ingredients, sortingId, image, description);
        this.drinkSize = drinkSize;
        this.drinkType = drinkType;
    }
    public Drink(double price, String name, List<Ingredient> ingredients, int sortingId, DrinkSize drinkSize, DrinkType drinkType) {
        super(price, name, ingredients, sortingId);
        this.drinkSize = drinkSize;
        this.drinkType = drinkType;
    }

    public Drink(){

    }

    public DrinkSize getDrinkSize() {
        return drinkSize;
    }

    public void setDrinkSize(DrinkSize drinkSize) {
        this.drinkSize = drinkSize;
    }

    public DrinkType getDrinkType() {
        return drinkType;
    }

    public void setDrinkType(DrinkType drinkType) {
        this.drinkType = drinkType;
    }






}

