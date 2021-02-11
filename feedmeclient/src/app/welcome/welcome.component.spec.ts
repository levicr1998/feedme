import { async, ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { TestModule } from '../test.module';
import { AppComponent } from '../app.component';
import { HomeComponent } from '../home/home.component';
import { WelcomeComponent } from './welcome.component';
import { CardsOrderComponent } from '../cards-order/cards-order.component';
import { MenuComponent } from '../menu/menu.component';
import { OrderListComponent } from '../order-list/order-list.component';
import { OrdersComponent } from '../chef/orders/orders.component';
import { AppRoutingModule } from '../app-routing.module';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { Router } from '@angular/router';
import { Location } from '@angular/common';
import { AskNameComponent } from '../ask-name/ask-name.component';
import { SuccessPaymentComponent } from '../success-payment/success-payment.component';
import { CancelPaymentComponent } from '../cancel-payment/cancel-payment.component';

describe('WelcomeComponent', () => {
  let component: WelcomeComponent;
  let fixture: ComponentFixture<WelcomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AppComponent,
        HomeComponent,
        WelcomeComponent,
        CardsOrderComponent,
        MenuComponent,
        AskNameComponent,
        SuccessPaymentComponent,
        CancelPaymentComponent,
        OrderListComponent,
        OrdersComponent],
      imports: [TestModule,
        AppRoutingModule,
        RouterTestingModule
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(WelcomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });



});


