import { Component, OnInit, Pipe, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { number } from "echarts";
import { element } from "protractor";
import { filter } from "rxjs/operators";
import { voucherRequest,voucherResponse } from "src/app/core/models/voucher.models";
import { VoucherService } from "src/app/core/services/voucher.service";
import Swal from "sweetalert2";
import * as XLSX from 'xlsx';
import { ToastrService } from "ngx-toastr";
@Component({
  selector: "app-voucher",
  templateUrl: "./voucher.component.html",
  styleUrls: ["./voucher.component.scss"],
})

export class voucherComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  getType : any = [
    {
      id:1,
      name: 'Tiền',
    },
    {
      id:0,
      name:'Phần trăm'
    }
  ]
  voucherrequest: voucherRequest = {
    id: null,
    name: null,
    startDate: new Date(),
    endDate: new Date(),
    description: null,
    discount: null,
    code : null,
    type: null,
    min: null,
    max: null,
    createTime: new Date(),
    updateTime: null,
    createBy: null,
    updateBy: null,
    status: 0,
  };
  materialData: voucherResponse[];

  osTypeDataPage: voucherResponse[];
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = 'discount.xlsx';
  @ViewChild("content") content: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private voucherService: VoucherService,
    private ToastrService: ToastrService
  ) { }

  ngOnInit(): void {
    this.breadCrumbItems = [


    ];
    this.formData = this.formBuilder.group({
      name: ["", [Validators.required]],
       description: ["", [Validators.required]],
       discount: ["", [Validators.required]],
       startDate: ["", [Validators.required]],
       endDate: ["", [Validators.required]],
       type: ["", [Validators.required]],
        min: ["", [Validators.required]],
       max: ["", [Validators.required]],
    });
    this.loadData();
  }

  loadData(): void {
    this.voucherService.getVoucher().subscribe((data) => {
      this.materialData = data;
      console.log(data);
      this.totalItems = data.length;
      this.osTypeDataPage = data.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage
      );
    });
  }

  onPageChange(event): void {
    this.currentPage = event;
    this.osTypeDataPage = this.materialData.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }

  get form() {
    return this.formData.controls;
  }

  openModal() {
    this.voucherrequest.id = null;
    this.modalRef = this.modalService.open(this.content,{size:"lg"});
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
      this.voucherrequest.name = name;
      const description = this.formData.get("description").value;
      this.voucherrequest.description = description;
      const startDate = this.formData.get("startDate").value;
      this.voucherrequest.startDate = new Date(startDate);
      console.log(startDate);
      const endDate = this.formData.get("endDate").value;
      this.voucherrequest.endDate = new Date(endDate);
      const discount = this.formData.get("discount").value;
      this.voucherrequest.discount = discount;
      const type = this.formData.get("type").value;
      this.voucherrequest.code = type;
      const min = this.formData.get("min").value;
      this.voucherrequest.min = min;
      const max = this.formData.get("max").value;
      this.voucherrequest.max = max;

      if (this.voucherrequest.id == null) {
        this.voucherService.createVoucher(this.voucherrequest).subscribe({
          next: (res) => {
            this.closeModal();
            Swal.fire("Success!", " added successfully.", "success");
            this.loadData();
          },
          error: (err) => {
            Swal.fire("Error!", "Failed to add .", "error");
          },
        });
      } else {
        this.voucherService.updateVoucher(this.voucherrequest).subscribe({
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

  editOsType(mateRial: voucherResponse) {
    this.formData.setValue({
      name: mateRial.name,
      description: mateRial.description,
      startDate: mateRial.startDate,
      endDate: mateRial.endDate,
      discount: mateRial.discount,
      type: mateRial.type,
      max: mateRial.max,
      min: mateRial.min,
    });
    this.openModal();
    this.voucherrequest.id = mateRial.id;
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
        this.voucherService.deleteVoucher(id).subscribe({
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

  ExportTOExcel() {
    {
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.materialData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'discount');
      XLSX.writeFile(wb, this.fileName);
    }
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
