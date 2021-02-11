import { Component, OnInit } from '@angular/core';
import { ConsumptieService } from '../services/consumptie.service';
import { Consumption } from '../classes/consumption';
import { DomSanitizer } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { PropertiesService } from '../services/properties.service'
import { element } from 'protractor';
import { OrderService } from '../services/order.service';
import { MatSnackBar } from '@angular/material';
import { BadgeService } from '../services/badge.service';

@Component({
  selector: 'app-menu',
  templateUrl: './menu.component.html',
  styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit {
  constructor(private _snackBar: MatSnackBar, private consumptionService: ConsumptieService, private propertieService: PropertiesService,
    public _sanitizer: DomSanitizer, private orderService: OrderService, private badgeService: BadgeService) { }

  searchValue: string;
  consumptions: Consumption[];
  consumptionSorting: number;
  consumptionType: string;
  filteredConsumptions: Consumption[];
  consumptionNames: string[];
  properties: string[];
  includeProperties: string[];

  filterHidden: boolean = true;

  ngOnInit() {
    this.consumptionService.getAllConsumptions().subscribe(data => {
      this.consumptions = data;
      this.consumptionNames = this.consumptions.map(c => c.name);
      this.filteredConsumptions = this.consumptions;
      this.consumptionSorting = undefined;
      this.consumptionType = undefined;
      this.searchValue = "";


    });

    this.propertieService.getAllIngredientProperties().subscribe(data => {
      this.properties = data;
      this.includeProperties = [...this.properties];
    });


  }

  filterInclude(filterValue: string) {
    const index: number = this.includeProperties.indexOf(filterValue);
    if (index !== -1) {
      this.includeProperties.splice(index, 1);
    } else {
      this.includeProperties.push(filterValue);
    }

    this.filterConsumptions();
  }

  filterConsumptions() {
    let sortingValue = this.consumptionSorting;
    let sortingType = this.consumptionType;
    let searchValue = this.searchValue;
    let includeProperties = this.includeProperties;
    
    this.filteredConsumptions = this.consumptions.filter(function (item: Consumption) {
      if (item.name.toLowerCase().includes(searchValue.toLowerCase()) && item.ingredients.map(element => element.ingredientPropertyList).every(e => (e && e.length > 0) ? e.some(p => includeProperties.includes(p)) : true)) {
          if(sortingType ? item.foodType == sortingType : true || sortingType == undefined) {
            return (sortingValue == undefined || item.sortingValue == sortingValue )
          }
          else {
            return false;
          }
      }
      return false;
    });

  }

  addToOrder(id: number) {
    this.orderService.postOrder(id).subscribe();
    this.badgeService.changeOrderAmount(this.badgeService.getOrderAmount() + 1);
    this._snackBar.open("This meal has been added to your order.", undefined, {
      duration: 2000,
    });
  }

  getImage(id: number) {
    return this.consumptionService.getImage(id);
  }

  getIngredientProperties(consumptionId: number) {
    let properties: string[] = [];
    this.consumptions.find(c => c.id == consumptionId).ingredients.map(i => i.ingredientPropertyList).forEach(propertyList => {
      if(propertyList.length > 0) properties = properties.concat(propertyList);
    });
    return [...new Set(properties)];
  }

  disableScroll(){
    document.getElementById('page').style.overflow = 'hidden';
  }

  enableScroll(){
    document.getElementById('page').style.overflow = 'auto';
  }

}
