package nl.feedme.api.ViewModel;

public class OrderViewModel {
    private int consumptionId;
    private String tableToken;
    private long guestId;
    private String note;

    public OrderViewModel(int consumptionId, String tableToken, String note) {
        this.consumptionId = consumptionId;
        this.tableToken = tableToken;
        this.note = note;
    }

    public OrderViewModel(){

    }

    public long getConsumptionId() {
        return consumptionId;
    }

    public void setConsumptionId(int consumptionId) {
        this.consumptionId = consumptionId;
    }

    public String getTableToken() {
        return tableToken;
    }

    public void setTableToken(String tableToken) {
        this.tableToken = tableToken;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getGuestId() {
        return guestId;
    }

    public void setGuestId(long guestId) {
        this.guestId = guestId;
    }
}
