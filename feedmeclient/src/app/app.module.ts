import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material.module';
import { WelcomeComponent } from './welcome/welcome.component';
import { CardsOrderComponent } from './cards-order/cards-order.component';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { ZXingScannerModule } from '@zxing/ngx-scanner';
import { MenuComponent } from './menu/menu.component';
import { HttpClientModule } from '@angular/common/http';
import { OrderListComponent } from './order-list/order-list.component';
import { MatListModule } from '@angular/material/list';
import { FormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { HttpClient } from '@angular/common/http';
import { OrdersComponent } from './chef/orders/orders.component';
import { CookieService } from 'ngx-cookie-service';
import { AskNameComponent } from './ask-name/ask-name.component';
import { OrderListDialogComponent } from './order-list/order-list-dialog/order-list-dialog.component';
import { ServiceWorkerModule } from '@angular/service-worker';
import { environment } from '../environments/environment';
import { SuccessPaymentComponent } from './success-payment/success-payment.component';
import { CancelPaymentComponent } from './cancel-payment/cancel-payment.component';
import { DialogComponent } from './services/restaurant-table.service';


export function HttpLoaderFactory(http: HttpClient) {
    return new TranslateHttpLoader(http);
}


@NgModule({
    declarations: [
        AppComponent,
        HomeComponent,
        WelcomeComponent,
        CardsOrderComponent,
        MenuComponent,
        OrderListComponent,
        OrderListDialogComponent,
        OrdersComponent,
        AskNameComponent,
        DialogComponent,
        SuccessPaymentComponent,
        CancelPaymentComponent
    ],
    imports: [

        HttpClientModule,
        TranslateModule.forRoot({
            loader: {
                provide: TranslateLoader,
                useFactory: HttpLoaderFactory,
                deps: [HttpClient]
            }
        }),
        BrowserModule,
        MatListModule,
        AppRoutingModule,
        BrowserAnimationsModule,

        MaterialModule,
        DragDropModule,
        FormsModule,
        ZXingScannerModule,
        HttpClientModule,
        ServiceWorkerModule.register('ngsw-worker.js', { enabled: environment.production })
    ],
    entryComponents: [
        OrderListDialogComponent,
        DialogComponent
    ],
    providers: [CookieService],
    bootstrap: [AppComponent]
})
export class AppModule { }
