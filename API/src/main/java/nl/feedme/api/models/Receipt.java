package nl.feedme.api.models;

import nl.feedme.api.models.Consumption;
import nl.feedme.api.models.Order;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
public class Receipt {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany
    private Collection<Order> orders;

    private Date checkoutDate;

    private double price;

    public Receipt(Collection<Order> orders) {
        this.checkoutDate = new Date();
        for (Order order :
                orders) {
            price += order.getConsumption().getPrice();
        }
        this.orders = orders;
    }

    public Collection<Order> getOrders() {
        return orders;
    }

    public void setOrders(Collection<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(Order order) {
        price += order.getConsumption().getPrice();
        orders.add(order);
    }

    public Date getCheckoutDate() {
        return checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String checkout() {
        StringBuilder sb = new StringBuilder();
        for (Order order :
                orders) {
            sb.append(order.getConsumption());
            sb.append("\n");
        }
        sb
                .append("Total: ")
                .append(price)
                .append("\n")
        .append(checkoutDate);
        return sb.toString();
    }
}
