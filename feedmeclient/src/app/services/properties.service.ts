import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class PropertiesService {

  private propertieUrl : string;

  constructor(private http : HttpClient) { 
    this.propertieUrl = environment.apiUrl + '/ingredientproperties';
  }

  public getAllIngredientProperties(){
    return this.http.get<string[]>(this.propertieUrl + '/');
  }
}
