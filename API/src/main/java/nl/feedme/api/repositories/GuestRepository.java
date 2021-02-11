package nl.feedme.api.repositories;

import nl.feedme.api.models.Guest;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<Guest, Long> {
}
