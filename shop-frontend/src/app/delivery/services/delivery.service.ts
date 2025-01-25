import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {catchError, of, throwError} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class DeliveryService {

  constructor(private httpClient: HttpClient) { }

  get(orderId: string) {
    return this.httpClient.get<Delivery>(`/delivery/api/delivery/${orderId}`).pipe(
      catchError(err => {
        if (err.status == 404) {
          return of( {receivedAt: "", status: "Not found yet, but coming up soon", progress: 0} as Delivery);
        }

       return throwError(err);
      })
    );
  }

}

export interface Delivery {
  receivedAt: string;
  status: string;
  progress: number;
  order: Order;
}

export interface Order {
  id: string;
}
