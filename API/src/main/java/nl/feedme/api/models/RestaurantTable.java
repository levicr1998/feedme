package nl.feedme.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "restaurant_table")
public class RestaurantTable implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="table_id")
    private long id;

    @JsonIgnore
    @OneToMany(
            mappedBy = "tables",
            cascade = CascadeType.REMOVE,
            orphanRemoval = true
    )
    private List<Guest> guests = new ArrayList<>();

    @JsonManagedReference
    @OneToMany(mappedBy = "restaurantTable")
    private List<Order> orders;

    @Column(name="number", unique=true)
    private Integer number;

    @Lob
    @JsonIgnore
    @Column(name="private_qr_code", length = 100000)
    private byte[] privateQrCode;

    @JsonIgnore
    @Column(name="secretKey")
    private String secretKey;

    @Column(name="lastOpenend")
    private Date lastOpened;
//
//    @Column(name = "round")
//    private int round;

    public RestaurantTable(List<Guest> guests, int number, byte[] privateQrCode) {
        this.guests = guests;
        this.number = number;
        this.privateQrCode = privateQrCode;
    }

    public RestaurantTable(int tablenr){
        this.number = tablenr;
    }

    public RestaurantTable(){

    }

    public RestaurantTable(TableIdAndNumber tableIdAndNumber, List<Order> orders) {
        this.id = tableIdAndNumber.getTable_id();
        this.number = tableIdAndNumber.getNumber();
        this.orders = orders;
    }

    public List<Guest> getGuests() {
        return guests;
    }

    public void setGuests(List<Guest> guests) {
        this.guests = guests;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public byte[] getPrivateQrCode() {
        return privateQrCode;
    }

    public void setPrivateQrCode(byte[] privateQrCode) {
        this.privateQrCode = privateQrCode;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public boolean isOpen() {
        return secretKey != null;
    }

    public RestaurantTable addGuest(Guest guest) {
        guest.setTable(this);
        this.guests.add(guest);
        return this;
    }

    public void setLastOpened(Date date) {
        this.lastOpened = date;
    }

    public void removeGuestFromTable(Guest guest) {
        this.guests.remove(guest);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    //    public int getRound() {
//        return round;
//    }
//
//    public void setRound(int round) {
//        this.round = round;
//    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != RestaurantTable.class) {
            return false;
        }
        RestaurantTable object = (RestaurantTable) obj;
        return object.getId() == this.getId();
    }
}
