import {Cart} from "./cart";
import {BillingAddress} from "./billing";

export interface Order {
  cart: Cart
  billingAddress: BillingAddress
}

