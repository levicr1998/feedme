import { TestBed, getTestBed } from '@angular/core/testing';

import { OrderService } from './order.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { Order } from '../classes/order';
import { CookieService } from 'ngx-cookie-service';
import { RouterTestingModule } from '@angular/router/testing';
import { environment } from 'src/environments/environment';
import { MatDialogModule, MatSnackBarModule } from '@angular/material';

describe('OrderService', () => {
  let injector: TestBed;
  let service: OrderService;
  let httpMock: HttpTestingController;

  const dummyOrderListResponse: Order[] = [
    { id: 1, restaurantTable: {
      id: 1,
      number:20,
      orders:[],
      open: false
    }, consumption: null, note: "Hoi", isPaid: false, ready:false, isOrdered: false},
    { id: 2, restaurantTable: {
      id: 2,
      number:20,
      orders:[],
      open: false,
    }, consumption: null, note: "Hoi", isPaid: false, ready:false, isOrdered: false},
    { id: 3, restaurantTable: {
      id: 3,
      number:20,
      orders:[],
      open: false,
    }, consumption: null, note: "Hoi", isPaid: false, ready:false, isOrdered: false},
]; 

 const dummyOrderResponse: Order = { id: 1, restaurantTable: {
  id: 1,
  number:20,
  orders:[],
  open: false,
}, consumption: null, note: "Hoi", isPaid: false, ready:false, isOrdered: false}


  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), MatDialogModule, MatSnackBarModule],
      providers: [OrderService, CookieService],
    });

    injector = getTestBed();
    service = injector.get(OrderService);
    httpMock = injector.get(HttpTestingController);

  });

  afterEach(() => {
httpMock.verify();
  });

  it('getAllOrders() should return data', () => {
    service.getAllOrdersByTableId(1).subscribe((res) => {
      expect(res).toEqual(dummyOrderListResponse);
    });

    const req = httpMock.expectOne(environment.apiUrl + '/orders/table/1');
    expect(req.request.method).toBe('GET');
    req.flush(dummyOrderListResponse);
  });

  it('postOrder() should POST and return data', () => {
    service.postOrder(1).subscribe((res) => {
      expect(res).toEqual(dummyOrderResponse);
    });

    const req = httpMock.expectOne(environment.apiUrl + '/orders');
    expect(req.request.method).toBe('POST');
    req.flush(dummyOrderResponse);
  });

  it('removeOrder() should DELELTE and return message', () => {
    service.removeOrder(1).subscribe((res) => {
      expect(res).toEqual({ msg: 'success' });
    });

    const req = httpMock.expectOne(environment.apiUrl + '/orders/1');
    expect(req.request.method).toBe('DELETE');
    req.flush({ msg: 'success' });
  });

});
