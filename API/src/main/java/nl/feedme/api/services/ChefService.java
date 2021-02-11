package nl.feedme.api.services;

import nl.feedme.api.exceptionHandler.ChefExceptions.ChefConflictException;
import nl.feedme.api.exceptionHandler.ChefExceptions.ChefNotFoundException;
import nl.feedme.api.models.Chef;
import nl.feedme.api.repositories.ChefRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChefService implements UserDetailsService {

    @Autowired
    private ChefRepository chefRepository;

    @Qualifier("passwordEncoder")
    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    public ChefService() {
    }

    public Chef saveChef(Chef chef) {
        if (chefRepository.findChefByUsername(chef.getUsername()) != null) {
            throw new ChefConflictException(chef.getUsername());
        }
        chef.setPassword(encoder.encode(chef.getPassword()));
        return chefRepository.save(chef);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Chef chef = chefRepository.findChefByUsername(username);
        if (chef == null) {
            throw new ChefNotFoundException(username);
        }
        return chef;
    }

    public Iterable<Chef> loadAll() {
        return chefRepository.findAll();
    }
}
