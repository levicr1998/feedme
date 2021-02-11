package nl.feedme.api.ViewModel;

import nl.feedme.api.models.DrinkSize;
import nl.feedme.api.models.DrinkType;
import nl.feedme.api.models.Ingredient;

import java.util.List;

public class DrinkViewModel {
   private double price;
   private byte[] image;
   private String name;
   private String description;
   private List<Ingredient> ingredients;
   private int sortingId;
   private DrinkSize drinkSize;
   private DrinkType drinkType;

    public DrinkViewModel(double price, String name, List<Ingredient> ingredients, int sortingId, DrinkSize drinkSize, DrinkType drinkType, byte[] image, String description) {
        this.price = price;
        this.name = name;
        this.ingredients = ingredients;
        this.sortingId = sortingId;
        this.drinkSize = drinkSize;
        this.drinkType = drinkType;
        this.image = image;
        this.description = description;
    }

    public DrinkViewModel() {
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
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
