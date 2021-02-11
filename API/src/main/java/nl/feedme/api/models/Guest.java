package nl.feedme.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.boot.web.servlet.server.Session;

import javax.persistence.*;
import javax.servlet.http.HttpSession;
import java.io.Serializable;

@Table(name="guest")
@Entity
public class Guest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="user_id")
    protected long id;

    @Column(name="name")
    protected String name;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id")
    protected RestaurantTable tables;

    public Guest(String name) {
        this.name = name;
    }

    public Guest(){

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

    @Override
    public boolean equals(Object obj) {
        Guest toCompare = (Guest) obj;
        return toCompare.getName().equals(this.getName());
    }

    public void setTable(RestaurantTable restaurantTable) {
        this.tables = restaurantTable;
    }
}
