package nl.feedme.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@JsonIgnoreProperties({"consumption"})
public class Ingredient implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="ingredient_id")
    private long id;

    @Column(name="name")
    private String name;

    @JsonIgnore
    @ManyToMany
    @JoinColumn(name="consumptions")
    private List<Consumption> consumptions;

    @ElementCollection(targetClass = IngredientProperty.class, fetch = FetchType.EAGER)
    @JoinTable(name = "tblIngredientProperties", joinColumns = @JoinColumn(name = "ingredient_id"))
    @Column(name = "properties", nullable = false)
    @Enumerated(EnumType.STRING)
    private List<IngredientProperty> ingredientPropertyList;

    public Ingredient(String name, List<IngredientProperty> ingredientPropertyList) {
        this.name = name;
        this.ingredientPropertyList = ingredientPropertyList;
    }

    public Ingredient(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientProperty> getIngredientPropertyList() {
        return ingredientPropertyList;
    }

    public void setIngredientPropertyList(List<IngredientProperty> ingredientPropertyList) {
        this.ingredientPropertyList = ingredientPropertyList;
    }

    @JsonIgnore
    public List<Consumption> getConsumptions() {
        return consumptions;
    }

    public void setConsumptions(List<Consumption> consumptions) {
        this.consumptions = consumptions;
    }
}
