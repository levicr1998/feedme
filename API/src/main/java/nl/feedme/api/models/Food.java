package nl.feedme.api.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Entity
public class Food extends Consumption {


    @Column
    @Enumerated(EnumType.STRING)
    private FoodType foodType;


    public Food(int consumptionid, double price, String name, List<Ingredient> ingredients, int sortingId, FoodType foodType, byte[] image, String description) {
        super(consumptionid, price, name, ingredients, sortingId, image, description);
        this.foodType = foodType;
    }

    public Food(int consumptionid, double price, String name, List<Ingredient> ingredients, int sortingId, FoodType foodType) {
        super(consumptionid, price, name, ingredients, sortingId);
        this.foodType = foodType;
    }
    public Food(double price, String name, List<Ingredient> ingredients, int sortingId, FoodType foodType, byte[] image, String description) {
        super(price, name, ingredients, sortingId, image, description);
        this.foodType = foodType;
    }
    public Food(double price, String name, List<Ingredient> ingredients, int sortingId, FoodType foodType) {
        super(price, name, ingredients, sortingId);
        this.foodType = foodType;
    }


    public Food() {

    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }


}