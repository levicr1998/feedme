package nl.feedme.api.services;

import com.paypal.api.payments.Item;
import nl.feedme.api.ViewModel.OrderViewModel;
import nl.feedme.api.exceptionHandler.OrderExceptions.AddOrderException;
import nl.feedme.api.exceptionHandler.OrderExceptions.NoOrdersFoundException;
import nl.feedme.api.exceptionHandler.OrderExceptions.OrderNotFoundException;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableExpiredException;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableNotOpenException;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableOrderUserConflictException;
import nl.feedme.api.models.Consumption;
import nl.feedme.api.models.Order;
import nl.feedme.api.models.RestaurantTable;
import nl.feedme.api.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.BasicJsonParser;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Service("orderService")
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestaurantTableService restaurantTableService;

    @Autowired
    private ConsumptionService consumptionService;

    @Autowired
    private GuestService guestService;

    public OrderService() {

    }

    public List<Order> getAllOrders() {
        List<Order> allOrders = (List<Order>) orderRepository.findAll();

        if (allOrders.size() > 0) {
            return allOrders;
        }
        throw new NoOrdersFoundException();
    }

    public List<Order> getOrderedOrders() {
        List<Order> orders = orderRepository.findAllByIsOrderedAndIsReadyAndIsPaid(true, false, false);

        return orders;
    }

    public Order addOrder(OrderViewModel orderViewModel) throws TableExpiredException, TableNotOpenException, UnsupportedEncodingException {
       if(orderViewModel != null) {
           Order order = new Order(
                   restaurantTableService.getTableFromToken(orderViewModel.getTableToken()),
                   consumptionService.getConsumption(orderViewModel.getConsumptionId()),
                   orderViewModel.getNote(),
                   guestService.findById(orderViewModel.getGuestId())
           );
           if (order != null) {
               return orderRepository.save(order);
           }
       }
        throw new AddOrderException();
    }

    public List<Order>  orderOrders(String token, long user_id) throws UnsupportedEncodingException, TableNotOpenException, TableExpiredException, TableOrderUserConflictException {
        RestaurantTable table = restaurantTableService.getTableFromToken(token);
        List<Order> orders = filterOrdersOrdered(getOrdersByGuestId(user_id), false);
        for (Order order :
                orders) {
            if (order.getRestaurantTable().getId() != table.getId()) {
                throw new TableOrderUserConflictException();
            }
            order.setOrdered(true);
        }
        return (List<Order>) orderRepository.saveAll(orders);
    }

    public Order getOrder(long orderId) {
        Order order = orderRepository.findById(orderId);

        if (order != null) {
            return order;
        }
        throw new OrderNotFoundException(orderId);
    }

    public List<Order> getAllUnpaidOrders() {
        return orderRepository.findAllByIsPaid(false);
    }

    public List<Order> getUnpaidOrdersByTableId(long tableId) {
        return orderRepository.findAllByRestaurantTable_IdAndIsPaid(tableId, false);
    }

    public List<Order> getUnpaidUnreadyOrdersByTableId(long tableId) {
        return orderRepository.findAllByRestaurantTable_IdAndIsPaidAndIsReady(tableId, false, false);
    }

    public List<Order> getOrderedUnpaidOrders(long tableId) {
        return orderRepository.findAllByRestaurantTable_idAndIsOrderedAndIsPaid(tableId, true, false);
    }

    public List<Order> getOrdersByGuestId(long guestId) {
         return orderRepository.getAllByGuestId(guestId);
        }



    public Map<String, Object> deleteOrder(long orderId) {

        if (!orderRepository.existsById(orderId))
            throw new OrderNotFoundException(orderId);

        orderRepository.deleteById(orderId);
        return new BasicJsonParser().parseMap("{ \"message\": \"Order has been deleted\" }");

    }

    private List<Order> filterOrdersReady(List<Order> orders, boolean isReady) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.isReady() == isReady) {
                filtered.add(order);
            }
        }
        return filtered;
    }

    private List<Order> filterOrdersPaid(List<Order> orders, boolean isPaid) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.isPaid() == isPaid) {
                filtered.add(order);
            }
        }
        return filtered;
    }

    private Map<Consumption, Integer> getOrdersSortedByConsumption(List<Order> orders) {
        Map<Consumption, Integer> consumptionCount = new HashMap<>();
        for( Order order : orders ) {
            if (consumptionCount.containsKey(order.getConsumption())) {
                int oldValue = consumptionCount.get(order.getConsumption());
                consumptionCount.put(order.getConsumption(), ++oldValue);
            } else {
                consumptionCount.put(order.getConsumption(), 1);
            }
        }
        return consumptionCount;
    }

    public List<Item> getPaymentItems(List<Order> orders) {
        Map<Consumption, Integer> consumptionCount = this.getOrdersSortedByConsumption(orders);
        List<Item> items = new ArrayList<>();
        for (Consumption consumption : consumptionCount.keySet()) {
            items.add(new Item(consumption.getName(), String.valueOf(consumptionCount.get(consumption)), String.valueOf(consumption.getPrice()), "EUR"));
        }
        return items;

    }

    private List<Order> filterOrdersOrdered(List<Order> orders, boolean isOrdered) {
        List<Order> filtered = new ArrayList<>();
        for (Order order : orders) {
            if (order.isOrdered() == isOrdered) {
                filtered.add(order);
            }
        }
        return filtered;
    }

    public void payOrders(List<Order> orders, String paymentId) {
        for (Order order :
                orders) {
            order.setPaymentId(paymentId);
        }
        orderRepository.saveAll(orders);
    }

    public void succeedPayment(String paymentId) {
        List<Order> orders = orderRepository.findAllByPaymentId(paymentId);
        for (Order order :
                orders) {
            order.setPaid(true);
            order.setGuest(null);
        }
        orderRepository.saveAll(orders);
        restaurantTableService.closeRestaurantTable(orders.get(0).getRestaurantTable());
    }

    public List<Order> getOrderedOrdersByGuestId(long guestId) {
        return orderRepository.getAllByGuestIdAndIsOrdered(guestId, true);
    }

    public List<Order> getUnOrderedOrdersByGuestId(long guestId) {
        return orderRepository.getAllByGuestIdAndIsOrdered(guestId, false);
    }


    public void setReady(int orderId) {
        Order order = orderRepository.findById(orderId);
        order.setReady(true);
        orderRepository.save(order);
    }

    public void setReady(Long[] orderIds) {
        List<Order> orders = orderRepository.findAllByIdIn(Arrays.asList(orderIds));
        orders.forEach(o -> o.setReady(true));
        orderRepository.saveAll(orders);
    }
}
