package nl.feedme.api.repositories;

import nl.feedme.api.models.RestaurantTable;
import nl.feedme.api.models.TableIdAndNumber;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RestaurantTableRepository  extends CrudRepository<RestaurantTable, Long> {

    @Query(value = "select t.table_id, t.number from restaurant_table t", nativeQuery = true)
    List<TableIdAndNumber> getAllNotDeep();
}
