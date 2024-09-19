import { Component, OnInit } from "@angular/core";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import {
  CANCELLED,
  COMPLETED,
  CONFIRMED,
  FAILED,
  IN_PROGRESS,
  UNCONFIRMED,
} from "src/app/core/constant/order.constant";
import { OrderService } from "src/app/core/services/order.service";

@Component({
  selector: "app-order",
  templateUrl: "./order.component.html",
  styleUrls: ["./order.component.scss"],
})
export class OrderComponent implements OnInit {
  // bread crumb items
  breadCrumbItems: Array<{}>;
  term;
  orders: any[];
  loading: boolean = false;
  navActive = 0;
  count: number;
  constructor(
    private orderService: OrderService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.loading = true;
    this.breadCrumbItems = [
      { label: "Đơn hàng" },
      { label: "Danh sách", active: true },
    ];
    this.orderService.findByStatus(UNCONFIRMED).subscribe({
      next: (res) => {
        this.orders = res;
        this.count = this.orders.length;
        this.loading = false;
      },
    });
  }
  getOrder(i: number) {
    if (i != this.navActive) {
      this.navActive = i;
      this.loading = true;
      if (i === 0) {
        this.orderService.findByStatus(UNCONFIRMED).subscribe({
          next: (res) => {
            this.orders = res;
            this.count = res.length;
            this.loading = false;
          },
          error: (err) => {
            this.loading = false;
          },
        });
      } else if (i === 1) {
        this.orderService.findByStatus(CONFIRMED).subscribe({
          next: (res) => {
            this.orders = res;
            this.loading = false;
          },
          error: (err) => {
            this.loading = false;
          },
        });
      } else if (i === 2) {
        this.orderService.findByStatus(IN_PROGRESS).subscribe({
          next: (res) => {
            this.orders = res;
            this.loading = false;
          },
          error: (err) => {
            this.loading = false;
          },
        });
      } else if (i === 3) {
        this.orderService.findByStatus(COMPLETED).subscribe({
          next: (res) => {
            this.orders = res;
            console.log(this.orders);

            this.loading = false;
          },
          error: (err) => {
            this.loading = false;
          },
        });
      } else if (i === 4) {
        this.orderService.findByStatus(CANCELLED).subscribe({
          next: (res) => {
            this.orders = res;
            this.loading = false;
          },
          error: (err) => {
            this.loading = false;
          },
        });
      } else if (i === 5) {
        this.orderService.findByStatus(FAILED).subscribe({
          next: (res) => {
            this.orders = res;
            this.loading = false;
          },
          error: (err) => {
            this.loading = false;
          },
        });
      }
    }
  }
  onLoadingChanged = (loading, count) => {
    this.loading = loading;
    if (count) {
      this.count = count;
    }
  };
}
