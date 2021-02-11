import { Guest } from '../classes/guest';

export class RestaurantTableMockService {

   

    joinTable(name: string){
        let tableResponse = {
            "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ0YWJsZUlkIjo3NzcsImV4cCI6MTU3NjE1MjUwOSwib3BlbmVkQXQiOjE1NzYwNjYxMDl9._x1UMrKE27Vd3nEv_CxXvCm-Wmg5-HROk6lBSYIf17fSil6qR4qOL3JHgZS1TOECIBTz_idg4BZsFEcuJmN9_Q",
            "guest": {
              "id": 778,
              "name": "Levi"
            }
        }
return tableResponse;
    }

    saveGuest(guest: Guest) {
        localStorage.setItem("guest", guest.id.toString());
    }

    validate(shouldBeLoggedIn: boolean) {
      return true;
    }

    saveToken(token: string) {
        localStorage.setItem("tableToken", token);
    }


}