import { Component, OnInit, Input, Output, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import { Router } from "@angular/router";

import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { ToastrService } from "ngx-toastr";
import {
  CANCELLED,
  CONFIRMED,
  IN_PROGRESS,
  UNCONFIRMED,
} from "src/app/core/constant/order.constant";
import { GhnService } from "src/app/core/services/ghn.service";
import { OrderService } from "src/app/core/services/order.service";
import { BillComponent } from "src/app/pages/bill/bill.component";

@Component({
  selector: "app-transaction",
  templateUrl: "./transaction.component.html",
  styleUrls: ["./transaction.component.scss"],
})
export class TransactionComponent implements OnInit {
  @Input() orders: any[];
  count: number = 0;
  @Input() onLoadingChanged: (loading: boolean, count?: any) => void;
  order: any;
  modalOrderVar: NgbModalRef;
  modalOrderDetail: NgbModalRef;
  modalEdit: NgbModalRef;
  formData: FormGroup;
  @ViewChild("content") content: any;
  @ViewChild("edit") edit: any;
  selectProvince: any;
  selectDistrict: any;
  selectWard: any;
  costShip: number;
  constructor(
    private modalService: NgbModal,
    private orderService: OrderService,
    private toasrService: ToastrService,
    private ghnService: GhnService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {}

  ngOnInit() {
    this.ghnService.getProvince().subscribe((res) => {
      this.selectProvince = res.data;
    });
    this.formData = this.formBuilder.group({
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
      note: [""],
    });
  }
  onChangeProvince(provinceId: number) {
    this.formData.get("district").setValue("");
    this.formData.get("ward").setValue("");
    if (provinceId !== null) {
      this.ghnService.getDistrict(provinceId).subscribe((res) => {
        this.selectDistrict = res.data;
      });
    }
  }
  onChangeDistrict(districtId: number) {
    this.formData.get("ward").setValue("");
    if (districtId !== null) {
      this.ghnService.getWard(districtId).subscribe((res) => {
        this.selectWard = res.data;
      });
    }
  }
  onChangeWard() {
    if (
      this.formData.get("district").valid &&
      this.formData.get("ward").valid
    ) {
      const district = this.formData.get("district").value;
      const ward = this.formData.get("ward").value;
      this.ghnService.getServiceFee(district, ward).subscribe({
        next: (res) => {
          this.costShip = res.data.total;
        },
        error: (err) => {
          this.toasrService.error(
            "Api giao hàng nhanh lỗi không tính được tiền ship"
          );
        },
      });
    } else {
      this.costShip = 0;
    }
  }
  confirm(data: any) {
    this.onLoadingChanged(true);
    const id: any[] = [];
    id.push(data.id);
    this.orderService.updateStatusConfirm(id).subscribe({
      next: (res) => {
        this.toasrService.info("Xác nhận đơn hàng " + data.code);
        this.orderService.findByStatus(UNCONFIRMED).subscribe({
          next: (res) => {
            this.orders = res;
            this.onLoadingChanged(false, res.length);
          },
        });
      },
      error: (err) => {
        this.toasrService.error("Vui lòng thử lại");
        this.onLoadingChanged(false);
      },
    });
  }
  cancel(data: any) {
    this.onLoadingChanged(true);
    this.orderService.updateStatusHuy(data.id).subscribe({
      next: (res) => {
        this.toasrService.info("Hủy đơn hàng " + data.code);
        this.orderService.findByStatus(UNCONFIRMED).subscribe({
          next: (res) => {
            this.orders = res;
            this.onLoadingChanged(false);
          },
        });
      },
      error: (err) => {
        this.toasrService.error("Vui lòng thử lại");
        this.onLoadingChanged(false);
      },
    });
  }
  inProgress(data: any) {
    this.onLoadingChanged(true);
    this.orderService.updateStatusXacGiaoHang(data.id).subscribe({
      next: (res) => {
        window.open(res.message, "_blank");
        this.toasrService.info("Xác nhận giao đơn hàng " + data.code);
        this.orderService.findByStatus(CONFIRMED).subscribe({
          next: (res) => {
            this.orders = res;
            this.onLoadingChanged(false);
          },
        });
      },
      error: (err) => {
        this.toasrService.error("Vui lòng thử lại");
        this.onLoadingChanged(false);
      },
    });
  }
  completed(data: any) {
    this.onLoadingChanged(true);
    const id: any[] = [];
    id.push(data.id);
    this.orderService.updateStatusGiaoThanhCong(id).subscribe({
      next: (res) => {
        this.toasrService.info("Đơn hàng " + data.code + " giao thành công");
        this.orderService.findByStatus(IN_PROGRESS).subscribe({
          next: (res) => {
            this.orders = res;
            this.onLoadingChanged(false);
          },
        });
      },
      error: (err) => {
        this.toasrService.error("Vui lòng thử lại");
        this.onLoadingChanged(false);
      },
    });
  }
  failed(data: any) {
    this.onLoadingChanged(true);
    const id: any[] = [];
    id.push(data.id);
    this.orderService.updateStatusGiaoThatBai(id).subscribe({
      next: (res) => {
        this.toasrService.info(
          "Xác nhận đơn hàng " + data.code + " giao hàng thất bại"
        );
        this.orderService.findByStatus(IN_PROGRESS).subscribe({
          next: (res) => {
            this.orders = res;
            this.onLoadingChanged(false);
          },
        });
      },
      error: (err) => {
        this.toasrService.error("Vui lòng thử lại");
        this.onLoadingChanged(false);
      },
    });
  }
  /**
   * Open modal
   * @param content modal content
   */
  openModal(content: any, data: any) {
    this.onLoadingChanged(true);
    if (data.code) {
      this.orderService.findOrderByCode(data.code).subscribe({
        next: (value) => {
          this.order = value;
          console.log(value);

          this.modalService.open(content, { size: "lg", centered: true });
          this.onLoadingChanged(false);
        },
        error: (error) => {
          this.onLoadingChanged(false);
        },
      });
    }
  }
  printPage() {
    window.print();
  }
  openBill(data: any) {
    this.modalOrderVar = this.modalService.open(BillComponent, {
      size: "md",
    });
    this.modalOrderVar.componentInstance.billData = data;
  }
  openDetail(data: any) {
    this.modalOrderDetail = this.modalService.open(this.content, {
      size: "md",
    });
  }

  getTamTinh(): number {
    let total: number = 0;
    this.order?.orderDetail?.forEach((value) => {
      total +=
        (value?.unitprice - (value?.unitprice * value?.discount) / 100) *
        value.quantity;
    });
    return total;
  }
  save() {
    if (this.formData.get("name").invalid) {
      this.toasrService.warning("Thiếu thông tin khách hàng");
      return;
    }
    if (this.formData.get("phone").invalid) {
      this.toasrService.warning("Thiếu số điện thoại");
      return;
    }
    if (
      this.formData.get("province").invalid ||
      this.formData.get("district").invalid ||
      this.formData.get("ward").invalid ||
      this.formData.get("address").invalid
    ) {
      this.toasrService.warning("Thiếu địa chỉ");
      return;
    }
    this.onLoadingChanged(true);
    const order: any = {
      id: 0,
      code: "",
      freight: 0,
      shipName: "",
      address: "",
      cityName: "",
      districtName: "",
      wardName: "",
      shipPhone: "",
      note: "",
      paymentType: "",
      total: 0,
      statusPay: 0,
      saleForm: false,
      idCity: 0,
      idDistrict: 0,
      idWard: "",
      voucher: 0,
      employeed: 0,
      customer: 0,
      status: 0,
      lstProductDetail: [],
    };
    order.customer = this.order.customer.id;
    order.paymentType = this.order.paymentType;
    order.saleForm = this.order.saleForm;
    order.idCity = this.formData.get("province").value;
    order.idDistrict = this.formData.get("district").value;
    order.idWard = this.formData.get("ward").value;
    order.address = this.formData.get("address").value;
    this.selectProvince.forEach((value) => {
      if (value.ProvinceID === this.formData.get("province").value) {
        order.cityName = value.ProvinceName;
      }
    });
    this.selectDistrict.forEach((value) => {
      if (value.DistrictID === this.formData.get("district").value) {
        order.districtName = value.DistrictName;
      }
    });
    this.selectWard.forEach((value) => {
      if (value.WardCode === this.formData.get("ward").value) {
        order.wardName = value.WardName;
      }
    });
    if (this.costShip) {
      order.freight = this.costShip;
    }
    order.shipName = this.formData.get("name").value;
    order.shipPhone = this.formData.get("phone").value;
    order.status = this.order.status;
    order.note = this.formData.get("note").value;
    order.total = this.order.total;
    order.statusPay = this.order.statusPay;
    if (this.order.voucher) {
      order.voucher = this.order.voucher.id;
    } else {
      order.voucher = 0;
    }
    order.employeed = this.order.employeed;
    this.order.orderDetail.forEach((value) => {
      const productDetail: any = {};
      productDetail.quantity = value.quantity;
      productDetail.id = value.productDetailId.id;
      order.lstProductDetail.push(productDetail);
    });
    order.id = this.order.id;
    console.log(order);
    if (order) {
      this.orderService.updateOrder(order).subscribe({
        next: (value) => {
          this.modalEdit.close();
          this.orderService.findOrderByCode(value.code).subscribe({
            next: (value) => {
              this.order = value;
              this.onLoadingChanged(false);
              this.toasrService.success("Thành công");
            },
            error: (error) => {
              this.onLoadingChanged(false);
            },
          });
        },
        error: (error) => {
          this.toasrService.error("Thất bại");
          this.onLoadingChanged(false);
        },
      });
    }
  }

  openModalEdit(data: any) {
    this.onChangeProvince(data.idCity);
    this.onChangeDistrict(data.idDistrict);
    this.formData = this.formBuilder.group({
      name: [
        data.shipName,
        [
          Validators.required,
          Validators.pattern(
            "^[AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+ [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]+(?: [AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ][aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz]*)*"
          ),
        ],
      ],
      phone: [
        data.shipPhone,
        [
          Validators.required,
          Validators.pattern(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g),
        ],
      ],
      address: [data.address, [Validators.required, Validators.maxLength(50)]],
      province: [data.idCity, Validators.required],
      district: [data.idDistrict, Validators.required],
      ward: [data.idWard, Validators.required],
      note: [data.note],
    });
    this.modalEdit = this.modalService.open(this.edit, {
      size: "md",
      centered: true,
    });
  }
  getTotalVoucher(): number {
    let total: number = 0;
    if (this.order.voucher && this.order.voucher.type === false) {
      total =
        ((this.order.total - this.getTamTinh()) * this.order.voucher.discount) /
        100;
      if (this.order.voucher.max && total > this.order.voucher.max) {
        total = this.order.voucher.max;
      }
    } else if (this.order.voucher && this.order.voucher.type === true) {
      total = this.order.voucher.discount;
    }
    return total;
  }

  exchange(bill: any) {
    this.router.navigate(["exchange"], {
      queryParams: {
        code: bill.code,
      },
    });
  }
}
