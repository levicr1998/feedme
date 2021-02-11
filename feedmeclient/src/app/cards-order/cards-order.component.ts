import { Component, OnInit, OnDestroy } from '@angular/core';
import { trigger, transition, animate, style, state } from '@angular/animations';
import { ConsumptieService } from '../services/consumptie.service';
import { Consumption } from '../classes/consumption';
import { MatSnackBar } from '@angular/material';

import { OrderService } from '../services/order.service';
import { WebsocketAPIService } from '../services/websocket-api.service';
import { BadgeService } from '../services/badge.service';
import { Router, ActivatedRoute } from '@angular/router';

@Component({
    selector: 'app-cards-order',
    templateUrl: './cards-order.component.html',
    styleUrls: ['./cards-order.component.scss'],
    animations: [
        trigger('cardAnimator', [
            transition('* => rotateOutRight', animate(200, style({ transform: 'translate(500px, 200px) rotate(40deg)' }))),
            transition('* => rotateOutLeft', animate(200, style({ transform: 'translate(-500px, 200px) rotate(-40deg)' })))
        ]),
        trigger('swipeAnimatorRight', [
            state('left', style({
                marginLeft: 0
            })),
            state('right', style({
                marginLeft: 180
            })),
            transition('left => right', [
                animate('0.8s')
            ]),
            transition('right => left', [
                animate('0s')
            ])
        ]),
        trigger('swipeAnimatorLeft', [
            state('right', style({
                marginLeft: 180
            })),
            state('left', style({
                marginLeft: 0
            })),
            transition('right => left', [
                animate('0.8s')
            ]),
            transition('left => right', [
                animate('0s')
            ])
        ]),
        trigger('menuAnimator', [
            state('top', style({
                marginTop: 0
            })),
            state('bottom', style({
                marginTop: 52
            })),
            transition('* => *', [
                animate('0.5s')
            ])
        ])
    ]
})
export class CardsOrderComponent implements OnInit {

    tutorialAllowed = true;
    consumptions: Consumption[];

    animationStates: string[] = [];

    constructor(private _snackBar: MatSnackBar, private consumptionService: ConsumptieService, private snackBar: MatSnackBar,
        private orderService: OrderService, private websocketService: WebsocketAPIService, private badgeService: BadgeService,
        private route: ActivatedRoute, private router: Router) { }

    guideRightState: string = 'left';
    guideLeftState: string = 'right';
    guideMenuState: string = 'top';

    guideProgress = 0;

    consumptionType;

    ngOnInit() {
        this.route.queryParams.subscribe(params => {
            this.consumptionType = params.type;


            let request
            if (params.type == "food")
                request = this.consumptionService.getAllFood();
            else if (params.type == "drinks")
                request = this.consumptionService.getAllDrinks();
            else
                request = this.consumptionService.getAllConsumptions();

            request.subscribe(consumptions => {
                this.consumptions = consumptions;
                this.consumptions.reverse();
            });
        });

        let getTutorial = localStorage.getItem('tutorial');
        if (getTutorial != null) {
            this.tutorialAllowed = false;
        } else {
            localStorage.setItem('tutorial', 'false')
        }
    }

    swiping = false;

    swipeRight(index) {
        if (!this.swiping) {
            this.swiping = true;
            (document.getElementById('card-' + index).getElementsByClassName('like')[0] as HTMLElement).style.color = "green";
            (document.getElementById('card-' + index).getElementsByClassName('dislike')[0] as HTMLElement).style.color = "white";
            if (!this.animationStates[index]) {
                this.animationStates[index] = 'rotateOutRight';
                this.badgeService.changeOrderAmount(this.badgeService.getOrderAmount() + 1);
                this.orderService.postOrder(this.consumptions[index].id).subscribe();
                this._snackBar.open("This meal has been added to your order.", undefined, {
                    duration: 2000,
                });
            }
        }
    }

