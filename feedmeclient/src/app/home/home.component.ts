import { Component, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { RestaurantTableService } from '../services/restaurant-table.service';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.scss']
})
export class HomeComponent implements OnInit {
  qrCode;
  tableNumber: number;

  constructor(public translate: TranslateService, private tableService: RestaurantTableService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
    this.tableService.getPrivateQr().subscribe(response => this.qrCode = this.sanitizer.bypassSecurityTrustUrl(URL.createObjectURL(response)));
    this.tableService.getTableByToken().subscribe(table => this.tableNumber = table.number);
  }

}
