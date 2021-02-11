package nl.feedme.api.services;

import nl.feedme.api.exceptionHandler.GuestExceptions.GuestNotFoundException;
import nl.feedme.api.models.Guest;
import nl.feedme.api.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class GuestService {
    @Autowired
    private GuestRepository guestRepository;

    public GuestService() {

    }

    public Guest addGuest(String name) {
        Guest n = new Guest(name);
        n = guestRepository.save(n);
        return n;
    }

    public Guest updateGuest(String name, long id) {
        Guest guest = findById(id);
        guest.setName(name);
        return guestRepository.save(guest);
    }

    public List<Guest> allGuests() {
        return (List<Guest>) guestRepository.findAll();
    }

    public void removeGuest(Guest guest) {
        guestRepository.deleteById(guest.getId());
    }

    public Guest findById(long guestId) {
        Optional<Guest> optionalGuest = guestRepository.findById(guestId);
        if (optionalGuest.isPresent()) {
            return optionalGuest.get();
        }
        throw new GuestNotFoundException(guestId);
    }
}
