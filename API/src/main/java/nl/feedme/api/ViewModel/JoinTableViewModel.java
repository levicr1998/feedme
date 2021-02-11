package nl.feedme.api.ViewModel;

import nl.feedme.api.models.Guest;


public class JoinTableViewModel {
    private String token;
    private Guest guest;

    public JoinTableViewModel(String token, Guest guest) {
        this.guest = guest;
        this.token = token;
    }

    public Guest getGuest() {
        return guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
