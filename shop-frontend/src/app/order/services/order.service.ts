import { Injectable } from '@angular/core';
import {BehaviorSubject, tap} from "rxjs";
import {Cart, CartItem} from "../models/cart";
import {BillingAddress} from "../models/billing";
import {Order} from "../models/order";
import {HttpClient} from "@angular/common/http";

@Injectable({
  providedIn: 'root'
})
export class OrderService {

  public cart$ = new BehaviorSubject<Cart>({items:[]} as unknown as Cart)

  constructor(private httpClient: HttpClient) { }

  reset() {
    this.cart$.next({items:[]} as unknown as Cart);
  }

  orderTo(billingAddress: BillingAddress) {
    let order = { cart: this.cart$.getValue(), billingAddress } as Order;
    return this.httpClient.post<{ id: string }>("/order/api/order", order).pipe(
      tap(value =>  this.reset())
    );
  }

  addItem(cartItem: CartItem) {
    let newCart = {...this.cart$.getValue()};

    let sameMenuItems = newCart.items.filter(i => i.itemId == cartItem.itemId);
    if (sameMenuItems.length != 0 ){
      let sameMenuItem = sameMenuItems[0];
      sameMenuItem.count = sameMenuItem.count + 1;
      sameMenuItem.total = sameMenuItem.count * sameMenuItem.itemPrice;
    } else {
      newCart.items.push(cartItem);
    }

    newCart.total = newCart.items.reduce((total, cartItem) => total + cartItem.total, 0);

    this.cart$.next(newCart)
  }

}
