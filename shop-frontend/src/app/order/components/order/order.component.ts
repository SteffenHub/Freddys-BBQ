import { Component } from '@angular/core';
import {HttpClient} from "@angular/common/http";

import {AsyncPipe, CurrencyPipe, JsonPipe, NgClass, NgForOf} from "@angular/common";
import {OrderService} from "../../services/order.service";
import {CartComponent} from "../cart/cart.component";
import {CartItem} from "../../models/cart";
import {MenuCategory, MenuItem} from "../../models/menu";

@Component({
  selector: 'app-order',
  standalone: true,
  imports: [
    AsyncPipe,
    JsonPipe,
    NgForOf,
    CurrencyPipe,
    NgClass,
    CartComponent,
  ],
  templateUrl: './order.component.html',
  styleUrl: './order.component.scss'
})
export class OrderComponent {

  menuCategories$ =  this.httpClient.get<MenuCategory[]>("/order/api/menu");

  constructor(private httpClient: HttpClient,
              protected orderService: OrderService) { }

  addToCart(menuItem: MenuItem) {
    let cartItem = {
      itemId: menuItem.id,
      itemTitle: menuItem.title,
      itemPrice: menuItem.price,
      count: 1,
      total: menuItem.price,
    } as CartItem;
    this.orderService.addItem(cartItem);
  }
}
