import { Component, OnInit, Inject } from '@angular/core';

import {MatDialogRef} from '@angular/material/dialog';
import {MAT_DIALOG_DATA} from '@angular/material/dialog';

@Component({
  selector: 'app-order-list-dialog',
  templateUrl: './order-list-dialog.component.html',
  styleUrls: ['./order-list-dialog.component.scss']
})
export class OrderListDialogComponent implements OnInit {

  constructor(public dialogRef: MatDialogRef<OrderListDialogComponent>,  
    @Inject(MAT_DIALOG_DATA) public data: string){

  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onYesClick(): void {
    this.dialogRef.close("confirmation");
  }

  ngOnInit() {
  }

}
