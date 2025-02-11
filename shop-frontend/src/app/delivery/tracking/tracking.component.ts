import { Component } from '@angular/core';
import {of, switchMap, takeUntil, timer} from "rxjs";
import {AsyncPipe, JsonPipe, NgStyle} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {DeliveryService} from "../services/delivery.service";
import {takeUntilDestroyed} from "@angular/core/rxjs-interop";

@Component({
  selector: 'app-tracking',
  standalone: true,
  imports: [
    AsyncPipe,
    JsonPipe,
    NgStyle
  ],
  templateUrl: './tracking.component.html',
  styleUrl: './tracking.component.scss'
})
export class TrackingComponent {

  constructor(private route: ActivatedRoute, private deliveryService: DeliveryService) {}

  delivery$ = timer(0, 3000).pipe(
    switchMap(() => {
      let orderId = this.route.snapshot.params['orderId'];
      return this.deliveryService.get(orderId)
    }),
    takeUntilDestroyed(),
  );



}
