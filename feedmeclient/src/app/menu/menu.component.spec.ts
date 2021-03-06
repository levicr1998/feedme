
import { async, ComponentFixture, TestBed, inject } from '@angular/core/testing';
import { TestModule } from '../test.module';
import { AppComponent } from '../app.component';
import { HomeComponent } from '../home/home.component';
import { WelcomeComponent } from '../welcome/welcome.component';
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
import { BadgeService } from '../services/badge.service';
import { SuccessPaymentComponent } from '../success-payment/success-payment.component';
import { CancelPaymentComponent } from '../cancel-payment/cancel-payment.component';

describe('MenuComponent', () => {
  let component: MenuComponent;
  let fixture: ComponentFixture<MenuComponent>;

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
        RouterTestingModule],
        providers: [ BadgeService]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MenuComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });



});
