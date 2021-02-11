import { async, ComponentFixture, TestBed, inject, tick } from '@angular/core/testing';
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
import { AskNameComponent } from '../ask-name/ask-name.component';
import { BadgeService } from '../services/badge.service';
import { By } from '@angular/platform-browser';
import { Consumption } from '../classes/consumption';
import { browser } from 'protractor';
import { SuccessPaymentComponent } from '../success-payment/success-payment.component';
import { CancelPaymentComponent } from '../cancel-payment/cancel-payment.component';

describe('CardsOrderComponent', () => {
  let component: CardsOrderComponent;
  let fixture: ComponentFixture<CardsOrderComponent>;

  const dummyConsumptionListResponse: Consumption[] = [
    { id: 1, price: 12, foodType: 'DESSERT', image:"sddas", name:'Toetje', description:'Lekker toetje', ingredients: [], sortingValue: 1},
    { id: 1, price: 13, foodType: 'MAIN', image:"sddas", name:'Biefstukje', description:'Lekker toetje', ingredients: [], sortingValue: 2},
    { id: 1, price: 4, foodType: 'STARTER', image:"sddas", name:'Carpaccio', description:'Lekker carpaccio', ingredients: [], sortingValue: 1},
];  

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [     AppComponent,
        HomeComponent,
        WelcomeComponent,
        CardsOrderComponent,
        AskNameComponent,
        SuccessPaymentComponent,
        CancelPaymentComponent,
        MenuComponent,
        OrderListComponent,
        OrdersComponent],
      imports: [ TestModule, AppRoutingModule],
      providers: [BadgeService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CardsOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    component.tutorialAllowed = false;

    component.consumptions = dummyConsumptionListResponse;
    fixture.detectChanges();  
    
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should create Cards Order Content', () => {

    const divCardsContent = fixture.debugElement.query(By.css(".cards-order-content")).nativeElement;
    expect(divCardsContent).toBeTruthy();
  });

  it('should load a card with consumption', () => {

    const existingCard = fixture.debugElement.query(By.css("#card-0")).nativeElement;
    expect(existingCard).toBeTruthy();
  });

  it('swipeRight function works', () => {
    spyOn(component, "swipeRight");
    const card = fixture.debugElement.query(By.css('#card-0'));
    fixture.debugElement.query(By.css('#card-0')).triggerEventHandler('swiperight', {});
    expect(component.swipeRight).toHaveBeenCalled();
  });

  it('swipeLeft function works', () => {
    spyOn(component, "swipeLeft");
    const card = fixture.debugElement.query(By.css('#card-0'));
    fixture.debugElement.query(By.css('#card-0')).triggerEventHandler('swipeleft', {});
    expect(component.swipeLeft).toHaveBeenCalled();
  });


});
