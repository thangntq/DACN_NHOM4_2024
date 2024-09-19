import {
  Component,
  Inject,
  OnInit,
  PLATFORM_ID,
  TemplateRef,
  ViewChild,
} from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { Product, ProductDetail } from "src/app/shared/classes/product";
import { CommonConstant } from "src/app/shared/constant/order.constant";
import { OrderService } from "src/app/shared/services/order.service";
import { AuthenticationService } from "../login/auth.service";
import { ModalDismissReasons, NgbModal } from "@ng-bootstrap/ng-bootstrap";
import { isPlatformBrowser } from "@angular/common";

@Component({
  selector: "app-orderdetail",
  templateUrl: "./orderdetail.component.html",
  styleUrls: ["./orderdetail.component.scss"],
})
export class OrderdetailComponent implements OnInit {
  @ViewChild("returnModal", { static: false }) returnModal: TemplateRef<any>;
  @ViewChild("ratingModal") ratingModal;
  public product: any;
  public modalOpen: boolean = false;
  public closeResult: string;
  public openDashboard: boolean = false;
  orderDetail: any;
  billCode: any;
  bill: any;
  billreturn: any;
  UNCONFIRMED = CommonConstant.UNCONFIRMED;
  CONFIRMED = CommonConstant.CONFIRMED;
  IN_PROGRESS = CommonConstant.IN_PROGRESS;
  COMPLETED = CommonConstant.COMPLETED;
  CANCELLED = CommonConstant.CANCELLED;
  FAILED = CommonConstant.FAILED;
  CHUA_THANH_TOAN = CommonConstant.CHUA_THANH_TOAN;
  DA_THANH_TOAN = CommonConstant.DA_THANH_TOAN;
  isCheckout: boolean = false;
  userInfo: any;
  leadtime: any;
  uploadedFiles: any[] = [];
  content: any;
  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private route: ActivatedRoute,
    private orderService: OrderService,
    private toastrService: ToastrService,
    private authService: AuthenticationService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.billCode = this.route.snapshot.params["id"];
    this.loadBill();
    this.authService.getUser().subscribe({
      next: (value) => {
        this.userInfo = value[0];
      },
      error: (error) => {},
    });
  }
  loadBill() {
    if (this.billCode) {
      this.orderService.getOrderByBillCode(this.billCode).subscribe({
        next: (res) => {
          const orderDetailClone = JSON.parse(JSON.stringify(res.orderDetail));

          this.bill = {
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
          console.log(res);
          this.getLeadTime();
        },
        error: (err) => {
          this.toastrService.error("Lỗi hóa đơn");
        },
      });
    }
  }
  ToggleDashboard() {
    this.openDashboard = !this.openDashboard;
  }
  getProduct(
    productListDatas: Product[],
    productDetail: ProductDetail
  ): Product {
    const product = productListDatas.find(
      (p) => p.id === productDetail.productId
    );
    return product ? product : null;
  }

  getImageProductDetail(product: any, productDetail: any) {
    return product.productImage.filter(
      (image) => image.color.id === productDetail.color.id
    );
  }
  getTamTinhOrder(): number {
    let totalPrice = 0;
    if (this.bill && this.bill.orderDetail) {
      this.bill.orderDetail.forEach((item) => {
        totalPrice +=
          (item?.unitprice - (item?.unitprice * item?.discount) / 100) *
          item.quantity;
      });
      return totalPrice;
    }
    return 0;
  }
  voucherOrder() {
    let costVoucher = 0;
    if (!this.bill.voucher.type) {
      costVoucher = (this.bill.voucher.discount * this.getTamTinhOrder()) / 100;
      if (this.bill.voucher.max && costVoucher > this.bill.voucher.max) {
        costVoucher = this.bill.voucher.max;
        return costVoucher;
      }
    } else {
      costVoucher = this.bill.voucher.discount;
      return costVoucher;
    }
    return costVoucher;
  }
  getLeadTime() {
    this.orderService
      .getLeadTime(this.bill.idDistrict, this.bill.idWard)
      .subscribe({
        next: (value) => {
          this.leadtime = value.data;
        },
      });
  }
  openModal() {
    this.modalOpen = true;
    if (isPlatformBrowser(this.platformId)) {
      // For SSR
      this.modalService
        .open(this.returnModal, {
          size: "lg",
          ariaLabelledBy: "address-modal",
          centered: true,
          windowClass: "return-modal",
          backdrop: false,
        })
        .result.then(
          (result) => {
            `Result ${result}`;
          },
          (reason) => {
            this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
          }
        );
    }
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return "by pressing ESC";
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return "by clicking on a backdrop";
    } else {
      return `with: ${reason}`;
    }
  }

  ngOnDestroy() {
    if (this.modalOpen) {
      this.modalService.dismissAll();
    }
  }
  handleFileUpload(event: any) {
    const file = event.target.files[0];

    if (!file) {
      this.toastrService.info("Vui lòng chọn một video");
      return;
    }

    if (file.type !== "video/mp4") {
      this.toastrService.error("Chỉ hỗ trợ tải lên video định dạng MP4");
      return;
    }
    console.log(file.size);

    if (file.size > 10 * 1024 * 1024) {
      this.toastrService.error("Kích thước video vượt quá giới hạn (10MB)");
      return;
    }

    if (this.uploadedFiles.length >= 1) {
      this.toastrService.info("Bạn chỉ có thể tải lên một video");
      return;
    }

    this.orderService.uploadImage(file).subscribe({
      next: (value) => {
        this.uploadedFiles.push(value?.[0]);
        this.toastrService.success("Tải video thành công");
      },
      error: (err) => {
        this.toastrService.error("Tải video lỗi! Vui lòng thử lại");
      },
    });
  }

  removeFile(index: number) {
    this.uploadedFiles.splice(index, 1);
  }

  increaseQuantity(productDetail: any) {
    if (
      this.billreturn.orderDetail.find((item) => item.id === productDetail.id)
        .quantity <
      this.bill.orderDetail.find((item) => item.id === productDetail.id)
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
  getQuantity(productDetail: any) {
    return this.bill.orderDetail.find((item) => item.id === productDetail.id)
      .quantity;
  }
  rating(productDetail: any) {
    this.product = productDetail?.productDetailId?.product;
    this.orderDetail = productDetail;
    this.ratingModal.openModal();
  }
  sendReturn() {
    const orderReturn: any = {
      id: 0,
      returnId: 0,
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
    orderReturn.orderId = this.bill.id;
    if (this.uploadedFiles && this.uploadedFiles.length == 0) {
      this.toastrService.info("Vui lòng thêm video");
      return;
    }
    orderReturn.video = this.uploadedFiles[0];
    this.isCheckout = true;
    if (this.bill.codeReturn) {
      const orderReturnDetail: any = {
        returnId: 0,
        orderId: 0,
        orderDetailResponseClientTemps: [],
      };
      orderReturnDetail.returnId = this.bill.codeReturn;
      this.billreturn.orderDetail.forEach((item) => {
        if (item.quantity > 0) {
          orderReturnDetail.orderDetailResponseClientTemps.push({
            id: item.id,
            returnQuantity: item.quantity,
          });
        }
      });
      orderReturnDetail.orderId = this.bill.id;
      this.orderService.addReturnDetail(orderReturn).subscribe({
        next: (res) => {
          this.loadBill();
          this.isCheckout = false;
          this.ngOnDestroy();
          this.toastrService.info("Gửi yêu cầu trả hàng thành công!");
        },
        error: () => {
          this.isCheckout = false;
          this.toastrService.error("Vui lòng thử lại sau!");
        },
      });
    } else {
      this.orderService.addReturn(orderReturn).subscribe({
        next: (res) => {
          this.loadBill();
          this.isCheckout = false;
          this.ngOnDestroy();
          this.toastrService.info("Gửi yêu cầu trả hàng thành công!");
        },
        error: () => {
          this.isCheckout = false;
          this.toastrService.error("Vui lòng thử lại sau!");
        },
      });
    }
  }
}
