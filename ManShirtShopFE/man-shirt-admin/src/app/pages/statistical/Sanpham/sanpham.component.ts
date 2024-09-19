import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup,ReactiveFormsModule } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { sanphamSta ,sanphamStatic} from "src/app/core/models/sanpham.models";
import { discount} from "src/app/core/models/discount.models";
import { SanphamStacService } from "src/app/core/services/SanphamStac.service";
import { DiscountService } from "src/app/core/services/discount.service";
import Swal from "sweetalert2";
import * as XLSX from 'xlsx';
import { ToastrService } from "ngx-toastr";
@Component({
  selector: "app-formm-type",
  templateUrl: "./sanpham.component.html",
  styleUrls: ["./sanpham.component.scss"],
})
export class sanphamComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
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
  totalItems = 0;
  itemsPerPage = 3;
  currentPage = 1;
  totalItems1 = 0;
  itemsPerPage1= 3;
  currentPage1 = 1;
  fileName = 'form.xlsx';
  @ViewChild("content") content: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private sanphamStacService: SanphamStacService,
    private DiscountService: DiscountService,
    private ToastrService:ToastrService,
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [
      { label: "Quản lý" },
      { label: "Thuộc tính sản phẩm"},
      { label: "Hình dáng", active: true },
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


  loaddiscountData(): void {
    this.DiscountService.getDiscount().subscribe((data1) => {
      this.discountData = data1;
    });
  }


  getProductTime : any[] = [];
  saveProductTime() {
    this.submitted = true;
    if (this.formData.valid) {
      if(!this.ValidateDate()){
        this.ToastrService.error("ngày bắt đầu phải lớn hơn ngày kết thúc");
        return;
      }
      const startDate = this.formData.get("startDate").value;
      this.SanphamSta.startDate = startDate;
      console.log(startDate);
      const endDate = this.formData.get("endDate").value;
      this.SanphamSta.endDate = endDate;

        this.sanphamStacService.GetSanphamsta(this.SanphamSta).subscribe({
          next: (res) => {
             this.getProductTime = res;
             console.log(this.getProductTime);
             this.totalItems1 = this.getProductTime.length;
             this.sanphamStaDataDp = this.getProductTime.slice(
               (this.currentPage1 - 1) * this.itemsPerPage1,
               this.currentPage1 * this.itemsPerPage1
             );
            Swal.fire("Success!", " added successfully.", "success");

          },
          error: (err) => {
            Swal.fire("Error!", "Failed to add .", "error");
          },
        });
      }
        console.log(this.getProductTime);
  }

  getProductDiscount : any[] = [];
  getProductDiscount1 : any[] = [];
  saveProductByDiscount() {
    this.submitted = true;
    console.log("helooo");
      const DiscountID = this.formData1.get("discountId").value;
      this.sanphamDiscount.discountId = DiscountID;
      console.log(DiscountID);
        this.sanphamStacService.GetSanphamDiscount(this.sanphamDiscount.discountId).subscribe({
          next: (res) => {
             this.getProductDiscount = res;
             console.log(this.getProductDiscount);
             this.totalItems = this.getProductDiscount.length;
             this.getProductDiscount1 = this.getProductDiscount.slice(
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
    this.sanphamStaDataDp = this.getProductTime.slice(
      (this.currentPage1 - 1) * this.itemsPerPage1,
      this.currentPage1 * this.itemsPerPage1
    );
  }
  onPageChange1(event): void {
    this.currentPage = event;
    this.getProductDiscount1 = this.getProductDiscount.slice(
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
}
