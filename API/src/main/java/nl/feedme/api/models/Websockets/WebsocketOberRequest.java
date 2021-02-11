package nl.feedme.api.models.Websockets;

public class WebsocketOberRequest {
    private int tableId;

    public WebsocketOberRequest() {
    }

    public WebsocketOberRequest(int tableId) {
        this.tableId = tableId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }
}
