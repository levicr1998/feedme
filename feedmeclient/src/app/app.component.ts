import { Component, OnInit, NgModule, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router, NavigationEnd, Event } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { WebsocketAPIService } from './services/websocket-api.service';
import { BadgeService } from './services/badge.service';
import { Subscription } from 'rxjs';
import { OrderService } from './services/order.service';
import { Location } from '@angular/common';
import { RestaurantTableService } from './services/restaurant-table.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  providers: [BadgeService]
})
export class AppComponent implements OnInit, OnDestroy {

  title = 'feedmeclient';
  hideNavbarComponents = ["/welcome", "/chef", "/insert-name", "/success-payment","/cancel-payment"];
  currentComponent: string;
  message: string;

  subscription: Subscription;
  orderAmount: number;
  urlIncluded: boolean;

  constructor(private route: ActivatedRoute, private router: Router, public translate: TranslateService, private webSocketAPI: WebsocketAPIService, private badgeService: BadgeService, private orderService: OrderService, private location: Location, private tableService: RestaurantTableService) {
   
   
    let language = localStorage.getItem("language");

    if(language != null){
      translate.setDefaultLang(language);
      translate.use(language);
    }else{
      translate.setDefaultLang('nl');
      translate.use('nl');
    }
   
    // the lang to use, if the lang isn't available, it will use the current loader to get them
   
    this.router.events.subscribe((event: Event) => {
      if (event instanceof NavigationEnd) {
        this.currentComponent = this.route.root.firstChild.snapshot.routeConfig.component.name;
      }
    });

    badgeService.orderAmount$.subscribe(amount => {
      if (amount > 0)
        this.orderAmount = amount;
      else
        this.orderAmount = undefined;
    });
  }

  ngOnInit(): void {
    this.webSocketAPI._connect();
    this.orderService.getAllOrdersByGuestId(+localStorage.getItem('guest')).subscribe(orders => {
      this.badgeService.changeOrderAmount(orders.filter(o => o.isOrdered == false).length);
    });
    this.orderAmount = this.badgeService.getOrderAmount();
    
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  checkUrl(event){
    this.urlIncluded = this.hideNavbarComponents.some(c => this.location.path().includes(c));
    if(!this.urlIncluded){
      this.tableService.checkLogin(true);
    }
  }

}
