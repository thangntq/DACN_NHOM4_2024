import { Component, OnInit, ViewChild } from "@angular/core";
import { FormGroup } from "@angular/forms";
import { ActivatedRoute, Router } from "@angular/router";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { ToastrService } from "ngx-toastr";
import {
  CANCELLED,
  COMPLETED,
  CONFIRMED,
  IN_PROGRESS,
  UNCONFIRMED,
} from "src/app/core/constant/order.constant";
import { OrderService } from "src/app/core/services/order.service";

@Component({
  selector: "app-exchange",
  templateUrl: "./exchange.component.html",
  styleUrls: ["./exchange.component.scss"],
})
export class ExchangeComponent implements OnInit {
  loading: boolean = false;
  breadCrumbItems: Array<{}>;
  term;
  returns: any[];
  navActive = 0;
  count: number;
  codeBill: any;
  order: any;
  billreturn: any;
  modalExchange: NgbModalRef;
  formData: FormGroup;
  content: any;
  @ViewChild("exchangeModal") exchangeModal: any;
  constructor(
    private orderService: OrderService,
    private modalService: NgbModal,
    private route: ActivatedRoute,
    private toastrService: ToastrService,
    private router: Router
  ) {
    // this.loading = true;
    this.breadCrumbItems = [
      { label: "Đổi trả" },
      { label: "Đơn hàng", active: true },
    ];
  }

  ngOnInit(): void {
    this.loading = true;
    this.orderService.findReturnByStatus(UNCONFIRMED).subscribe({
      next: (res) => {
        this.returns = res;
        this.count = this.returns.length;
        this.loading = true;
        this.codeBill = this.route.snapshot.queryParamMap.get("code");
        if (this.codeBill) {
          this.orderService.findOrderByCode(this.codeBill).subscribe({
            next: (res) => {
              const orderDetailClone = JSON.parse(
                JSON.stringify(res.orderDetail)
              );
              console.log(res);
              this.order = {
                ...res,
                orderDetail: res.orderDetail,
              };

              this.billreturn = {
                ...res,
                orderDetail: orderDetailClone,
              };
              this.billreturn.orderDetail.forEach((detail) => {
                detail.quantity = 0;
              });
              this.openModal();
              this.loading = false;
            },
            error: (error) => {
              this.loading = false;
            },
          });
        } else {
          this.loading = false;
        }
      },
    });
  }
  sendReturn() {
    const orderReturn: any = {
      id: 0,
      note: "string",
      reason: "string",
      video: "string",
      orderId: 0,
      orderDetailResponseClientTemps: [],
    };
    this.billreturn.orderDetail.forEach((item) => {
      if (item.quantity > 0) {
        orderReturn.orderDetailResponseClientTemps.push({
          id: item.id,
          returnQuantity: item.quantity,
        });
      }
    });
    if (orderReturn.orderDetailResponseClientTemps.length == 0) {
      this.toastrService.info("Vui lòng chọn sản phẩm");
      return;
    }
    if (this.content && this.content !== "") {
      orderReturn.reason = this.content;
    } else {
      this.toastrService.info("Vui lòng nhập lí do");
      return;
    }
    orderReturn.orderId = this.order.id;
    orderReturn.video = null;
    console.log(orderReturn);
    if (!this.order.codeReturn) {
      this.orderService.createReturn(orderReturn).subscribe({
        next: (res) => {
          this.toastrService.info("Tạo đơn trả hàng thành công!");
          this.closeModal();
        },
        error: () => {
          this.toastrService.error("Vui lòng thử lại sau!");
        },
      });
    }
  }
  openModal() {
    this.modalExchange = this.modalService.open(this.exchangeModal, {
      size: "lg",
      backdrop: false,
      windowClass: "custom-modal",
    });
  }
  getOrder(i: number) {
    if (i != this.navActive) {
      this.navActive = i;
      this.loading = true;
      if (i === 0) {
        this.orderService.findReturnByStatus(UNCONFIRMED).subscribe({
          next: (res) => {
            this.returns = res;
            this.count = res.length;
            this.loading = false;
          },
          error: (err) => {
            this.loading = false;
          },
        });
      } else if (i === 1) {
        this.orderService.findReturnByStatus(CONFIRMED).subscribe({
          next: (res) => {
            this.returns = res;
            this.loading = false;
          },
          error: (err) => {
            this.loading = false;
          },
        });
      } else if (i === 2) {
        this.orderService.findReturnByStatus(IN_PROGRESS).subscribe({
          next: (res) => {
            this.returns = res;
            this.loading = false;
          },
          error: (err) => {
            this.loading = false;
          },
        });
      } else if (i === 3) {
        this.orderService.findReturnByStatus(COMPLETED).subscribe({
          next: (res) => {
            this.returns = res;
            this.loading = false;
          },
          error: (err) => {
            this.loading = false;
          },
        });
      } else if (i === 4) {
        this.orderService.findReturnByStatus(CANCELLED).subscribe({
          next: (res) => {
            this.returns = res;
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
  getQuantity(productDetail: any) {
    return this.order.orderDetail.find((item) => item.id === productDetail.id)
      .quantity;
  }
  increaseQuantity(productDetail: any) {
    if (
      this.billreturn.orderDetail.find((item) => item.id === productDetail.id)
        .quantity <
      this.order.orderDetail.find((item) => item.id === productDetail.id)
        .quantity
    ) {
      this.billreturn.orderDetail.find((item) => item.id === productDetail.id)
        .quantity++;
    }
  }

  decreaseQuantity(productDetail: any) {
    if (
      this.billreturn.orderDetail.find((item) => item.id === productDetail.id)
        .quantity > 0
    ) {
      this.billreturn.orderDetail.find((item) => item.id === productDetail.id)
        .quantity--;
    }
  }

  closeModal() {
    this.router.navigate([], {
      queryParams: { code: null },
      queryParamsHandling: "merge",
      replaceUrl: true,
    });
    this.modalExchange.close();
  }
}
