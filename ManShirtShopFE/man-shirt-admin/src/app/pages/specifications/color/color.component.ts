import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { color } from "src/app/core/models/color.models";
import { ColorService } from "src/app/core/services/color.service";
import Swal from "sweetalert2";
import * as XLSX from "xlsx";
@Component({
  selector: "app-color-type",
  templateUrl: "./color.component.html",
  styleUrls: ["./color.component.scss"],
})
export class colorComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  color: color = {
    id: null,
    name: null,
    createTime: new Date(),
    updateTime: null,
    createBy: null,
    updateBy: null,
    status: 0,
  };
  colorData: color[];
  osTypeDataPage: color[];
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = "color.xlsx";
  @ViewChild("content") content: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private colorService: ColorService
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [
      { label: "Quản lý" },
      { label: "Thuộc tính sản phẩm" },
      { label: "Màu sắc", active: true },
    ];
    this.formData = this.formBuilder.group({
      name: ["", [Validators.required]],
    });
    this.loadData();
  }
  loadData(): void {
    this.colorService.getColor().subscribe((data) => {
      this.colorData = data;
      this.totalItems = data.length;
      this.osTypeDataPage = data.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage
      );
    });
  }
  onPageChange(event): void {
    this.currentPage = event;
    this.osTypeDataPage = this.colorData.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }
  get form() {
    return this.formData.controls;
  }
  openModal() {
    this.color.id = null;
    this.modalRef = this.modalService.open(this.content);
  }
  closeModal(): void {
    this.modalRef.close();
  }
  saveOsType() {
    this.submitted = true;
    if (this.formData.valid) {
      const name = this.formData.get("name").value;
      this.color.name = name;
      if (this.color.id == null) {
        this.colorService.createColor(this.color).subscribe({
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
        this.colorService.updateColor(this.color).subscribe({
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
  editOsType(color: color) {
    this.formData.setValue({
      name: color.name,
    });
    this.openModal();
    this.color.id = color.id;
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
        this.colorService.deleteColor(id).subscribe({
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
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.colorData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "color");
      XLSX.writeFile(wb, this.fileName);
    }
  }
}
