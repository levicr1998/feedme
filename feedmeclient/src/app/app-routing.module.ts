import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { WelcomeComponent } from './welcome/welcome.component';
import { CardsOrderComponent } from './cards-order/cards-order.component';
import { MenuComponent } from './menu/menu.component';
import { OrderListComponent } from './order-list/order-list.component';
import { OrdersComponent } from './chef/orders/orders.component';
import { AskNameComponent } from './ask-name/ask-name.component';
import { SuccessPaymentComponent } from './success-payment/success-payment.component';
import { CancelPaymentComponent } from './cancel-payment/cancel-payment.component';


const routes: Routes = [
    { path: 'home', component: HomeComponent },
    { path: 'insert-name', component: AskNameComponent },
    { path: 'welcome', component: WelcomeComponent },
    { path: 'menu', component: MenuComponent },
    { path: 'cards-order', component: CardsOrderComponent },
    { path: 'order-list', component: OrderListComponent },
    { path: 'chef/orders', component: OrdersComponent },
    { path: 'success-payment', component: SuccessPaymentComponent },
    { path: 'cancel-payment', component: CancelPaymentComponent },
    { path: '', redirectTo: '/welcome', pathMatch: 'full' }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
