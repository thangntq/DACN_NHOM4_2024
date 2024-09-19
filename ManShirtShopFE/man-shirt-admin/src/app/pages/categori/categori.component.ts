import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { categoryori } from "src/app/core/models/categoryorigin.models";
import { CategoryoriService } from "src/app/core/services/categoryori.service";
import {
  categorirequest,
  categoriresponse,
} from "src/app/core/models/categori.models";
import { CategoriService } from "src/app/core/services/categori.service";
import Swal from "sweetalert2";
import * as XLSX from "xlsx";
@Component({
  selector: "app-collar-type",
  templateUrl: "./categori.component.html",
  styleUrls: ["./categori.component.scss"],
})
export class categoriComponent implements OnInit {
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
  categori: categorirequest = {
    id: null,
    name: null,
    createTime: new Date(),
    updateTime: null,
    createBy: null,
    updateBy: null,
    status: 0,
    categoryId: 0,
  };

  categoryoridata: categoryori[];
  collarData: categoriresponse[];
  osTypeDataPage: categoriresponse[];
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
    private collarService: CategoryoriService,
    private cateroriService: CategoriService
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [
      { label: "Quản lý" },
      { label: "Thuộc tính sản phẩm" },
      { label: "Cổ áo", active: true },
    ];
    this.formData = this.formBuilder.group({
      name: ["", [Validators.required]],
      categoryori: ["", [Validators.required]],
    });
    this.loadData();
  }
  loadData(): void {
    this.cateroriService.getCollar().subscribe((data) => {
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
    this.categori.id = null;
    this.modalRef = this.modalService.open(this.content);
    this.cbcCategoryori();
  }
  cbcCategoryori() {
    this.collarService.getCollar().subscribe((data) => {
      this.categoryoridata = data;
    });
  }
  closeModal(): void {
    this.modalRef.close();
  }
  saveOsType() {
    this.submitted = true;
    if (this.formData.valid) {
      const name = this.formData.get("name").value;
      this.categori.name = name;
      const categoryori = this.formData.get("categoryori").value;
      this.categori.categoryId = categoryori;
      console.log(this.categori.categoryId);
      if (this.categori.id == null) {
        this.cateroriService.createCollar(this.categori).subscribe({
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
        this.cateroriService.updateCollar(this.categori).subscribe({
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
  editOsType(categori: categoriresponse) {
    this.formData.setValue({
      name: categori.name,
      categoryori: categori.categoryId,
    });
    this.openModal();
    this.categori.id = categori.id;
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
        this.cateroriService.deleteCollar(id).subscribe({
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
