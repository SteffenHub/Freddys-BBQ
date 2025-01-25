import { Routes } from '@angular/router';
import {OrderComponent} from "./order/components/order/order.component";
import {TrackingComponent} from "./delivery/tracking/tracking.component";
import {CheckoutComponent} from "./order/components/checkout/checkout.component";

export const routes: Routes = [
  { path: '', component: OrderComponent },
  { path: 'checkout', component: CheckoutComponent },
  { path: 'tracking/:orderId', component: TrackingComponent }
];
