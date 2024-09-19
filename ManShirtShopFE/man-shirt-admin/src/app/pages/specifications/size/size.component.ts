import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { size } from "src/app/core/models/size.models";
import { SizeService } from "src/app/core/services/size.service";
import Swal from "sweetalert2";
import * as XLSX from "xlsx";
@Component({
  selector: "app-size-type",
  templateUrl: "./size.component.html",
  styleUrls: ["./size.component.scss"],
})
export class sizeComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  size: size = {
    id: null,
    code: null,
    description: null,
    createTime: new Date(),
    updateTime: null,
    createBy: null,
    updateBy: null,
    status: 0,
  };
  sizeData: size[];
  osTypeDataPage: size[];
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = "size.xlsx";
  @ViewChild("content") content: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private sizeService: SizeService
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [
      { label: "Quản lý" },
      { label: "Thuộc tính sản phẩm" },
      { label: "Kích cỡ", active: true },
    ];
    this.formData = this.formBuilder.group({
      code: ["", [Validators.required]],
      description: ["", [Validators.required]],
    });
    this.loadData();
  }
  loadData(): void {
    this.sizeService.getSize().subscribe((data) => {
      this.sizeData = data;
      this.totalItems = data.length;
      this.osTypeDataPage = data.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage
      );
    });
  }
  onPageChange(event): void {
    this.currentPage = event;
    this.osTypeDataPage = this.sizeData.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }
  get form() {
    return this.formData.controls;
  }
  openModal() {
    this.size.id = null;
    this.modalRef = this.modalService.open(this.content);
  }
  closeModal(): void {
    this.modalRef.close();
  }
  saveOsType() {
    this.submitted = true;
    if (this.formData.valid) {
      const name = this.formData.get("code").value;
      this.size.code = name;
      const description = this.formData.get("description").value;
      this.size.description = description;
      if (this.size.id == null) {
        this.sizeService.createSize(this.size).subscribe({
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
        this.sizeService.updateSize(this.size).subscribe({
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
  editOsType(size: size) {
    this.formData.setValue({
      code: size.code,
      description: size.description,
    });
    this.openModal();
    this.size.id = size.id;
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
        this.sizeService.deleteSize(id).subscribe({
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
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.sizeData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "color");
      XLSX.writeFile(wb, this.fileName);
    }
  }
}
