package nl.feedme.api.ViewModel;

public class PaypalSuccess {
    private String paymentId;
    private String payerId;

    public PaypalSuccess(String payerId, String paymentId) {
        this.payerId = payerId;
        this.paymentId = paymentId;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }
}
