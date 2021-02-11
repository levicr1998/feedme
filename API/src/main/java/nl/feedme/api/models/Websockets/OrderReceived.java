package nl.feedme.api.models.Websockets;

public class OrderReceived {
    private int orders;
    private boolean received;
    private String errorMessage;

    public OrderReceived(int orders, boolean received, String errorMessage) {
        this.orders = orders;
        this.received = received;
        this.errorMessage = errorMessage;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }


    public boolean isReceived() {
        return received;
    }

    public void setReceived(boolean received) {
        this.received = received;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
