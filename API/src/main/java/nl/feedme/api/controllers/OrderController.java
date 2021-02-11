package nl.feedme.api.controllers;

import nl.feedme.api.ViewModel.OrderViewModel;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableExpiredException;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableNotOpenException;
import nl.feedme.api.exceptionHandler.RestaurantTableExceptions.TableOrderUserConflictException;
import nl.feedme.api.models.Order;
import nl.feedme.api.models.Websockets.OberReceived;
import nl.feedme.api.models.Websockets.OrderReceived;
import nl.feedme.api.models.Websockets.WebsocketOberRequest;
import nl.feedme.api.models.Websockets.WebsocketOrder;
import nl.feedme.api.services.ConsumptionService;
import nl.feedme.api.services.OrderService;
import nl.feedme.api.services.RestaurantTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ConsumptionService consumptionService;
    @Autowired
    private RestaurantTableService restaurantTableService;

    @GetMapping(path = "orders")
    public @ResponseBody
    ResponseEntity getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @PostMapping("orders")
    public ResponseEntity postOrder(@RequestBody OrderViewModel orderViewModel) throws TableExpiredException, TableNotOpenException, UnsupportedEncodingException {
        return ResponseEntity.ok(orderService.addOrder(orderViewModel));
    }

    @GetMapping("orders/{orderId}")
    public ResponseEntity getOrder(@PathVariable long orderId) {
        return ResponseEntity.ok(orderService.getOrder(orderId));
    }

    // Gets all the orders which have there isOrdered field to true
    @GetMapping("orders/ordered")
    public @ResponseBody ResponseEntity getOrderedOrders() {
        return ResponseEntity.ok(orderService.getOrderedOrders());
    }

    @GetMapping("orders/table/{tableId}")
    public ResponseEntity getAllOrdersByTableId(@PathVariable long tableId) {
        return ResponseEntity.ok(orderService.getOrderedUnpaidOrders(tableId));
    }

    @GetMapping("orders/table/unready/{tableId}")
    public ResponseEntity getUnreadyOrdersByTableId(@PathVariable long tableId) {
        return ResponseEntity.ok(orderService.getUnpaidUnreadyOrdersByTableId(tableId));
    }

    @GetMapping("orders/guest/{guestId}")
    public ResponseEntity getAllOrdersByGuestId(@PathVariable long guestId) {
        return ResponseEntity.ok(orderService.getOrdersByGuestId(guestId));
    }


    @DeleteMapping("orders/{orderId}")
    public ResponseEntity removeOrder(@PathVariable long orderId) {
        return ResponseEntity.ok(orderService.deleteOrder(orderId));
    }

    @GetMapping("orders/ordered/{guestId}")
    public ResponseEntity getAllOrderedOrdersByGuestId(@PathVariable long guestId) {
        return ResponseEntity.ok(orderService.getOrderedOrdersByGuestId(guestId));
    }

    @GetMapping("orders/unordered/{guestId}")
    public ResponseEntity getAllUnOrderedOrdersByGuestId(@PathVariable long guestId) {
        return ResponseEntity.ok(orderService.getUnOrderedOrdersByGuestId(guestId));
    }

    @MessageMapping("/newOrder")
    @SendTo("/topic/orderReceived")
    public OrderReceived orderReceived(WebsocketOrder order) throws UnsupportedEncodingException, TableNotOpenException, TableExpiredException, TableOrderUserConflictException {
        List<Order> orderList = orderService.orderOrders(order.getToken(), order.getUser_id());
        return new OrderReceived(orderList.size(), true, "None");
    }

    @PutMapping("orders/ready")
    public ResponseEntity setReady(@RequestBody Long[] orderIds) {
        orderService.setReady(orderIds);
        return ResponseEntity.ok(0);
    }

}
