import { Consumption } from './consumption';
import { RestaurantTable } from './restaurantTable';

export class Order {
    id: number;
    restaurantTable: RestaurantTable;
    consumption: Consumption;
    note: string;
    isPaid: boolean;
    ready: boolean;
    isOrdered: boolean;
}
