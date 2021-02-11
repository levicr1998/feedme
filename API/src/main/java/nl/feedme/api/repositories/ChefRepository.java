package nl.feedme.api.repositories;

import nl.feedme.api.models.Chef;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChefRepository extends CrudRepository<Chef, Long> {

    Chef findChefByUsername(String username);
}
