import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { Order } from '../classes/order';
import { OrderService } from '../services/order.service';
import { RestaurantTableService } from '../services/restaurant-table.service';
import { MatDialog, MatDialogRef } from '@angular/material/dialog';
import { OrderListDialogComponent } from './order-list-dialog/order-list-dialog.component';
import { WebsocketAPIService } from '../services/websocket-api.service';
import { BadgeService } from '../services/badge.service';
import { HttpClient } from '@angular/common/http';
import { element } from 'protractor';
import { MatSnackBar } from '@angular/material';
import { environment } from 'src/environments/environment';

@Component({
    selector: 'app-order-list',
    templateUrl: './order-list.component.html',
    styleUrls: ['./order-list.component.scss']
})
export class OrderListComponent implements OnInit {
    orderList: Order[] = [];
    alreadyOrderedList: Order[] = [];
    totalAmount: number;
    totalInOrder: number;

    constructor(private webSocketServer: WebsocketAPIService, public dialog: MatDialog, private orderService: OrderService,
        private cdRef: ChangeDetectorRef, private tableService: RestaurantTableService, private websocketService: WebsocketAPIService,
        private badgeService: BadgeService, private httpClient: HttpClient, private _snackBar: MatSnackBar) { }

    ngOnInit() {
        this.getOrders();
    }

    openDialog() {
        let dialogRef = this.dialog.open(OrderListDialogComponent, {
            width: '225px',
            data: "ss"
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result == "confirmation") {
                //console.log("Implement WebsocketAPi in the constructor and then use the _send method to send the variable orderlist into it");
                this.websocketService._send(this.tableService.getToken(), this.tableService.getGuest().id);
                this.badgeService.changeOrderAmount(0);
                setTimeout(() => {
                    this.getOrders();
                },
                    2000);

            }
        });
    }

    requestOber(): void {
        this.tableService.getTableByToken().subscribe(o => {
            this.webSocketServer._sendOberRequest(o.id);
        })
    }

    deleteOrder(id: number) {
        this.orderService.removeOrder(id).subscribe(result => {
            this.orderList.splice(this.orderList.findIndex(o => o.id == id), 1);
            this.badgeService.changeOrderAmount(this.badgeService.getOrderAmount() - 1);
            this.cdRef.detectChanges();
        }, error => console.log(error.error.message));
    }

    payOrders() {
        this.httpClient.post<CheckoutPaypal>(environment.apiUrl + "/tables/checkout", this.tableService.getToken()).subscribe(response => {
            window.location.href = (response.redirect_url);
        }, error => {
            this.openSnackBar(error.error.message);
            console.log(error);
        });
    }

    getOrders() {
        this.orderService.getAllUnOrderedOrdersByGuestId(+this.tableService.getGuest().id).subscribe(data => {
            this.orderList = data;
            this.totalInOrder = this.orderList.map(element => element.consumption.price).reduce((a, b) => a + b, 0);
        });
        this.orderService.getAllOrderedOrdersByGuestId(+this.tableService.getGuest().id).subscribe(data => {
            this.alreadyOrderedList = data;
        })
        this.tableService.getTableByToken().subscribe(t => {
            this.orderService.getAllOrdersByTableId(t.id).subscribe(orders => this.totalAmount = orders.map(element => element.consumption.price).reduce((a, b) => a + b, 0));
        });
    }

    openSnackBar(message: string) {
        this._snackBar.open(message, null, {
            duration: 2000,
        });
    }
}
class CheckoutPaypal {
    status: string;
    redirect_url: string;

    constructor(parameters) {
    }
}