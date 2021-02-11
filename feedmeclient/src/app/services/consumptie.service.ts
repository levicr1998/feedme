import { Injectable } from '@angular/core';
import { Consumption } from '../classes/consumption';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';



@Injectable({
    providedIn: 'root'
})
export class ConsumptieService {

    private consumptionUrl: string;

    constructor(private http: HttpClient) {
        this.consumptionUrl = environment.apiUrl + '/consumptions';
    }

    public getAllConsumptions() {
        return this.http.get<Consumption[]>(this.consumptionUrl + '/');
    }

    public getAllFood() {
        return this.http.get<Consumption[]>(this.consumptionUrl + '/food');
    }

    public getAllDrinks() {
        return this.http.get<Consumption[]>(this.consumptionUrl + '/drink');
    }

    public getImage(id: number) {
        return this.consumptionUrl + '/image/' + id;
    }


}
