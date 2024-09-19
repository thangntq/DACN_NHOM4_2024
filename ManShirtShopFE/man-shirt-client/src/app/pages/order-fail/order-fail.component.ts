import { Component, OnInit } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import { OrderService } from "src/app/shared/services/order.service";

@Component({
  selector: "app-order-fail",
  templateUrl: "./order-fail.component.html",
  styleUrls: ["./order-fail.component.scss"],
})
export class OrderFailComponent implements OnInit {
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
          console.log(res);
          if (res.statusPay == 0 && res.paymentType == "VNPAY") {
            this.order = res;
          } else {
            this.router.navigate(["order/success"], {
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