    swipeLeft(index) {
        if (!this.swiping) {
            this.swiping = true;
            (document.getElementById('card-' + index).getElementsByClassName('like')[0] as HTMLElement).style.color = "white";
            (document.getElementById('card-' + index).getElementsByClassName('dislike')[0] as HTMLElement).style.color = "red";
            if (!this.animationStates[index]) {
                this.animationStates[index] = 'rotateOutLeft';
            }
        }
    }

    removeCard(index) {
        this.swiping = false;
        if (this.animationStates[index] && this.animationStates[index] != '') {
            this.consumptions.splice(index, 1);
            this.animationStates = [];
            if (this.consumptions.length == 0){
                if(this.consumptionType == 'drinks')
                    this.router.navigate(['/cards-order'], { queryParams: { type: 'food' } });
                else{
                    this.router.navigate(['/order-list']);
                }
            }
        }
    }

    moveDistance: number;

    onCardMove(event) {
        var element: HTMLElement = event.source.element.nativeElement;
        element.style.transition = '';
        element.style.transform = "translate3d(" + event.distance.x + "px,  " + Math.pow(event.distance.x * 0.03, 2) + "px, 0px) rotate(" +
            event.distance.x * 0.1 + "deg)";
        this.moveDistance = event.distance.x;
        var like = (element.getElementsByClassName('like')[0] as HTMLElement);
        var dislike = (element.getElementsByClassName('dislike')[0] as HTMLElement);

        var overlay = (document.getElementsByClassName('overlay')[0] as HTMLElement);
        overlay.classList.remove('fade-bg');
        let opacityHex = ((Math.round(Math.abs(event.distance.x * 0.3))).toString(16));
        if (opacityHex.length < 2) {
            opacityHex = '0' + opacityHex;
        }

        if (this.moveDistance >= 0) {
            like.style.color = "green";
            dislike.style.color = "white";
            overlay.style.backgroundColor = "#4ad050" + opacityHex;
        }
        else if (this.moveDistance <= 0) {
            dislike.style.color = "red";
            like.style.color = "white";
            overlay.style.backgroundColor = "#c70000" + opacityHex;
        }
        else {
            dislike.style.color = "white";
            like.style.color = "white";
            overlay.style.backgroundColor = "transparent";
        }
    }

    decimalToHexString(number) {
        if (number < 0) {
            number = 0xFFFFFFFF + number + 1;
        }

        return number.toString(16).toUpperCase();
    }

    onCardRelease(event, index) {
        var element: HTMLElement = event.source.element.nativeElement;
        if (this.moveDistance > 100 && (!this.animationStates[index] || this.animationStates[index] == '')) {
            this.swipeRight(index);
        }
        else if (this.moveDistance < -100 && (!this.animationStates[index] || this.animationStates[index] == '')) {
            this.swipeLeft(index);
        }
        else {
            (<HTMLElement>event.source.element.nativeElement).style.transition = 'transform 0.2s';
            (<HTMLElement>event.source.element.nativeElement).style.transform = 'translate3d(0, 0, 0)';
            if (!this.animationStates[index]) {
                (element.getElementsByClassName('like')[0] as HTMLElement).style.color = "white";
                (element.getElementsByClassName('dislike')[0] as HTMLElement).style.color = "white";
            }
        }
        var overlay = (document.getElementsByClassName('overlay')[0] as HTMLElement);
        overlay.classList.add('fade-bg');
        overlay.style.backgroundColor = "transparent";

    }

    viewIngredients(index) {
        if (this.consumptions[index].ingredients.length > 0)
            this.snackBar.open(this.consumptions[index].ingredients.map(i => i.name).join(', '), '', {
                duration: 1000
            });
    }

    changeGuideRightState() {
        this.guideRightState == 'left' ? this.guideRightState = 'right' : this.guideRightState = 'left';
    }

    changeGuideLeftState() {
        this.guideLeftState == 'left' ? this.guideLeftState = 'right' : this.guideLeftState = 'left';
    }

    changeGuideMenuState() {
        this.guideMenuState == 'bottom' ? this.guideMenuState = 'top' : this.guideMenuState = 'bottom';
    }

    progressGuide() {
        this.guideProgress++;
    }

    getImage(id: number) {
        return this.consumptionService.getImage(id);

    }
}


