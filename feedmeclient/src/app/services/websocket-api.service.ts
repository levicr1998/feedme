import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { AppComponent } from '../app.component';
import { OrdersComponent } from '../chef/orders/orders.component';
import { Order } from '../classes/order';
import { OrderListComponent } from '../order-list/order-list.component';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class WebsocketAPIService {

    webSocketEndPoint: string = environment.websocketUrl;
    stompClient: any;

    ordersComponent: OrdersComponent;
    constructor() {
    }

    _connect(ordersComponent?: OrdersComponent) {
        this.ordersComponent = ordersComponent;

        console.log("Initialize WebSocket Connection");
        var socket = new SockJS(this.webSocketEndPoint + 'stomp');
        this.stompClient = Stomp.over(socket);
        this.stompClient.connect({}, frame => {
            console.log('Connected: ' + frame);
            this.stompClient.subscribe("/topic/orderReceived", sdkEvent => {
                console.log(sdkEvent);
                this.onMessageReceived(sdkEvent.body, "orderReceived");
            });
            this.stompClient.subscribe("/topic/oberReceived", sdkEvent => {
                console.log(sdkEvent);
                this.onMessageReceived(sdkEvent.body, "oberReceived");
            });
        });
    }

    _disconnect() {
        if (this.stompClient !== null) {
            this.stompClient.disconnect();
        }
        console.log("Disconnected");
    }

    // on error, schedule a reconnection attempt
    errorCallBack(error) {
        console.log("errorCallBack -> " + error)
        setTimeout(() => {
            this._connect(this.ordersComponent);
        }, 5000);
    }

    /**
    * Send message to sever via web socket
    * @param {*} message 
    */
    _send(token: String, user_id: number) {
        this.stompClient.send("/app/newOrder", {}, JSON.stringify({ token: token, user_id: user_id }));
    }

    _sendOberRequest(token: number) {
        console.log(token);
        this.stompClient.send("/app/newOberRequest", {}, JSON.stringify({ tableId: token }));
    }

    onMessageReceived(message, type) {
        console.log("Message Recieved from Server :: " + message);
        if (this.ordersComponent) {
            if (type == "orderReceived") {
                this.ordersComponent.handleMessage();
            } else if (type == "oberReceived") {
                console.log(message);
                this.ordersComponent.handleOberRequest(JSON.parse(message));
            }
        }
    }
}
