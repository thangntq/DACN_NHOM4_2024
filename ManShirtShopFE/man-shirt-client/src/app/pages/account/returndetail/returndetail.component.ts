import { Component, Inject, OnInit, PLATFORM_ID } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { OrderService } from "src/app/shared/services/order.service";
import { AuthenticationService } from "../login/auth.service";
import { NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { CommonConstant } from "src/app/shared/constant/order.constant";

@Component({
  selector: "app-returndetail",
  templateUrl: "./returndetail.component.html",
  styleUrls: ["./returndetail.component.scss"],
})
export class ReturndetailComponent implements OnInit {
  returnCode: any;
  userInfo: any;
  billReturn: any;
  UNCONFIRMED = CommonConstant.UNCONFIRMED;
  CONFIRMED = CommonConstant.CONFIRMED;
  IN_PROGRESS = CommonConstant.IN_PROGRESS;
  COMPLETED = CommonConstant.COMPLETED;
  CANCELLED = CommonConstant.CANCELLED;
  FAILED = CommonConstant.FAILED;
  CHUA_THANH_TOAN = CommonConstant.CHUA_THANH_TOAN;
  DA_THANH_TOAN = CommonConstant.DA_THANH_TOAN;
  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private route: ActivatedRoute,
    private orderService: OrderService,
    private toastrService: ToastrService,
    private authService: AuthenticationService,
    private modalService: NgbModal
  ) {}
  public openDashboard: boolean = false;

  ngOnInit(): void {
    this.returnCode = this.route.snapshot.params["id"];
    if (this.returnCode) {
      this.orderService.getReturnByBillCode(this.returnCode).subscribe({
        next: (res) => {
          this.billReturn = res.data;
          console.log(res);
        },
        error: (err) => {
          this.toastrService.error("Lỗi hóa đơn");
        },
      });
    }
    this.authService.getUser().subscribe({
      next: (value) => {
        this.userInfo = value;
      },
      error: (error) => {},
    });
  }

  ToggleDashboard() {
    this.openDashboard = !this.openDashboard;
  }
  getImageProductDetail(product: any, productDetail: any) {
    return product.productImage.filter(
      (image) => image.color.id === productDetail.color.id
    );
  }
}
