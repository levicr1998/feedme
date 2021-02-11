import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Order } from '../classes/order';
import { Observable } from 'rxjs';
import { Consumption } from '../classes/consumption';
import { CookieService } from 'ngx-cookie-service';
import { RestaurantTableService } from './restaurant-table.service';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class OrderService {

    private orderUrl: string;

    constructor(private http: HttpClient, private tableService: RestaurantTableService) {
        this.orderUrl = environment.apiUrl + '/orders';
    }

    public getAllOrdersByTableId(tableId: number): Observable<Order[]> {
        return this.http.get<Order[]>(this.orderUrl + '/table/' + tableId);
    }

    public getAllOrdersByGuestId(guestId: number): Observable<Order[]> {
        return this.http.get<Order[]>(this.orderUrl + '/guest/' + guestId);
    }

    public getAllOrderedOrdersByGuestId(guestId: number): Observable<Order[]> {
        return this.http.get<Order[]>(this.orderUrl + '/ordered/' + guestId);
    }

    public getAllUnOrderedOrdersByGuestId(guestId: number): Observable<Order[]> {
        return this.http.get<Order[]>(this.orderUrl + '/unordered/' + guestId);
    }

    public getOrderedOrders(): Observable<Order[]> {

        return this.http.get<Order[]>(this.orderUrl + '/ordered');
    }

    public changeOrderStatusToReady(orderId: number)  {
        this.http.put(this.orderUrl + '/ready/' + orderId, null);
    }

    public postOrder(consumptionId: number): Observable<Order> {
        let tableToken = this.tableService.getToken();
        let guest = this.tableService.getGuest();
        return this.http.post<any>(this.orderUrl, { consumptionId: consumptionId, tableToken: tableToken, note: "test", guestId: guest.id });
    }

    public removeOrder(orderId: number) {
        return this.http.delete(this.orderUrl + '/' + orderId);
    }

    public setReady(orderIds: number[]) {
        console.log(orderIds);
        return this.http.put(this.orderUrl + '/ready', orderIds);
    }
}
