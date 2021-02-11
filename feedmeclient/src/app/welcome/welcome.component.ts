import { Component, OnInit, RootRenderer, ViewChild } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { RestaurantTableService } from '../services/restaurant-table.service';
import { DomSanitizer } from '@angular/platform-browser';
import Hammer from 'hammerjs';
import { MAT_RIPPLE_GLOBAL_OPTIONS, RippleGlobalOptions, MatSnackBar } from '@angular/material';
import { Router } from '@angular/router';
import { ZXingScannerComponent } from '@zxing/ngx-scanner';

const globalRippleConfig: RippleGlobalOptions = {
    animation: {
        enterDuration: 300
    }
};

@Component({
    selector: 'app-welcome',
    templateUrl: './welcome.component.html',
    styleUrls: ['./welcome.component.scss'],
    providers: [
        { provide: MAT_RIPPLE_GLOBAL_OPTIONS, useValue: globalRippleConfig }
    ]
})
export class WelcomeComponent implements OnInit {

    @ViewChild('scanner', { static: false })
    scanner: ZXingScannerComponent;

    cameras: any[] = [];
    currentCamera: any = null;
    currentCameraIndex: number = 1;

    hasPermission: boolean;

    constructor(public translate: TranslateService, private tableService: RestaurantTableService, private sanitizer: DomSanitizer, private router: Router, private _snackBar: MatSnackBar) { }

    ngOnInit() {
        this.tableService.validate(false);
        //navigator.mediaDevices.getUserMedia({ video: true });
    }

    camerasFoundHandler(event) {
        console.log(event);
        this.cameras = event;

        if (this.cameras.length > 1) {
            var mc = new Hammer.Manager(this.scanner);
            console.log(mc);
            mc.add(new Hammer.Tap({ event: 'doubletap', taps: 2 }));
            mc.on("doubletap", () => this.changeCamera());
        }
    }

    changeCamera() {
        this.scanner.device = this.cameras[++this.currentCameraIndex % this.cameras.length];
        console.log("Camera changed");
    }

    scanSuccessHandler(event) {
        let domain = event.split('/');
        this.getTableOpen(domain[5], domain[6]);
    }

    changeLanguage(language: string) {
        this.translate.use(language);
        localStorage.setItem('language', language);
    }

    getTableOpen(status, tokenId) {
        this.tableService.getTableIsOpenStatus(tokenId).subscribe(response => {
            if (response == false) {
                if (status == "open") {
                    this.router.navigate(['/insert-name'], { queryParams: { open: tokenId } });
                }
            } else {
                if (status == "token") {
                    this.router.navigate(['/insert-name'], { queryParams: { token: tokenId } });
                } else {
                    this.openSnackBar("Tafel is al geopend. Scan de geheime QR-code van de tafel.")
                }
            }
            return response;

        }, error => {
            this.openSnackBar(error.error.message);
            console.log(error);
        });
    }

    // onHasPermission(has: boolean): void {
    //     this.hasPermission = has;
    // }

    openSnackBar(message: string) {
        this._snackBar.open(message, null, {
            duration: 2000,
        });
    }

}
