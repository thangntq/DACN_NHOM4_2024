import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { sleeve } from "src/app/core/models/sleeve.models";
import { SleeveService } from "src/app/core/services/sleeve.service";
import Swal from "sweetalert2";
import * as XLSX from 'xlsx';
@Component({
  selector: "app-sleeve-type",
  templateUrl: "./sleeve.component.html",
  styleUrls: ["./sleeve.component.scss"],
})
export class sleeveComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  sleeVe : sleeve = {
    id: null,
    name: null,
    diameter:null,
    createTime: new Date(),
    updateTime: null,
    createBy: null,
    updateBy: null,
    status: 0,
  };
  sleeveData: sleeve[];
  osTypeDataPage: sleeve[];
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = 'sleeve.xlsx';
  @ViewChild("content") content: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private sleeVeService: SleeveService
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [
      { label: "Quản lý" },
      { label: "Thuộc tính sản phẩm"},
      { label: "Tay áo", active: true },
    ];
    this.formData = this.formBuilder.group({
      name: ["", [Validators.required]],
      diameter: ["", [Validators.required]],
    });
    this.loadData();
  }
  loadData(): void {
    this.sleeVeService.getSleeve().subscribe((data) => {
      this.sleeveData = data;
      this.totalItems = data.length;
      this.osTypeDataPage = data.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage
      );
    });
  }
  onPageChange(event): void {
    this.currentPage = event;
    this.osTypeDataPage = this.sleeveData.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }
  get form() {
    return this.formData.controls;
  }
  openModal() {
    this.sleeVe.id = null;
    this.modalRef = this.modalService.open(this.content);
  }
  closeModal(): void {
    this.modalRef.close();
  }
  saveOsType() {
    this.submitted = true;
    if (this.formData.valid) {
      const name = this.formData.get("name").value;
      this.sleeVe.name = name;
      const diameter = this.formData.get("diameter").value;
      this.sleeVe.diameter = diameter;
      if (this.sleeVe.id == null) {
        this.sleeVeService.createSleeve(this.sleeVe).subscribe({
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
        this.sleeVeService.updateSleeve(this.sleeVe).subscribe({
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
  editOsType(sleeVe: sleeve) {
    this.formData.setValue({
      name: sleeVe.name,
      diameter: sleeVe.diameter,
    });
    this.openModal();
    this.sleeVe.id = sleeVe.id;
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
        this.sleeVeService.deleteSleeve(id).subscribe({
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
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.sleeveData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'color');
      XLSX.writeFile(wb, this.fileName);
    }
  }
}
