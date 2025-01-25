import { Component } from '@angular/core';
import {OrderService} from "../../services/order.service";
import {CartComponent} from "../cart/cart.component";
import {AsyncPipe, CurrencyPipe} from "@angular/common";
import {Router, RouterLink} from "@angular/router";
import {FormControl, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {BillingAddress} from "../../models/billing";

@Component({
  selector: 'app-checkout',
  standalone: true,
  imports: [
    CartComponent,
    AsyncPipe,
    CurrencyPipe,
    RouterLink,
    ReactiveFormsModule
  ],
  templateUrl: './checkout.component.html',
  styleUrl: './checkout.component.scss'
})
export class CheckoutComponent {

  billingForm = new FormGroup({
    name: new FormControl(''),
    street: new FormControl(''),
    city: new FormControl(''),
    zip: new FormControl(''),
  });

  constructor(protected orderService: OrderService, private router: Router) { }

  placeOrder() {
    let billingAddress = this.billingForm.getRawValue() as BillingAddress;
    this.orderService.orderTo(billingAddress).subscribe(
      (order) => this.router.navigateByUrl(`/tracking/${order.id}`)
    );
  }

}
