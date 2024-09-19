import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { form } from "src/app/core/models/form.models";
import { FormService } from "src/app/core/services/form.service";
import Swal from "sweetalert2";
import * as XLSX from 'xlsx';
@Component({
  selector: "app-formm-type",
  templateUrl: "./form.component.html",
  styleUrls: ["./form.component.scss"],
})
export class formComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  formm: form = {
    id: null,
    name: null,
    createTime: new Date(),
    updateTime: null,
    createBy: null,
    updateBy: null,
    status: 0,
  };
  formmData: form[];
  osTypeDataPage: form[];
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = 'form.xlsx';
  @ViewChild("content") content: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private formmService: FormService
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [
      { label: "Quản lý" },
      { label: "Thuộc tính sản phẩm"},
      { label: "Hình dáng", active: true },
    ];
    this.formData = this.formBuilder.group({
      name: ["", [Validators.required]],
    });
    this.loadData();
  }
  loadData(): void {
    this.formmService.getForm().subscribe((data) => {
      this.formmData = data;
      this.totalItems = data.length;
      this.osTypeDataPage = data.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage
      );
    });
  }
  onPageChange(event): void {
    this.currentPage = event;
    this.osTypeDataPage = this.formmData.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }
  get form() {
    return this.formData.controls;
  }
  openModal() {
    this.formm.id = null;
    this.modalRef = this.modalService.open(this.content);
  }
  closeModal(): void {
    this.modalRef.close();
  }
  saveOsType() {
    this.submitted = true;
    if (this.formData.valid) {
      const name = this.formData.get("name").value;
      this.formm.name = name;
      if (this.formm.id == null) {
        this.formmService.createForm(this.formm).subscribe({
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
        this.formmService.updateForm(this.formm).subscribe({
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
  editOsType(formm: form) {
    this.formData.setValue({
      name: formm.name,
    });
    this.openModal();
    this.formm.id = formm.id;
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
        this.formmService.deleteForm(id).subscribe({
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
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.formmData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'color');
      XLSX.writeFile(wb, this.fileName);
    }
  }
}
