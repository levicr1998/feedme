import { async, ComponentFixture, TestBed, inject} from '@angular/core/testing';
import { RouterTestingModule} from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { Router } from '@angular/router';
import { Location} from '@angular/common';
import {TranslateModule} from '@ngx-translate/core';
import { HomeComponent } from './home/home.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { CardsOrderComponent } from './cards-order/cards-order.component';
import { MenuComponent } from './menu/menu.component';
import { OrderListComponent } from './order-list/order-list.component';
import { OrdersComponent } from './chef/orders/orders.component';
import {TestModule } from './test.module';
import { AppComponent } from './app.component';
import {TranslateLoader} from '@ngx-translate/core';
import {TranslateHttpLoader} from '@ngx-translate/http-loader';
import { DebugElement } from '@angular/core';
import { AskNameComponent } from './ask-name/ask-name.component';
import { SuccessPaymentComponent } from './success-payment/success-payment.component';
import { CancelPaymentComponent } from './cancel-payment/cancel-payment.component';

describe('AppComponent', () => {
  let app: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let de: DebugElement;
  let submitEl: DebugElement;


 

  beforeEach(async(() => {
    

    TestBed.configureTestingModule({
      declarations: [     AppComponent,
        HomeComponent,
        WelcomeComponent,
        CardsOrderComponent,
        MenuComponent,
        SuccessPaymentComponent,
        AskNameComponent,
        OrderListComponent,
        CancelPaymentComponent,
        OrdersComponent],
      imports: [ TestModule, 
        AppRoutingModule,
          RouterTestingModule.withRoutes([
            { path: 'home', component: HomeComponent },
            { path: 'menu', component: MenuComponent },
            { path: 'order-list', component: OrderListComponent },
       ])]
    })
    .compileComponents();

  
  }));
  
  beforeEach(() => {
    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
    fixture.detectChanges();
  });


  it('should create the app', () => {
    fixture = TestBed.createComponent(AppComponent);
    app = fixture.debugElement.componentInstance;
    de = fixture.debugElement;

    expect(app).toBeTruthy();
  });


  it('clicking button should go to Home page', async(inject([Router, Location], (router: Router, location: Location) => {
   
    fixture.debugElement.query(By.css(".btn_home")).nativeElement.click();
    fixture.whenStable().then(() => {
      expect(location.path()).toEqual('/home');
  });
})));

it('clicking button should go to Menu page', async(inject([Router, Location], (router: Router, location: Location) => {
   
  fixture.debugElement.query(By.css(".btn_menu")).nativeElement.click();
  fixture.whenStable().then(() => {
    expect(location.path()).toEqual('/menu');
});
})));

it('clicking button should go to Menu page', async(inject([Router, Location], (router: Router, location: Location) => {
   
  fixture.debugElement.query(By.css(".btn_menu")).nativeElement.click();
  fixture.whenStable().then(() => {
    expect(location.path()).toEqual('/menu');
});
})));

it('clicking button should go to OrderList page', async(inject([Router, Location], (router: Router, location: Location) => {
   
  fixture.debugElement.query(By.css(".btn_order-list")).nativeElement.click();
  fixture.whenStable().then(() => {
    expect(location.path()).toEqual('/order-list');
});
})));



});
