import { async, ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { TestModule } from '../../test.module';
import { AppComponent } from '../../app.component';
import { HomeComponent } from '../../home/home.component';
import { WelcomeComponent } from '../../welcome/welcome.component';
import { CardsOrderComponent } from '../../cards-order/cards-order.component';
import { MenuComponent } from '../../menu/menu.component';
import { OrderListComponent } from '../../order-list/order-list.component';
import { OrdersComponent } from '../../chef/orders/orders.component';
import { AppRoutingModule } from '../../app-routing.module';
import { RouterTestingModule } from '@angular/router/testing';
import { AskNameComponent } from 'src/app/ask-name/ask-name.component';
import { SuccessPaymentComponent } from 'src/app/success-payment/success-payment.component';
import { CancelPaymentComponent } from 'src/app/cancel-payment/cancel-payment.component';


describe('OrdersComponent', () => {
  let component: OrdersComponent;
  let fixture: ComponentFixture<OrdersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AppComponent,
        HomeComponent,
        WelcomeComponent,
        CardsOrderComponent,
        AskNameComponent,
        SuccessPaymentComponent,
        CancelPaymentComponent,
        MenuComponent,
        OrderListComponent,
        OrdersComponent],
      imports: [TestModule, AppRoutingModule,
        RouterTestingModule]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OrdersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

});
