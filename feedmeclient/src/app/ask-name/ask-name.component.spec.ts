import { async, ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { TestModule } from '../test.module';
import { AskNameComponent } from './ask-name.component';
import { CardsOrderComponent } from '../cards-order/cards-order.component';
import { AppComponent } from '../app.component';
import { HomeComponent } from '../home/home.component';
import { WelcomeComponent } from '../welcome/welcome.component';
import { MenuComponent } from '../menu/menu.component';
import { OrderListComponent } from '../order-list/order-list.component';
import { OrdersComponent } from '../chef/orders/orders.component';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { By } from '@angular/platform-browser';
import { Location} from '@angular/common';
import { RestaurantTableService } from '../services/restaurant-table.service';
import { RestaurantTableMockService } from '../services/restaurant-tablemock.service';
import { Observable, of } from 'rxjs';
import { SuccessPaymentComponent } from '../success-payment/success-payment.component';
import { CancelPaymentComponent } from '../cancel-payment/cancel-payment.component';

describe('AskNameComponent', () => {
  let component: AskNameComponent;
  let fixture: ComponentFixture<AskNameComponent>;
  let service: RestaurantTableService;
  const responseAnswer:  Observable<any> = of({ "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ0YWJsZUlkIjo3NzcsImV4cCI6MTU3NjE1MjUwOSwib3BlbmVkQXQiOjE1NzYwNjYxMDl9._x1UMrKE27Vd3nEv_CxXvCm-Wmg5-HROk6lBSYIf17fSil6qR4qOL3JHgZS1TOECIBTz_idg4BZsFEcuJmN9_Q", "guest": { "id": 778, "name": "Levi" }});

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppComponent,
        HomeComponent,
        WelcomeComponent,
        CardsOrderComponent,
        AskNameComponent,
        SuccessPaymentComponent,
        CancelPaymentComponent,
        MenuComponent,
        OrderListComponent,
        OrdersComponent,  ],
      imports: [ 
        TestModule,  RouterTestingModule.withRoutes([
          { path: 'home', component: HomeComponent }
         ])],
         providers: [ {provide: RestaurantTableService, useClass: RestaurantTableMockService } ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AskNameComponent);
    component = fixture.componentInstance;
    service = fixture.debugElement.injector.get(RestaurantTableService);
    fixture.detectChanges();
  
    spyOn(service, 'validate').and.returnValue();
spyOn(service, 'saveGuest').and.returnValue();
 
  });

  afterEach(() => {
    service = null;
  })
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('clicking button should go to another page', async(inject([Router, Location], (router: Router, location: Location) => {
  component.nickname ="Joeri";
  spyOn(service, 'joinTable').and.returnValue(responseAnswer);
    const button = fixture.debugElement.query(By.css("#btn_jointable")).nativeElement.click();
    fixture.whenStable().then(() => {
      expect(location.path()).toEqual('/home');
  });
})));

});
