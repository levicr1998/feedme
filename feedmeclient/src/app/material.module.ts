import { NgModule } from '@angular/core';


import {
    MatButtonModule,
    MatCardModule,
    MatMenuModule,
    MatIconModule,
    MatToolbarModule,
    MatGridListModule,
    MatTreeModule,
    MatInputModule,
    MatDialogModule,
    MatSnackBarModule,
    MatRadioModule,
    MatCheckboxModule,
    MatBadgeModule,
    MatRippleModule,
    MatProgressSpinnerModule

} from '@angular/material';

@NgModule({
    exports: [
        MatButtonModule,
        MatCardModule,
        MatMenuModule,
        MatIconModule,
        MatToolbarModule,
        MatGridListModule,
        MatTreeModule,
        MatInputModule,
        MatDialogModule,
        MatToolbarModule,
        MatSnackBarModule,
        MatRadioModule,
        MatCheckboxModule,
        MatSnackBarModule,
        MatBadgeModule,
        MatRippleModule,
        MatProgressSpinnerModule

    ]
})

export class MaterialModule { }