package nl.feedme.api.ViewModel;

import nl.feedme.api.models.Chef;

public class ChefViewModel {
    private long id;
    private String username;
    private String password;

    public ChefViewModel(Chef chef) {
        this.id = chef.getId();
        this.username = chef.getUsername();
        this.password = chef.getPassword();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
