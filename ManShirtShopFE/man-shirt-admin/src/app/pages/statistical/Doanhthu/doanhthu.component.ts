import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup,ReactiveFormsModule } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { sanphamSta ,sanphamStatic} from "src/app/core/models/sanpham.models";
import { discount} from "src/app/core/models/discount.models";
import { DoanhthuService } from "src/app/core/services/doanhthu.service";
import { DiscountService } from "src/app/core/services/discount.service";
import Swal from "sweetalert2";
import { ToastrService } from "ngx-toastr";
import * as XLSX from 'xlsx';
import { element } from "protractor";
@Component({
  selector: "app-formm-type",
  templateUrl: "./doanhthu.component.html",
  styleUrls: ["./doanhthu.component.scss"],
})
export class doanhthuComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  loading: boolean = false;
  SanphamSta: any = {
    startDate: null,
    endDate: null,
  };

  discount: discount = {
    id: null,
    name: null,
    startDate: new Date(),
    endDate: new Date(),
    description: null,
    createTime: new Date(),
    updateTime: null,
    createBy: null,
    updateBy: null,
    status: 0,
  };

  sanphamDiscount : any ={
    discountId: this.discount.id
  }

  discountData:discount []
  sanphamStaData: sanphamSta[];
  sanphamStaDataDp: sanphamSta[];
  formData: FormGroup;
  formData1: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = 'form.xlsx';
  totalItems = 0;
  itemsPerPage = 5;
  currentPage = 1;
  totalItems1 = 0;
  itemsPerPage1= 5;
  currentPage1 = 1;

  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private DoanhthuService: DoanhthuService,
    private DiscountService: DiscountService,
    private ToastrService:ToastrService,
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [
    ];
    this.formData = this.formBuilder.group({
      startDate: ["", [Validators.required]],
      endDate: ["", [Validators.required]],
    });
    this.formData1 = this.formBuilder.group({
      discountId: ["", [Validators.required]],

    });
    this.loaddiscountData();
  }
  order:any;
  openModal(content: any, sanphamsta: any) {
    if (sanphamsta.id) {
      this.DoanhthuService.GetOrder(sanphamsta.id).subscribe({
        next: (value) => {
          this.order = value;
          console.log(value);
          this.modalService.open(content, { size: "lg", centered: true });
        },
        error: (error) => {

        },
      });
    }
  }

  loaddiscountData(): void {
    this.DiscountService.getDiscount().subscribe((data1) => {
      this.discountData = data1;
    });
  }


  getOrder : any = []
  getProductTime : any = []
  saveProductTime() {
    this.submitted = true;
    if (this.formData.valid) {
      if(!this.ValidateDate()){
        this.ToastrService.error("ngày bắt đầu phải lớn hơn ngày kết thúc");
        return;
      }
    console.log("helooo");
      const startDate = this.formData.get("startDate").value;
      this.SanphamSta.startDate = startDate;
      console.log(startDate);
      const endDate = this.formData.get("endDate").value;
      this.SanphamSta.endDate = endDate;

        this.DoanhthuService.GetSanphamsta(this.SanphamSta).subscribe({
          next: (res) => {
             this.getProductTime = res;
             this.totalItems1 = this.getProductTime.order.length;
             this.sanphamStaDataDp = this.getProductTime.order.slice(
               (this.currentPage1 - 1) * this.itemsPerPage1,
               this.currentPage1 * this.itemsPerPage1
             );
             console.log(this.getProductTime.order);

            Swal.fire("Success!", " added successfully.", "success");

          },
          error: (err) => {
            Swal.fire("Error!", "Failed to add .", "error");
          },
        });
      }
        console.log(this.getProductTime);
  }

  getProductDiscount : any = [];
  getProductDiscount1 : any = [];
  saveProductByDiscount() {
    this.submitted = true;
    console.log("helooo");
      const DiscountID = this.formData1.get("discountId").value;
      this.sanphamDiscount.discountId = DiscountID;
      console.log(DiscountID);
        this.DoanhthuService.GetSanphamDiscount(this.sanphamDiscount.discountId).subscribe({
          next: (res) => {
             this.getProductDiscount = res;

             this.totalItems = this.getProductDiscount.order.length;
             this.getProductDiscount1 = this.getProductDiscount.order.slice(
               (this.currentPage - 1) * this.itemsPerPage,
               this.currentPage * this.itemsPerPage
             );
            Swal.fire("Success!", " added successfully.", "success");

          },
          error: (err) => {
            Swal.fire("Error!", "Failed to add .", "error");
          },
        });

        console.log(this.getProductTime);
  }
  onPageChange(event): void {
    this.currentPage1 = event;
    this.sanphamStaDataDp = this.getProductTime.order.slice(
      (this.currentPage1 - 1) * this.itemsPerPage1,
      this.currentPage1 * this.itemsPerPage1
    );
  }
  onPageChange1(event): void {
    this.currentPage = event;
    this.getProductDiscount1 = this.getProductDiscount.order.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }
  ValidateDate(){
    const startDate = this.formData.get("startDate").value;
    const start = new Date(startDate)
    const endDate = this.formData.get("endDate").value;
    const end = new Date(endDate)
    if(start > end ){
       return false;
    }
    return true;

  }
  getTamTinh(): number {
    let total: number = 0;
    this.order?.orderResponeAdmin?.orderDetail?.forEach((value) => {
      total +=
        (value?.unitprice - (value?.unitprice * value?.discount) / 100) *
        value.quantity;
    });
    return total;
  }
  getTotalVoucher(): number {
    let total: number = 0;
    if (this.order.orderResponeAdmin.voucher && this.order.orderResponeAdmin.voucher.type === false) {
      total =
        ((this.order.orderResponeAdmin.total - this.getTamTinh()) * this.order.orderResponeAdmin.voucher.discount) /
        100;
      if (this.order.orderResponeAdmin.voucher.max && total > this.order.orderResponeAdmin.voucher.max) {
        total = this.order.orderResponeAdmin.voucher.max;
      }
    } else if (this.order.orderResponeAdmin.voucher && this.order.orderResponeAdmin.voucher.type === true) {
      total = this.order.orderResponeAdmin.voucher.discount;
    } console.log(total)
    return total;

  }
}
