package nl.feedme.api.models;

import com.fasterxml.jackson.annotation.*;
import nl.feedme.api.ViewModel.OrderViewModel;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name="orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="order_id")
    private long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "table_id")
    private RestaurantTable restaurantTable;

    @ManyToOne
    @JsonIdentityReference(alwaysAsId = true)
    @JoinColumn(name = "consumption_id")
    private Consumption consumption;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "guest_id")
    private Guest guest;

    @Column(name = "note")
    private String note;

    @Column(name = "paymentId")
    private String paymentId;

    @Column(name = "paid")
    private boolean isPaid = false;

    @Column(name = "ready")
    private boolean isReady = false;

    @Column(name = "ordered")
    private boolean isOrdered = false;

    public Order(long id, RestaurantTable restaurantTable, Consumption consumption, String note, Guest guest) {
        this.id = id;
        this.restaurantTable = restaurantTable;
        this.consumption = consumption;
        this.note = note;
        this.guest = guest;
    }

    public Order(RestaurantTable restaurantTable, Consumption consumption, String note, Guest guest){
        this.restaurantTable = restaurantTable;
        this.consumption = consumption;
        this.note = note;
        this.guest = guest;
    }

    public Order(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public RestaurantTable getRestaurantTable() {
        return restaurantTable;
    }

    public void setRestaurantTable(RestaurantTable restaurantTable) {
        this.restaurantTable = restaurantTable;
    }

    public Consumption getConsumption() {
        return consumption;
    }

    public void setConsumption(Consumption consumption) {
        this.consumption = consumption;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPaid(boolean bool) {
        this.isPaid = bool;
    }

    public boolean isPaid() {
        return isPaid;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean ordered) {
        isOrdered = ordered;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public boolean isDone() { return this.isReady && this.isPaid; }


}
