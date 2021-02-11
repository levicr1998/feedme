package nl.feedme.api.models.Websockets;

import com.fasterxml.jackson.annotation.JsonFormat;
import nl.feedme.api.models.Order;

import java.util.List;

public class WebsocketOrder {

    private List<Order> orders;
    private String token;
    private long user_id;

    public WebsocketOrder(){

    }

    public List<Order> getOrders() {

        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
