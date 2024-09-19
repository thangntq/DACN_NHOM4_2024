import { Component, OnInit } from "@angular/core";
import { CommonConstant } from "src/app/shared/constant/order.constant";
import { OrderService } from "src/app/shared/services/order.service";
import { AuthenticationService } from "../login/auth.service";

@Component({
  selector: "app-orders",
  templateUrl: "./orders.component.html",
  styleUrls: ["./orders.component.scss"],
})
export class OrdersComponent implements OnInit {
  public openDashboard: boolean = false;
  orders: any;
  UNCONFIRMED = CommonConstant.UNCONFIRMED;
  CONFIRMED = CommonConstant.CONFIRMED;
  IN_PROGRESS = CommonConstant.IN_PROGRESS;
  COMPLETED = CommonConstant.COMPLETED;
  CANCELLED = CommonConstant.CANCELLED;
  FAILED = CommonConstant.FAILED;
  CHUA_THANH_TOAN = CommonConstant.CHUA_THANH_TOAN;
  DA_THANH_TOAN = CommonConstant.DA_THANH_TOAN;
  userInfo: any;
  constructor(
    private orderService: OrderService,
    private authService: AuthenticationService
  ) {}

  ngOnInit(): void {
    this.loadOrder();
  }
  private loadOrder() {
    this.orderService.getOrder().subscribe({
      next: (res) => {
        this.orders = res;
      },
      error: (err) => {
        this.orders.error("Lỗi api order");
      },
    });
    this.authService.getUser().subscribe({
      next: (value) => {
        this.userInfo = value[0];
      },
      error: (error) => {},
    });
  }
  ToggleDashboard() {
    this.openDashboard = !this.openDashboard;
  }
  logout() {
    this.authService.logout();
  }
}
