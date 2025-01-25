import { Component } from '@angular/core';
import {AsyncPipe, CurrencyPipe, JsonPipe} from "@angular/common";
import {OrderService} from "../../services/order.service";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    AsyncPipe,
    JsonPipe,
    CurrencyPipe,
    RouterLink
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.scss'
})
export class CartComponent {

  constructor(protected orderService: OrderService){}

}
