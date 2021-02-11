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
import { RouterTestingModule } from '@angular/router/testing';
import { By } from '@angular/platform-browser';
import { Router } from '@angular/router';
import { Location} from '@angular/common';
import { AskNameComponent } from '../ask-name/ask-name.component';
import { BadgeService } from '../services/badge.service';
import { SuccessPaymentComponent } from '../success-payment/success-payment.component';
import { CancelPaymentComponent } from '../cancel-payment/cancel-payment.component';

describe('HomeComponent', () => {
  let component: HomeComponent;
  let fixture: ComponentFixture<HomeComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [     AppComponent,
        HomeComponent,
        WelcomeComponent,
        CardsOrderComponent,
        AskNameComponent,
        MenuComponent,
        SuccessPaymentComponent,
        CancelPaymentComponent,
        OrderListComponent,
        OrdersComponent],
      imports: [ TestModule, AppRoutingModule,
          RouterTestingModule.withRoutes([
        { path: 'cards-order', component: CardsOrderComponent }
       ])],
       providers: [ BadgeService ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render drinks button in a tag', async(() => {
  fixture.detectChanges();
    const compiled = fixture.debugElement.nativeElement;
    expect(compiled.querySelector('a.btn_orderdrinks .imgDrinks').src).toContain('/assets/drink.svg');
  }));

  
  it('should render food button in a tag', async(() => {
    fixture.detectChanges();
      const compiled = fixture.debugElement.nativeElement;
      expect(compiled.querySelector('a.btn_orderfood .imgFood').src).toContain('/assets/food.svg');
    }));


  it('clicking button should go to drinks page', async(inject([Router, Location], (router: Router, location: Location) => {
  
    const button = fixture.debugElement.query(By.css(".btn_orderdrinks")).nativeElement.click();
    fixture.whenStable().then(() => {
      expect(location.path()).toEqual('/cards-order?type=drinks');
  });
})));

it('clicking button should go to food page', async(inject([Router, Location], (router: Router, location: Location) => {
  
  const button = fixture.debugElement.query(By.css(".btn_orderfood")).nativeElement.click();
  fixture.whenStable().then(() => {
    expect(location.path()).toEqual('/cards-order?type=food');
});
})));
  

});