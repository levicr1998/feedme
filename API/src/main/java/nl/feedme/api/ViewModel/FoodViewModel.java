package nl.feedme.api.ViewModel;

import nl.feedme.api.models.FoodType;
import nl.feedme.api.models.Ingredient;

import java.util.List;

public class FoodViewModel {
    private FoodType foodType;
    private double price;
    private byte[] image;
    private String description;
    private String name;
    private  List<Ingredient> ingredients;
    private int sortingId;

    public FoodViewModel() {
    }

    public FoodViewModel(FoodType foodType,double price, String name, List<Ingredient> ingredients, int sortingId, byte[] image, String description) {
        this.foodType = foodType;
        this.price = price;
        this.name = name;
        this.ingredients = ingredients;
        this.sortingId = sortingId;
        this.image = image;
        this.description = description;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public int getSortingId() {
        return sortingId;
    }

    public void setSortingId(int sortingId) {
        this.sortingId = sortingId;
    }
}
