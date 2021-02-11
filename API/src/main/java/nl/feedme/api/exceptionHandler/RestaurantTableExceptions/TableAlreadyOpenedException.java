package nl.feedme.api.exceptionHandler.RestaurantTableExceptions;

import nl.feedme.api.models.RestaurantTable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.text.Format;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class TableAlreadyOpenedException extends IOException {
    public TableAlreadyOpenedException(RestaurantTable table) {
        super(String.format("Table %d (id: %d) is already opened.", table.getNumber(), table.getId()));
    }
}
