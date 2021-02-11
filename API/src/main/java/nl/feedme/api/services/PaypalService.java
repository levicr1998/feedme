package nl.feedme.api.services;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;
import nl.feedme.api.exceptionHandler.OrderExceptions.NoOrdersFoundException;
import nl.feedme.api.exceptionHandler.OrderExceptions.PaymentException;
import nl.feedme.api.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@PropertySource("classpath:application.properties")
@Service("paypalService")
public class PaypalService {

    @Value("${paypal.client-id}")
    private String clientId;

    @Value("${paypal.client-secret}")
    private String clientSecret;

    @Value("${front-end-url}")
    private String frontendUrl;

    @Autowired
    private OrderService orderService;

    public PaypalService() {

    }

    public String getClientId() {
        return clientId;
    }

    public Map<String, String> createPayment(long tableId){
        List<Order> orders = orderService.getUnpaidOrdersByTableId(tableId);
        if (orders.size() < 1) {
            throw new NoOrdersFoundException();
        }
        ItemList itemList = new ItemList();
        itemList.setItems(orderService.getPaymentItems(orders));
        double sum = 0;
        for (Order order :
                orders) {
            sum += order.getConsumption().getPrice();
        }

        Map<String, String> response = new HashMap<>();
        Amount amount = new Amount();
        amount.setCurrency("EUR");
        amount.setTotal(String.valueOf(sum));
        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setItemList(itemList);
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(frontendUrl + "/cancel-payment");
        redirectUrls.setReturnUrl(frontendUrl + "/success-payment");
        payment.setRedirectUrls(redirectUrls);
        Payment createdPayment;
        try {
            String redirectUrl = "";
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            createdPayment = payment.create(context);
            if(createdPayment!=null){
                orderService.payOrders(orders, createdPayment.getId());

                List<Links> links = createdPayment.getLinks();
                for (Links link:links) {
                    if(link.getRel().equals("approval_url")){
                        redirectUrl = link.getHref();
                        break;
                    }
                }
                response.put("status", "success");
                response.put("redirect_url", redirectUrl);
            }
        } catch (PayPalRESTException e) {
            throw new PaymentException(e);
        }
        return response;
    }

    public String completePayment(String paymentId, String payerId){
        Map<String, Object> response = new HashMap();
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        try {
            APIContext context = new APIContext(clientId, clientSecret, "sandbox");
            Payment createdPayment = payment.execute(context, paymentExecution);
            if(createdPayment!=null){
                orderService.succeedPayment(createdPayment.getId() );
                response.put("status", "success");
                response.put("payment", createdPayment);
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
            return e.getMessage();
        }
        return response.get("status").toString();
    }
}
