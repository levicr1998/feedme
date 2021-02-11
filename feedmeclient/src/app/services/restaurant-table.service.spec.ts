import { TestBed, getTestBed, flush } from '@angular/core/testing';

import { RestaurantTableService } from './restaurant-table.service';
import { HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';
import { RestaurantTable } from '../classes/restaurantTable';
import { RouterTestingModule } from '@angular/router/testing';
import { HomeComponent } from '../home/home.component';
import { CookieService } from 'ngx-cookie-service';
import { environment } from 'src/environments/environment';
import { MatDialogModule, MatSnackBarModule } from '@angular/material';

describe('restaurant-table-service', () => {
    let injector: TestBed;
    let service: RestaurantTableService;
    let httpMock: HttpTestingController;
    



    const dummyDataOrder : RestaurantTable = {
        id: 1,
        number:20,
        orders: [],
        open: false
};
    const dummyDataResponseList : RestaurantTable[] = [{
        id: 1,
        number:20,
        orders: [],
        open: false
    },{ 
    id: 2,
    number:20,
    orders: [],
    open: false
    }
]
    
    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule, MatDialogModule, MatSnackBarModule, RouterTestingModule.withRoutes([])],
        providers: [RestaurantTableService, CookieService],
      });
  
      injector = getTestBed();
      service = injector.get(RestaurantTableService);
      httpMock = injector.get(HttpTestingController);
  
    });

    beforeEach(() => {
        let store = {};
        const mockLocalStorage = {
          getItem: (key: string): string => {
            return key in store ? store[key] : null;
          },
          setItem: (key: string, value: string) => {
            store[key] = `${value}`;
          },
          removeItem: (key: string) => {
            delete store[key];
          },
          clear: () => {
            store = {};
          }
        };

        spyOn(localStorage, 'getItem')
          .and.callFake(mockLocalStorage.getItem);
        spyOn(localStorage, 'setItem')
          .and.callFake(mockLocalStorage.setItem);
        spyOn(localStorage, 'removeItem')
          .and.callFake(mockLocalStorage.removeItem);
        spyOn(localStorage, 'clear')
          .and.callFake(mockLocalStorage.clear);

          localStorage.setItem('tableToken', '123456');
    })
  
    afterEach(() => {
  httpMock.verify();
    });
  
  
  
    it('get Table By Token', () => {

    service.getTableByToken().subscribe((res) => {
            console.log(res);
            expect(res).toEqual(dummyDataOrder);
           
    });
    const req = httpMock.expectOne(environment.apiUrl + '/tables/123456');
    expect(req.request.method).toBe('GET');
    req.flush(dummyDataOrder);
  });

  it('checks if token exists', () => {
    expect(service.tokenExists()).toBe(true);
    localStorage.clear();
    expect(service.tokenExists()).toBe(false);
  });

  
//   it('checks if getToken works as expected', () => {
//     service.saveToken('123456');
//     expect(service.getToken).toBe(localStorage.getItem("tableToken"));
//     service.clearTokens();
//     expect(service.getToken).toBeNull();
//   });



});




