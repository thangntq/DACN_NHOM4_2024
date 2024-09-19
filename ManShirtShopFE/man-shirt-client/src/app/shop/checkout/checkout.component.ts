import {
  Component,
  Inject,
  OnInit,
  PLATFORM_ID,
  TemplateRef,
  ViewChild,
} from "@angular/core";
import {
  FormGroup,
  FormBuilder,
  Validators,
  FormControl,
} from "@angular/forms";
import { Observable, of } from "rxjs";
import { IPayPalConfig, ICreateOrderRequest } from "ngx-paypal";
import { environment } from "../../../environments/environment";
import { Product, ProductDetail } from "../../shared/classes/product";
import { ProductService } from "../../shared/services/product.service";
import { OrderService } from "../../shared/services/order.service";
import { GhnService } from "src/app/shared/services/ghn.service";
import { ToastrService } from "ngx-toastr";
import { ActivatedRoute, Router } from "@angular/router";
import { map } from "rxjs/operators";
import { isPlatformBrowser } from "@angular/common";
import { ModalDismissReasons, NgbModal } from "@ng-bootstrap/ng-bootstrap";

@Component({
  selector: "app-checkout",
  templateUrl: "./checkout.component.html",
  styleUrls: ["./checkout.component.scss"],
})
export class CheckoutComponent implements OnInit {
  public checkoutForm: FormGroup;
  public productDetails: ProductDetail[] = [];
  public productDetailsCheckout: ProductDetail[] = [];
  checkoutData: any;
  public products: Product[] = [];
  order: any;
  total: number = 0;
  addresss: any;
  provinces: any;
  districts: any;
  wards: any;
  costShip: number = 0;
  isSubmitting = false;
  selectedAddress: any;
  costShipError: boolean = false;
  checkoutCode: any;
  listVoucher: any;
  selectedVoucher: any;
  @ViewChild("voucher", { static: false }) voucherModal: TemplateRef<any>;
  public modalOpen: boolean = false;
  public closeResult: string;
  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private fb: FormBuilder,
    public productService: ProductService,
    private ghnService: GhnService,
    private toastrService: ToastrService,
    private route: ActivatedRoute,
    private orderService: OrderService,
    private router: Router,
    private modalService: NgbModal
  ) {
    this.checkoutCode = this.route.snapshot.params["code"];
    if (this.checkoutCode) {
      this.orderService.getCheckout(this.checkoutCode).subscribe({
        next: (value) => {
          if (value.status && value.order) {
            if (
              value.order.statusPay == 1 ||
              value.order.paymentType == "COD"
            ) {
              this.router.navigate(["order/success"], {
                queryParams: {
                  bill_code: value.order.code,
                },
              });
            } else {
              this.order = value.order;
              console.log(this.order);
            }
          } else if (value.status && value.checkOut) {
            this.checkoutData = value.checkOut;
            const request: any = [];
            this.checkoutData.checkOutDetails.forEach((item) => {
              request.push({
                id: item.productDetail.id,
                quantity: item.quantity,
              });
            });
            if (request) {
              this.orderService.getAllVoucher(request).subscribe({
                next: (res) => {
                  this.listVoucher = res;
                  console.log(this.listVoucher);
                },
                error: (err) => {},
              });
            }
          } else {
            this.router.navigate(["checkouts/error"], {});
          }
        },
        error: (err) => {
          this.router.navigate(["checkouts/error"], {});
        },
      });
    }
    this.checkoutForm = this.fb.group({
      addressNode: [0, Validators.required],
      name: [
        "",
        [
          Validators.required,
          Validators.pattern(
            "^[AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+ [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+(?: [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]*)*"
          ),
        ],
      ],
      phone: [
        "",
        [
          Validators.required,
          Validators.pattern(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g),
        ],
      ],
      address: ["", [Validators.required, Validators.maxLength(50)]],
      province: ["", Validators.required],
      district: ["", Validators.required],
      ward: ["", Validators.required],
      payment: ["VNPAY", Validators.required],
      note: [""],
    });
  }

  ngOnInit(): void {
    this.productService.getProducts().subscribe({
      next: (value) => {
        this.products = value;
      },
      error: (message) => {
        this.toastrService.error("Lỗi khi lấy danh sách sản phẩm");
      },
    });
    this.loadProvinces();
    this.loadAddress();
  }

  checkout() {
    let order: any = {};
    if (this.order) {
      this.isSubmitting = true;
      order.freight = this.order.freight;
      order.shipName = this.order.shipName;
      order.shipPhone = this.order.shipPhone;
      order.note = this.order.note;
      order.paymentType = this.checkoutForm.controls["payment"].value;
      order.address = {};
      order.address.address = this.order.address;
      order.address.idCity = this.order.idCity;
      order.address.cityName = this.order.cityName;
      order.address.idDistrict = this.order.idDistrict;
      order.address.districtName = this.order.districtName;
      order.address.idWard = this.order.idWard;
      order.address.wardName = this.order.wardName;
      order.address.other = "";
      order.lstProductDetail = this.order.orderDetail;
      order.id = this.order.id;
      order.code = this.order.code;
      order.voucher = this.order?.voucher?.id;
      order.statusPay = this.order.statusPay;
      // this.checkoutData.checkOutDetails.forEach((item) => {
      //   const productDetail: any = {};
      //   productDetail.id = item.productDetail.id;
      //   productDetail.quantity = item.quantity;
      //   order.lstProductDetail.push(productDetail);
      // });
      order.codeCheckOut = this.checkoutCode;
    } else {
      if (this.checkoutForm.controls["name"].errors?.required) {
        this.toastrService.warning("Tên không được bỏ trống");
        return;
      }
      if (this.checkoutForm.controls["name"].errors?.pattern) {
        this.toastrService.warning("Vui lòng nhập chính xác tên");
        return;
      }
      if (this.checkoutForm.controls["phone"].errors?.required) {
        this.toastrService.warning("Số điện thoại không được bỏ trống");
        return;
      }
      if (this.checkoutForm.controls["phone"].errors?.pattern) {
        this.toastrService.warning("Số điện thoại không đúng");
        return;
      }
      if (
        this.checkoutForm.controls["address"].invalid ||
        this.checkoutForm.controls["province"].invalid ||
        this.checkoutForm.controls["district"].invalid ||
        this.checkoutForm.controls["ward"].invalid
      ) {
        this.toastrService.warning("Địa chỉ trống");
        return;
      }
      if (this.costShip === 0 || this.costShipError) {
        this.toastrService.warning("Chưa tính được tiền ship");
        return;
      }
      this.isSubmitting = true;
      order.freight = this.costShip;
      order.shipName = this.checkoutForm.controls["name"].value;
      order.shipPhone = this.checkoutForm.controls["phone"].value;
      order.note = this.checkoutForm.controls["note"].value;
      order.paymentType = this.checkoutForm.controls["payment"].value;
      order.address = {};
      if (this.checkoutForm.controls["addressNode"].value == 0) {
        order.address.id = null;
      } else {
        order.address.id = this.checkoutForm.controls["addressNode"].value;
      }
      order.address.address = this.checkoutForm.controls["address"].value;
      order.address.idCity = this.checkoutForm.controls["province"].value;
      this.provinces.forEach((element) => {
        if (element.ProvinceID == order.address.idCity) {
          order.address.cityName = element.ProvinceName;
        }
      });
      order.address.idDistrict = this.checkoutForm.controls["district"].value;
      this.districts.forEach((element) => {
        if (element.DistrictID == order.address.idDistrict) {
          order.address.districtName = element.DistrictName;
        }
      });
      order.address.idWard = this.checkoutForm.controls["ward"].value;
      this.wards.forEach((element) => {
        if (element.WardCode === order.address.idWard) {
          order.address.wardName = element.WardName;
        }
      });
      order.address.other = "";
      order.lstProductDetail = [];
      order.id = 0;
      order.code = "";
      order.voucher = this.selectedVoucher ? this.selectedVoucher.id : 0;
      order.statusPay = 0;
      this.checkoutData.checkOutDetails.forEach((item) => {
        const productDetail: any = {};
        productDetail.id = item.productDetail.id;
        productDetail.quantity = item.quantity;
        order.lstProductDetail.push(productDetail);
      });
      order.codeCheckOut = this.checkoutData.code;
    }
    console.log(order);

    this.orderService.checkout(order).subscribe((result) => {
      if (result.status && result.responseData) {
        order.lstProductDetail.forEach((productDetail) => {
          this.productService.removeCartItemById(productDetail.id);
        });
        if (result.responseData.type) {
          window.location.href = result.responseData.link;
        } else {
          this.router.navigate(["order/success"], {
            queryParams: {
              bill_code: result.responseData.detail.code,
            },
          });
        }
      } else {
        this.toastrService.error("Lỗi thanh toán");
        this.isSubmitting = false;
      }
      this.isSubmitting = false;
    });
  }
  onAddressChange() {
    if (this.checkoutForm.controls["addressNode"].value == 0) {
      this.selectedAddress = null;
      const payment = this.checkoutForm.controls["payment"].value;
      const note = this.checkoutForm.controls["note"].value;
      this.costShip = 0;
      this.districts = null;
      this.wards = null;
      this.checkoutForm = this.fb.group({
        addressNode: [0, Validators.required],
        name: [
          "",
          [
            Validators.required,
            Validators.pattern(
              "^[AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+ [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+(?: [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]*)*"
            ),
          ],
        ],
        phone: [
          "",
          [
            Validators.required,
            Validators.pattern(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g),
          ],
        ],
        address: ["", [Validators.required, Validators.maxLength(50)]],
        province: ["", Validators.required],
        district: ["", Validators.required],
        ward: ["", Validators.required],
        payment: [payment, Validators.required],
        note: [note],
      });
    } else {
      this.checkoutForm.controls["phone"] = new FormControl("");
      this.selectedAddress = this.addresss.find(
        (address) =>
          address.id == this.checkoutForm.controls["addressNode"].value
      );
      this.districts = null;
      this.wards = null;
      this.loadDistricts(this.selectedAddress?.idCity);
      this.loadWards(this.selectedAddress?.idDistrict);
      this.checkoutForm.controls["name"].setValue(
        this.selectedAddress?.fullName
      );
      this.checkoutForm.controls["phone"].setValue(this.selectedAddress?.phone);
      this.checkoutForm.controls["province"].setValue(
        this.selectedAddress?.idCity
      );
      this.checkoutForm.controls["district"].setValue(
        this.selectedAddress?.idDistrict
      );
      this.checkoutForm.controls["ward"].setValue(this.selectedAddress?.idWard);
      this.onWardChange(this.selectedAddress?.idWard);
      this.checkoutForm.controls["address"].setValue(
        this.selectedAddress?.address
      );
    }
  }
  private loadAddress() {
    this.orderService.getAddress().subscribe({
      next: (res) => {
        this.addresss = res;
        this.addresss.forEach((element) => {
          if (element.default) {
            this.checkoutForm.controls["addressNode"].setValue(element.id);
            this.onAddressChange();
            return;
          }
        });
      },
      error: (err) => {
        this.toastrService.error("Lỗi api address");
      },
    });
  }
  private loadProvinces() {
    this.ghnService.getProvinces.subscribe({
      next: (res) => {
        this.provinces = res.data;
      },
      error: (err) => {
        this.toastrService.error("Lỗi api tỉnh");
      },
    });
  }

  public onProvinceChange(provinceId: number) {
    this.loadDistricts(provinceId);
    this.checkoutForm.controls["district"].setValue("");
    this.checkoutForm.controls["ward"].setValue("");
    this.costShip = 0;
  }

  private loadDistricts(provinceId: number) {
    this.ghnService.getDistricts(provinceId).subscribe({
      next: (res) => {
        this.districts = res.data;
      },
      error: (err) => {
        this.toastrService.error("Lỗi api huyện");
      },
    });
  }

  public onDistrictChange(districtId: number) {
    this.loadWards(districtId);
    this.checkoutForm.controls["ward"].setValue("");
    this.costShip = 0;
  }

  private loadWards(districtId: number) {
    this.ghnService.getWards(districtId).subscribe({
      next: (res) => {
        this.wards = res.data;
      },
      error: (err) => {
        this.toastrService.error("Lỗi api xã");
      },
    });
  }
  public onWardChange(wardCode: number) {
    this.ghnService.getServiceFee(152, wardCode).subscribe({
      next: (res) => {
        this.costShip = res.data.total;
      },
      error: (err) => {
        this.costShipError = true;
        this.toastrService.error("Lỗi api tính phí ship");
      },
    });
  }
  public get getTotal(): Observable<number> {
    let totalPrice = 0;
    if (this.checkoutData && this.checkoutData.checkOutDetails) {
      this.checkoutData.checkOutDetails.forEach((item) => {
        totalPrice +=
          item.quantity *
          item.productDetail.giaSp *
          (1 - item.productDetail.discountSp / 100);
      });
      this.total =
        totalPrice +
        this.costShip -
        (this.selectedVoucher ? this.onChangeVoucher() : 0);
      return of(totalPrice);
    }
    return of(0);
  }
  getTamTinh(): number {
    let totalPrice = 0;
    if (this.checkoutData && this.checkoutData.checkOutDetails) {
      this.checkoutData.checkOutDetails.forEach((item) => {
        totalPrice +=
          item.quantity *
          item.productDetail.giaSp *
          (1 - item.productDetail.discountSp / 100);
      });
      return totalPrice;
    }
    return 0;
  }
  public get getTotalOrder(): Observable<number> {
    let totalPrice = 0;
    if (this.order && this.order.orderDetail) {
      this.order.orderDetail.forEach((item) => {
        totalPrice +=
          (item?.unitprice - (item?.unitprice * item?.discount) / 100) *
          item.quantity;
      });
      return of(totalPrice);
    }
    return of(0);
  }
  getTamTinhOrder(): number {
    let totalPrice = 0;
    if (this.order && this.order.orderDetail) {
      this.order.orderDetail.forEach((item) => {
        totalPrice +=
          (item?.unitprice - (item?.unitprice * item?.discount) / 100) *
          item.quantity;
      });
      return totalPrice;
    }
    return 0;
  }
  getImageProductDetail(product: any, productDetail: any) {
    return product.productImage.filter(
      (image) => image.color.id === productDetail.color.id
    );
  }
  openModal() {
    this.modalOpen = true;
    if (isPlatformBrowser(this.platformId)) {
      // For SSR
      this.modalService
        .open(this.voucherModal, {
          size: "lg",
          ariaLabelledBy: "voucher-modal",
          centered: true,
          windowClass: "Address",
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
  addVoucher(voucher: any) {
    this.selectedVoucher = voucher;
    console.log(this.selectedVoucher);
    // this.ngOnDestroy();
  }
  removeVoucher(voucher: any) {
    this.selectedVoucher = null;
  }
  ngOnDestroy() {
    if (this.modalOpen) {
      this.modalService.dismissAll();
    }
  }
  onChangeVoucher() {
    let costVoucher = 0;
    if (!this.selectedVoucher.type) {
      costVoucher = (this.selectedVoucher.discount * this.getTamTinh()) / 100;
      if (this.selectedVoucher.max && costVoucher > this.selectedVoucher.max) {
        costVoucher = this.selectedVoucher.max;
        return costVoucher;
      }
    } else {
      costVoucher = this.selectedVoucher.discount;
      return costVoucher;
    }
    return costVoucher;
  }
  voucherOrder() {
    let costVoucher = 0;
    if (!this.order.voucher.type) {
      costVoucher =
        (this.order.voucher.discount * this.getTamTinhOrder()) / 100;
      if (this.order.voucher.max && costVoucher > this.order.voucher.max) {
        costVoucher = this.order.voucher.max;
        return costVoucher;
      }
    } else {
      costVoucher = this.order.voucher.discount;
      return costVoucher;
    }
    return costVoucher;
  }
}
