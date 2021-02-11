import { BrowserModule } from "@angular/platform-browser";
import { NgModule } from "@angular/core";

import { DragDropModule } from "@angular/cdk/drag-drop";
import { ZXingScannerModule } from "@zxing/ngx-scanner";
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { MaterialModule } from "./material.module";
import { AppRoutingModule } from "./app-routing.module";
import { MatListModule } from "@angular/material/list";
import { FormsModule } from "@angular/forms";
import { TranslateModule, TranslateService } from "@ngx-translate/core";
import { TranslateLoader } from "@ngx-translate/core";
import { TranslateHttpLoader } from "@ngx-translate/http-loader";
import { HttpClientTestingModule } from "@angular/common/http/testing";
import { CookieService } from "ngx-cookie-service";
import { HttpClientModule, HttpClient } from "@angular/common/http";
import { Observable } from "rxjs/internal/Observable";
import { of } from "rxjs";
import { HttpLoaderFactory } from "./app.module";
import { RouterTestingModule } from '@angular/router/testing';
import { MatDialogRef } from '@angular/material';

export class TranslateServiceStub {
  public get(key: any): any {
    return of(key);
  }
}

@NgModule({
  declarations: [],
  imports: [
    HttpClientTestingModule,
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
    ZXingScannerModule
  ],
  providers: [CookieService, TranslateService],
  exports: [
    HttpClientTestingModule,
    BrowserModule,
    TranslateModule,
    MatListModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    RouterTestingModule,
    MaterialModule,
    DragDropModule,
    FormsModule,
    ZXingScannerModule,
    HttpClientTestingModule
  ]
})
export class TestModule {}
