import { Injectable, Inject, Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CookieService } from 'ngx-cookie-service';
import { Observable } from 'rxjs';
import { RestaurantTable } from '../classes/restaurantTable';
import { Guest } from '../classes/guest';
import { JsonPipe } from '@angular/common';
import { Router } from '@angular/router';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { environment } from 'src/environments/environment';

@Injectable({
    providedIn: 'root'
})
export class RestaurantTableService {

    tableUrl: string;

    constructor(private http: HttpClient, private router: Router, public dialog: MatDialog, private _snackBar: MatSnackBar) {
        this.tableUrl = environment.apiUrl + '/tables';
    }

    getTableByToken(): Observable<RestaurantTable> {
        let tableToken = this.getToken();
        return this.http.get<RestaurantTable>(this.tableUrl + '/' + tableToken);
    }

    getTableIsOpenStatus(tokenId: string): Observable<any> {
        return this.http.get(this.tableUrl + "/is-open/" + tokenId);
    }

    getPrivateQr(): Observable<Object> {
        let tableToken = this.getToken();
        return this.http.get(this.tableUrl + '/private-qr/' + tableToken, { responseType: 'blob' });
    }

    postTableQR(url: string): Observable<Guest> {
        let guestName: string = "Lars"
        return this.http.post<Guest>(url, guestName);
    }

    saveToken(token: string) {
        localStorage.setItem("tableToken", token);
    }

    getToken(): string {
        return localStorage.getItem("tableToken");
    }

    getGuest(): Guest {
        let guest = new Guest;
        guest.id = Number.parseInt(localStorage.getItem("guest"));
        return guest;
    }

    tokenExists(): boolean {
        return localStorage.getItem("tableToken") != null && localStorage.getItem("tableToken").length > 1;
    }

    saveGuest(guest: Guest) {
        localStorage.setItem("guest", guest.id.toString());
    }


    joinTable(name: string) {
        if (!this.tokenExists()) {
            throw Error("No token found");
        }
        return this.http.post(this.tableUrl + '/token/' + this.getToken(), name);


    }

    openTable(name: string, id: string) {
        return this.http.post(this.tableUrl + '/open/' + id, name);
    }

    validate(shouldBeLoggedIn: boolean) {
        let object = { token: null, guest: null };
        object.token = this.getToken();
        object.guest = this.getGuest();
        this.http.post(environment.apiUrl + '/guests/validate', object)
            .subscribe(
                success => {
                    console.log("All good");
                    this.router.navigate(["/home"]);
                },
                error => {
                    if (shouldBeLoggedIn) {
                        this.clearTokens();
                        this.showDialog();
                        this.router.navigate(["/welcome"]);
                    } else {
                        this.clearTokens();
                        //this.openSnackBar("Geen verbinding met de server. Probeer opnieuw!")
                        console.log("Logging in");
                    }

                }
            );
    }

    checkLogin(shouldBeLoggedIn: boolean){
        let object = { token: null, guest: null };
        object.token = this.getToken();
        object.guest = this.getGuest();
        this.http.post(environment.apiUrl + '/guests/validate', object)
            .subscribe(
                success => {
                },
                error => {
                    if (shouldBeLoggedIn) {
                        this.clearTokens();
                        this.showDialog();
                        this.router.navigate(["/welcome"]);
                    } else {
                        this.clearTokens();
                        //this.openSnackBar("Geen verbinding met de server. Probeer opnieuw!")
                        console.log("Logging in");
                    }

                }
            );
    }

    clearTokens() {
        localStorage.removeItem("tableToken");
        localStorage.removeItem("guest");
    }

    showDialog() {
        const dialogRef = this.dialog.open(DialogComponent, {
            width: 'auto',
            height: '150px',
        });
        setTimeout(() => {
            dialogRef.close();
        }, 1000000);
    }

    openSnackBar(message: string) {
        this._snackBar.open(message, null, {
            duration: 2000,
        });
    }

    getTablesWithOrders(): Observable<RestaurantTable[]> {
        return this.http.get<RestaurantTable[]>(this.tableUrl + '/chef');
    }

}


@Component({
    selector: 'dialog-component',
    templateUrl: '../dialog-component/dialog-component.html',
    styleUrls: ['../dialog-component/dialog-component.scss'],
})
export class DialogComponent {

    constructor(
        public dialogRef: MatDialogRef<DialogComponent>) { }

    onOkClick(): void {
        this.dialogRef.close();
    }

}