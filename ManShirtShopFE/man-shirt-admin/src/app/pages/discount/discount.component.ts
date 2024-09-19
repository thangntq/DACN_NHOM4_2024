import { Component, OnInit, Pipe, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { filter } from "rxjs/operators";
import { formatDate } from "@angular/common";
import { discount } from "src/app/core/models/discount.models";
import { DiscountService } from "src/app/core/services/discount.service";
import { ProductRespone } from "src/app/core/models/product.model";
import { ProductService } from "src/app/core/services/product.service";
import { discountproduct } from "src/app/core/models/discountproduct.models";
import { ProductdiscountService } from "src/app/core/services/productdiscount.service";
import { ToastrService } from "ngx-toastr";
import Swal from "sweetalert2";
import * as XLSX from "xlsx";

@Component({
  selector: "app-discount",
  templateUrl: "./discount.component.html",
  styleUrls: ["./discount.component.scss"],
})
export class discountComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;

  selectDiscount: any;
  discountID: number;
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

  producdiscount: discountproduct = {
    id: null,
    percent: null,
    discountId: null,
    productId: [],
  };

  discountproductData: discountproduct[];
  discountproductData1: discountproduct[];
  typePageDP: discountproduct[];
  productData: ProductRespone[];
  modalCollar: NgbModalRef;
  discountData: discount[];
  osTypeDataPage: discount[];
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  formData1: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = "discount.xlsx";
  discountSelect: any;
  @ViewChild("content") content: any;
  @ViewChild("content1") content1: any;
  @ViewChild("content2") content2: any;
  @ViewChild("content3") content3: any;
  @ViewChild("collarModal") collarModal: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private discountService: DiscountService,
    private productService: ProductService,
    private productdiscountServices: ProductdiscountService,
    private ToastrService:ToastrService,
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [];
    this.formData = this.formBuilder.group({
      name: ["", [Validators.required]],
      description: ["", [Validators.required]],
      startDate: ["", [Validators.required]],
      endDate: ["", [Validators.required]],
    });

    this.formData1 = this.formBuilder.group({
      percent: ["", [Validators.required]],
      productId: ["", [Validators.required]],
      discountId: ["", [Validators.required]],
    });

    this.loadData();
    this.loadDataProduct();

  }
  loadData(): void {
    this.discountService.getDiscount().subscribe((data) => {
      this.discountData = data;
      this.totalItems = data.length;
      this.osTypeDataPage = data.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage
      );
      console.log(this.osTypeDataPage);
    });
  }
  loaddiscountProductData(): void {
    this.productdiscountServices.getDiscountProduct().subscribe((data1) => {
      this.discountproductData = data1;
      this.totalItems = data1.length;
      this.typePageDP = data1.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage
      );
    });
  }

  loadDataProduct(): void {
    this.productdiscountServices.getDiscountProduct1().subscribe((data) => {
      this.productData = data;
      this.totalItems = data.length;
      console.log(data);
    });
  }

  openTableDiscountProductModal(discount: discount) {
    this.discountid = discount;
    this.productdiscountServices
      .getdiscountId(discount.id)
      .subscribe((data) => {
        {
          this.discountproductData1 = data;
          this.loadData();

        }
      });
    this.modalRef = this.modalService.open(this.content2, { size: "lg" });
  }

  onPageChange(event): void {
    this.currentPage = event;
    this.osTypeDataPage = this.discountData.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
    console.log(this.osTypeDataPage);
  }
  get form() {
    return this.formData.controls;
  }

  openModal() {
    this.discount.id = null;
    this.resetForm();
    this.modalRef = this.modalService.open(this.content);
  }

  openModal1() {
    this.discount.id = null;

    this.modalRef = this.modalService.open(this.content3);
  }

  discountid: discount = {
    id: 0,
    name: "",
    startDate: undefined,
    endDate: undefined,
    description: "",
    createTime: undefined,
    updateTime: undefined,
    createBy: "",
    updateBy: "",
    status: 0,
  };

  openCollarModal(discount: discount) {
    this.discountSelect = discount;
    this.formData1.get("discountId").setValue(discount.name);
    this.modalRef = this.modalService.open(this.content1);
  }

  closeModal(): void {
    this.modalRef.close();
  }

  saveOsType() {
    this.submitted = true;
    if (this.formData.valid) {
      if(!this.ValidateDate()){
        this.ToastrService.error("ngày bắt đầu phải lớn hơn ngày kết thúc");
        return;
      }
      const name = this.formData.get("name").value;
      this.discount.name = name;
      const description = this.formData.get("description").value;
      this.discount.description = description;
      const startDate = this.formData.get("startDate").value;
      this.discount.startDate = new Date(startDate);
      console.log(startDate);
      const endDate = this.formData.get("endDate").value;
      this.discount.endDate = new Date(endDate);
      if (this.discount.id == null) {
        this.discountService.createDiscount(this.discount).subscribe({
          next: (res) => {
            this.closeModal();
            Swal.fire("Success!", " added successfully.", "success");
            this.loadData();
            this.resetForm();
          },
          error: (err) => {
            Swal.fire("Error!", "Failed to add .", "error");
          },
        });
      } else {
        this.discountService.updateDiscount(this.discount).subscribe({
          next: (res) => {
            this.closeModal();
            Swal.fire("Success!", " updated successfully.", "success");
            this.loadData();
          },
          error: (err) => {
            Swal.fire("Error!", "Failed to update .", "error");
          },
        });
      }
    }
  }

  saveDiscountProduct() {
    this.submitted = true;
    if (this.formData1.valid && this.discountSelect) {
      const percent = this.formData1.get("percent").value;
      this.producdiscount.percent = percent;
      this.producdiscount.discountId = this.discountSelect.id;
      const productId = this.formData1.get("productId").value;
      this.producdiscount.productId = productId;
      console.log(this.producdiscount);

      if (this.discount.id == null) {
        this.productdiscountServices
          .createDiscountProduct(this.producdiscount)
          .subscribe({
            next: (res) => {
              this.closeModal();
              this.resetForm();
              Swal.fire("Success!", " added successfully.", "success");
              this.loadData();
            },
            error: (err) => {
              Swal.fire("Error!", "Failed to add .", "error");
            },
          });
      } else {
        this.discountService.updateDiscount(this.discount).subscribe({
          next: (res) => {
            this.closeModal();
            Swal.fire("Success!", " updated successfully.", "success");
            this.loadData();
            this.resetForm();
          },
          error: (err) => {
            Swal.fire("Error!", "Failed to update .", "error");
          },
        });
      }
    }
  }

  editOsType(discount: discount) {
    const dateStart = new Date(discount.startDate);
    const dateEnd = new Date(discount.endDate);
    const format = "yyyy-MM-dd";
    const locate = "en-US";
    const formatdateStart = formatDate(dateStart, format, locate);
    const formatdateEnd = formatDate(dateEnd, format, locate);
    this.formData.setValue({
      name: discount.name,
      description: discount.description,
      startDate: formatdateStart,
      endDate: formatdateEnd,
    });
    this.openModal1();
    this.discount.id = discount.id;
  }

  deleteOsType(id: any) {
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning", // thay thế type bằng icon
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!",
    }).then((result) => {
      if (result.value) {
        this.discountService.deleteDiscount(id).subscribe({
          next: (res) => {
            Swal.fire("Deleted!", "OS Type has been deleted.", "success");
            this.loadData();

          },
          error: (err) => {
            Swal.fire("Error!", "Failed to delete OS Type.", "error");
          },
        });
      }
    });
  }

  deleteProductDiscount(id: any) {
    Swal.fire({
      title: "Are you sure?",
      text: "You won't be able to revert this!",
      icon: "warning", // thay thế type bằng icon
      showCancelButton: true,
      confirmButtonColor: "#3085d6",
      cancelButtonColor: "#d33",
      confirmButtonText: "Yes, delete it!",
    }).then((result) => {
      if (result.value) {
        this.productdiscountServices.deleteDiscountProduct(id).subscribe({
          next: (res) => {
            Swal.fire(
              "Deleted!",
              "Discount Product has been deleted.",
              "success"
            );
            this.loadData();
            this.productdiscountServices
                .getdiscountId(this.discountid.id)
                 .subscribe((data) => {
               {
             this.discountproductData1 = data;
               this.loadData();

        }
      });
          },
          error: (err) => {
            Swal.fire("Error!", "Failed to delete Discount.", "error");
          },
        });
      }
    });
  }
  ExportTOExcel() {
    {
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.discountData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "discount");
      XLSX.writeFile(wb, this.fileName);
    }
  }
  resetForm() {
    this.formData = this.formBuilder.group({
      name: ["", [Validators.required]],
      description: ["", [Validators.required]],
      startDate: ["", [Validators.required]],
      endDate: ["", [Validators.required]],
    });

    this.formData1 = this.formBuilder.group({
      percent: ["", [Validators.required]],
      productId: ["", [Validators.required]],
      discountId: ["", [Validators.required]],
    });
    this.loadDataProduct();

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
