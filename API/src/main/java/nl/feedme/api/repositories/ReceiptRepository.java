package nl.feedme.api.repositories;

import nl.feedme.api.models.Receipt;
import org.springframework.data.repository.CrudRepository;

public interface ReceiptRepository extends CrudRepository<Receipt, Long> {
}
