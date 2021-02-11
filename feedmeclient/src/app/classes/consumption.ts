import { Ingredient } from './ingredient';

export class Consumption {
    id : number;
    price : number;
    foodType : string;
    image : string;
    name : string;
    description : string;
    ingredients: Ingredient[];
    sortingValue : number;
}
