import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ConsumptieService } from './consumptie.service';
import { Ingredient } from '../classes/ingredient';
import { Consumption } from '../classes/consumption';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';

describe('ConsumptieService', () => {
  let injector: TestBed;
  let service: ConsumptieService;
  let httpMock: HttpTestingController;

  const dummyConsumptionListResponse: Consumption[] = [
      { id: 1, price: 12, foodType: 'DESSERT', image:"sddas", name:'Toetje', description:'Lekker toetje', ingredients: [], sortingValue: 1},
      { id: 1, price: 13, foodType: 'MAIN', image:"sddas", name:'Biefstukje', description:'Lekker toetje', ingredients: [], sortingValue: 2},
      { id: 1, price: 4, foodType: 'STARTER', image:"sddas", name:'Carpaccio', description:'Lekker carpaccio', ingredients: [], sortingValue: 1},
  ];  

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ConsumptieService, CookieService],
    });

    injector = getTestBed();
    service = injector.get(ConsumptieService);
    httpMock = injector.get(HttpTestingController);

  });

  afterEach(() => {
httpMock.verify();
  });



  it('getAllConsumptions() should return data', () => {
    service.getAllConsumptions().subscribe((res) => {
      expect(res).toEqual(dummyConsumptionListResponse);
    });

    const req = httpMock.expectOne(environment.apiUrl + '/consumptions/');
    expect(req.request.method).toBe('GET');
    req.flush(dummyConsumptionListResponse);
  });
});
