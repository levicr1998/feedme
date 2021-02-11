package nl.feedme.api.models.Websockets;

public class OberReceived {
    private long tableId;
    private int tableNumber;
    private String userId;

    public OberReceived() {
    }

    public OberReceived(long tableId, int tableNumber, String userId) {
        this.tableId = tableId;
        this.tableNumber = tableNumber;
        this.userId = userId;
    }

    public long getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
