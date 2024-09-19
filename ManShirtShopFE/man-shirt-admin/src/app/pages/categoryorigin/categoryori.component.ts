import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { categoryori } from "src/app/core/models/categoryorigin.models";
import { CategoryoriService } from "src/app/core/services/categoryori.service";
import Swal from "sweetalert2";
import * as XLSX from "xlsx";
@Component({
  selector: "app-collar-type",
  templateUrl: "./categoryori.component.html",
  styleUrls: ["./categoryori.component.scss"],
})
export class categoryoriComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  categoryori: categoryori = {
    id: null,
    name: null,
    createTime: new Date(),
    updateTime: null,
    createBy: null,
    updateBy: null,
    status: 0,
  };
  collarData: categoryori[];
  osTypeDataPage: categoryori[];
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = "collar.xlsx";
  @ViewChild("content") content: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private collarService: CategoryoriService
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [
      { label: "Quản lý" },
      { label: "Thuộc tính sản phẩm" },
      { label: "Cổ áo", active: true },
    ];
    this.formData = this.formBuilder.group({
      name: ["", [Validators.required]],
    });
    this.loadData();
  }
  loadData(): void {
    this.collarService.getCollar().subscribe((data) => {
      this.collarData = data;
      this.totalItems = data.length;
      this.osTypeDataPage = data.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage
      );
    });
  }
  onPageChange(event): void {
    this.currentPage = event;
    this.osTypeDataPage = this.collarData.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }
  get form() {
    return this.formData.controls;
  }
  openModal() {
    this.categoryori.id = null;
    this.modalRef = this.modalService.open(this.content);
  }

  closeModal(): void {
    this.modalRef.close();
  }

  saveOsType() {
    this.submitted = true;
    if (this.formData.valid) {
      const name = this.formData.get("name").value;
      this.categoryori.name = name;
      if (this.categoryori.id == null) {
        this.collarService.createCollar(this.categoryori).subscribe({
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
        this.collarService.updateCollar(this.categoryori).subscribe({
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
  editOsType(colLar: categoryori) {
    this.formData.setValue({
      name: colLar.name,
    });
    this.openModal();
    this.categoryori.id = colLar.id;
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
        this.collarService.deleteCollar(id).subscribe({
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
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.collarData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "color");
      XLSX.writeFile(wb, this.fileName);
    }
  }
}
