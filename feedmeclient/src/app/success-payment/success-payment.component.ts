import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'app-success-payment',
    templateUrl: './success-payment.component.html',
    styleUrls: ['./success-payment.component.scss']
})
export class SuccessPaymentComponent implements OnInit {

    public state: String;
    public done: Boolean = false;

    constructor(private route: ActivatedRoute, private http: HttpClient , public translate : TranslateService, private router : Router) { }

    ngOnInit() {
        this.state = "Processing...";
        this.route.queryParams.subscribe(params => {
            this.succeedPayment(params['PayerID'], params['paymentId']);
        })
    }

    succeedPayment(payerId: string, paymentId: string) {
        this.http.post(environment.apiUrl + "/tables/checkout/complete", { payerId: payerId, paymentId: paymentId }, { responseType: 'text' }).subscribe(response => {
            this.state = response;
            this.done = true;
            setTimeout(() =>{
                this.router.navigate(['/welcome']);
            }, 10000);
        });
    }
}
