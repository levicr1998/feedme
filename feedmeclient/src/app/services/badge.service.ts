import { Injectable } from '@angular/core';
import {  BehaviorSubject } from 'rxjs';

@Injectable()
export class BadgeService {
  private orderAmountSource = new BehaviorSubject<number>(0);

  orderAmount$ = this.orderAmountSource.asObservable();

  constructor() { }

  changeOrderAmount(amount?: number){
    this.orderAmountSource.next(amount);
  }

  getOrderAmount(): number{
    return this.orderAmountSource.getValue();
    
  }

}
