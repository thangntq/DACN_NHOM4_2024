import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { OrderService } from "src/app/shared/services/order.service";

@Component({
  selector: "app-order-success",
  templateUrl: "./order-success.component.html",
  styleUrls: ["./order-success.component.scss"],
})
export class OrderSuccessComponent implements OnInit {
  billCode: any;
  order: any;
  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.billCode = this.route.snapshot.queryParamMap.get("bill_code");
    if (this.billCode) {
      this.orderService.findByCodeInSucces(this.billCode).subscribe({
        next: (res) => {
          if (res.statusPay == 1 || res.paymentType == "COD") {
            this.order = res;
          } else {
            this.router.navigate(["order/fail"], {
              queryParams: {
                bill_code: res.code,
              },
            });
          }
        },
        error: (err) => {
          this.router.navigate(["checkouts/error"], {});
        },
      });
    } else {
      this.router.navigate(["checkouts/error"], {});
    }
  }
}
