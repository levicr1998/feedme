import { Component, OnInit, ChangeDetectorRef, HostBinding, Renderer2, HostListener } from '@angular/core';
import { WebsocketAPIService } from 'src/app/services/websocket-api.service';
import { Order } from 'src/app/classes/order';
import { OrderService } from 'src/app/services/order.service';
import { RestaurantTableService } from 'src/app/services/restaurant-table.service';
import { RestaurantTable } from 'src/app/classes/restaurantTable';
import { trigger, style, state, transition, animate, useAnimation } from '@angular/animations';

@Component({
    selector: 'app-orders',
    templateUrl: './orders.component.html',
    styleUrls: ['./orders.component.scss'],
    animations: [
        trigger('readyAmountAnimator', [
            transition('void <=> *', []),
            state('closed', style({
                width: '55px',
                height: '40px'
            })),
            state('opened', style({
                width: '{{readyAmountWidth}}px',
                height: '40px'
            }), { params: { readyAmountWidth: 120 } }),
            transition('* => opened', [
                animate('0.4s ease-out')
            ]),
            transition('* => closed', [
                animate('0.2s ease-in')
            ])
        ])
    ]
})
export class OrdersComponent implements OnInit {

    orders: Order[] = [];
    tables: RestaurantTableOrder[] = [];
    oberNeeded: Number[] = [];
    animationStates: any[][] = [[]];
    Arr = Array;
    amount: number = 5;


    constructor(private webSocketAPI: WebsocketAPIService, private orderService: OrderService, private cdRef: ChangeDetectorRef, private tableService: RestaurantTableService, private renderer: Renderer2) { }

    ngOnInit() {
        this.webSocketAPI._connect(this);
        this.handleMessage();

        this.renderer.listen('window', 'click', (event: Event) => {
            this.clearNotifications();
        });
    }

    disconnect() {
        this.webSocketAPI._disconnect();
    }

    handleMessage() {
        this.tableService.getTablesWithOrders().subscribe((tables) => {
            if (tables != null)
                this.getOrders(tables);
        });

    }

    handleOberRequest(table: any) {
        let doesTableExist = false;
        let oberAlreadyCalled = false;

        this.tables.forEach(t => {
            if (t.id == table.tableId) {
                doesTableExist = true;
            }
        })

        this.oberNeeded.forEach(o => {
            if (o == table.tableId) {
                oberAlreadyCalled = true;
            }
        })

        if (!oberAlreadyCalled)
            this.oberNeeded.push(table.tableId);

        if (!doesTableExist){
            this.tables.push(new RestaurantTableOrder(table.tableId, table.tableNumber, []));
        }
    }

    checkOber(tableNumber: Number) {

        if (this.oberNeeded.find(o => o == tableNumber)) {
            return true;
        }
        return false;
    }

    orderTables(tables: RestaurantTable[]) {
        for (let i = 0; i < tables.length; i++) {
            const table = tables[i];
            if (!this.tables.some(t => t.id == table.id)) {
                this.tables.push(new RestaurantTableOrder(table.id, table.number, table.orders));
                this.animationStates.push([]);
            } else {
                this.tables.find(t => t.id == table.id).pushAll(table.orders);
            }
        }
    }

    getOrders(tables: RestaurantTable[]) {
        if (tables.length > 0) {
            this.orderTables(tables);
            this.cdRef.detectChanges();
        }
    }

    getReadyAmountAnimatorOpen(size: number) {
        return { value: 'opened', params: { readyAmountWidth: size } }
    }

    getReadyAmountAnimatorClosed() {
        return { value: 'closed' }
    }

    onReadyClick(tableIndex: number, consumptionIndex: number) {
        this.clearNotifications();

        let amount = this.tables[tableIndex].consumptions[consumptionIndex].orders.length;
        if (amount > 1) {
            document.getElementById('checkbox' + tableIndex + '-' + consumptionIndex).style.width = (amount + 1) * 40 + "px";
            this.animationStates[tableIndex][consumptionIndex] = this.getReadyAmountAnimatorOpen((amount + 1) * 40);
        }
        else {
            this.orderReady(tableIndex, consumptionIndex, amount);
        }
    }

    orderReady(tableIndex: number, consumptionIndex: number, amount: number) {
        this.animationStates[tableIndex][consumptionIndex] = this.getReadyAmountAnimatorClosed();

        let readyOrders = this.tables[tableIndex].consumptions[consumptionIndex].orders.splice(0, amount);
        readyOrders.forEach(o => o.ready = true);
        this.tables[tableIndex].pushAll(readyOrders);
        this.orderService.setReady(readyOrders.map(o => o.id)).subscribe();
    }

    clearNotifications() {
        this.tables.forEach(t => {
            t.consumptions.forEach(c => {
                c.new = false;
            })
        });
        this.animationStates.forEach(animationState => {
            for (let i = 0; i < animationState.length; i++) {
                animationState[i] = this.getReadyAmountAnimatorClosed();
            }
        });
    }
}

class RestaurantTableOrder {
    consumptions: SortConsumption[] = [];
    readyConsumptions: SortConsumption[] = [];
    id: number;
    number: number;

    constructor(id: number, number: number, orders: Order[]) {
        this.id = id;
        this.number = number;
        orders.forEach(o => this.push(o));
    }

    push(order: Order) {
        if (!order.ready) {
            let added: boolean = false;
            this.consumptions.forEach(c => {
                if (c.id == order.consumption.id) {
                    c.push(order);
                    added = true;
                }
            });

            if (!added) this.consumptions.push(new SortConsumption(order));
        } else {
            let added: boolean = false;
            this.readyConsumptions.forEach(c => {
                if (c.id == order.consumption.id) {
                    c.push(order);
                    added = true;
                }
            });

            if (!added) this.readyConsumptions.push(new SortConsumption(order));
        }
    }

    pushAll(orders: Order[]) {
        orders.forEach(o => this.push(o));
    }
}

class SortConsumption {
    orders: Order[];
    id: number;
    name: string;
    new: boolean;

    constructor(order: Order) {
        this.orders = [order];
        this.name = order.consumption.name;
        this.id = order.consumption.id;
        this.new = true;
    }

    push(order: Order) {
        if (!this.orders.some(o => o.id == order.id)) {
            this.orders.push(order);
            this.new = true;
        }
    }
}