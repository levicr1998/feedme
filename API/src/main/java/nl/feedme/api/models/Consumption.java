package nl.feedme.api.models;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Drink.class, name = "Drink"),
        @JsonSubTypes.Type(value = Food.class, name = "Food")
})
@Entity
public abstract class Consumption implements Comparable<Consumption>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "consumption_id")
    protected long id;

    @Column(name = "price")
    private double price;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "ingredient_id")
    private List<Ingredient> ingredients;

    @Column(name = "sorting_value")
    private int sortingValue;

    @JsonIgnore
    @Column(name = "image", columnDefinition = "longblob")
    private byte[] image;

    public Consumption(long id, double price, String name, List<Ingredient> ingredients, int sortingId, byte[] image,String description) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.ingredients = ingredients;
        this.sortingValue = sortingId;
        this.image = image;
        this.description = description;
    }
    public Consumption(long id, double price, String name, List<Ingredient> ingredients, int sortingId) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.ingredients = ingredients;
        this.sortingValue = sortingId;
    }

    public Consumption( double price, String name, List<Ingredient> ingredients, int sortingId, byte[] image, String description) {
        this.price = price;
        this.name = name;
        this.ingredients = ingredients;
        this.sortingValue = sortingId;
        this.image = image;
        this.description = description;
    }
    public Consumption( double price, String name, List<Ingredient> ingredients, int sortingId) {
        this.price = price;
        this.name = name;
        this.ingredients = ingredients;
        this.sortingValue = sortingId;
    }

    public Consumption(int id){
        this.id = id;
    }

    public Consumption() {

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSortingValue() {
        return sortingValue;
    }

    public void setSortingValue(int sortingValue) {
        this.sortingValue = sortingValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int compareTo(Consumption object) {
        //TODO: this function
        return 1;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return this.getName() + "(" + getPrice() + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj.getClass() != Consumption.class) {
            return false;
        }
        Order object = (Order) obj;

        return object.getId() == this.getId();
    }
}