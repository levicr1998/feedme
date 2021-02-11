package nl.feedme.api.repositories;

import nl.feedme.api.models.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends CrudRepository<Order, Long> {

    List<Order> getAllByRestaurantTable_Id(long id);

    List<Order> findAllByRestaurantTable_IdAndIsPaid(long id, boolean paid);

    List<Order> getAllByGuestId(long id);

    Order findById(long id);

    List<Order> findAllByIsOrderedAndIsReadyAndIsPaid(boolean isOrdered, boolean isReady, boolean isPaid);

    List<Order> findAllByPaymentId(String id);

    List<Order> getAllByGuestIdAndIsOrdered(long id, boolean ordered);

    List<Order> findAllByRestaurantTable_IdAndIsPaidAndIsReady(long tableId, boolean paid, boolean ready);

    List<Order> findAllByIsPaid(boolean paid);

    List<Order> findAllByRestaurantTable_idAndIsOrderedAndIsPaid(long tableId, boolean ordered, boolean paid);

    List<Order> findAllByIdIn(List<Long> ids);
}
    