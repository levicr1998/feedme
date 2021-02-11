import { Consumption } from './consumption';
import { Guest } from './guest';
import { Order } from './order';

export class RestaurantTable {
    id: number;
    number: number;
    orders: Order[];
    open: boolean;
}
