import { Component, OnInit } from '@angular/core';
import { RestaurantTableService } from '../services/restaurant-table.service';
import { Router, ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material';


@Component({
    selector: 'app-ask-name',
    templateUrl: './ask-name.component.html',
    styleUrls: ['./ask-name.component.scss']
})
export class AskNameComponent implements OnInit {

    nickname: string;
    open: boolean;
    token: string;
    id: string;


    constructor(private tableService: RestaurantTableService, private router: ActivatedRoute, private route: Router, private _snackBar: MatSnackBar) { }

    ngOnInit() {
        this.tableService.validate(false);
        this.router.queryParams.subscribe(params => {
            this.open = params.open ? true : false;
            if (this.open) {
                this.id = params.open;
            } else {
                this.token = params.token;
            }
        })
    }


    joinTable() {

        let request;

        if (this.nickname == null) {
            this.openSnackBar("Please fill in your name!");
            return;
        }
        if (this.open) {
            request = this.tableService.openTable(this.nickname, this.id);
            console.log("Opening table");
        } else {
            console.log("Joining table");
            this.tableService.saveToken(this.token);
            request = this.tableService.joinTable(this.nickname);
            console.log(request);
        }
        request.subscribe(
            data => {
                console.log(data);
                this.tableService.saveToken(data.token);
                this.tableService.saveGuest(data.guest);
                this.route.navigate(['/home']);
            },
            error => {
                this.openSnackBar(error.error.message)
                console.log(error);
            }
        );
    }

    openSnackBar(message: string) {
        this._snackBar.open(message, null, {
          duration: 2000,
        });
      }
}
