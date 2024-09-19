import { FlatTreeControl } from "@angular/cdk/tree";
import { Component, OnInit, ViewChild } from "@angular/core";
import { FormBuilder, FormGroup, Validators } from "@angular/forms";
import {
  MatTreeFlatDataSource,
  MatTreeFlattener,
} from "@angular/material/tree";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import {
  categoryRequest,
  categoryResponse,
} from "src/app/core/models/category.models";
import { CategoryService } from "src/app/core/services/category.service";
import Swal from "sweetalert2";
import * as XLSX from "xlsx";
interface ExampleFlatNode {
  expandable: boolean;
  id: number;
  name: string;
  createTime: Date;
  updateTime: Date;
  createBy: string;
  updateBy: string;
  status: number;
  level: number;
}
@Component({
  selector: "app-category",
  templateUrl: "./category.component.html",
  styleUrls: ["./category.component.scss"],
})
export class CategoryComponent implements OnInit {
  displayedColumns: string[] = ["name", "create", "update", "action"];
  breadCrumbItems: Array<{}>;
  term: any;
  category: categoryRequest = {
    id: null,
    name: null,
    categoryId: 0,
  };
  categorylData: categoryResponse[];
  categoryDataPage: categoryResponse[];
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = "category.xlsx";
  @ViewChild("content") content: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private categoryService: CategoryService
  ) {}
  ngOnInit(): void {
    this.breadCrumbItems = [
      { label: "Quản lý" },
      { label: "Thể loại", active: true },
    ];
    this.formData = this.formBuilder.group({
      name: ["", [Validators.required]],
    });
    this.loadData();
  }
  loadData(): void {
    this.categoryService.getCategory().subscribe((data) => {
      this.dataSource.data = data;
      this.categorylData = data;
      this.totalItems = data.length;
      this.categoryDataPage = data.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage
      );
    });
  }
  onPageChange(event): void {
    this.currentPage = event;
    this.categoryDataPage = this.categorylData.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }
  get form() {
    return this.formData.controls;
  }
  openModal() {
    this.category.id = null;
    this.modalRef = this.modalService.open(this.content);
  }
  closeModal(): void {
    this.modalRef.close();
  }
  saveCategory() {
    this.submitted = true;
    if (this.formData.valid) {
      const name = this.formData.get("name").value;
      this.category.name = name;
      if (this.category.id == null) {
        this.categoryService.createCategory(this.category).subscribe({
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
        this.categoryService.updateCategory(this.category).subscribe({
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
  createCategoryChild(category: categoryResponse) {
    this.openModal();
    this.category.categoryId = category.id;
  }
  editCategory(category: categoryResponse) {
    this.formData.setValue({
      name: category.name,
    });
    this.openModal();
    this.category.id = category.id;
    this.category.categoryId = category.categoryId;
    console.log(category.categoryId);
  }
  deleteCategory(id: any) {
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
        this.categoryService.deleteCategory(id).subscribe({
          next: (res) => {
            Swal.fire("Deleted!", "Category has been deleted.", "success");
            this.loadData();
          },
          error: (err) => {
            Swal.fire("Error!", "Failed to delete Category.", "error");
          },
        });
      }
    });
  }
  ExportTOExcel() {
    {
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.dataSource.data);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "color");
      XLSX.writeFile(wb, this.fileName);
    }
  }
  private transformer = (node: categoryResponse, level: number) => {
    return {
      expandable: !!node.lstCategory && node.lstCategory.length > 0,
      id: node.id,
      name: node.name,
      createTime: node.createTime,
      updateTime: node.updateTime,
      createBy: node.createBy,
      updateBy: node.updateBy,
      categoryId: node.categoryId,
      status: node.status,
      level: level,
    };
  };

  treeControl = new FlatTreeControl<ExampleFlatNode>(
    (node) => node.level,
    (node) => node.expandable
  );

  treeFlattener = new MatTreeFlattener(
    this.transformer,
    (node) => node.level,
    (node) => node.expandable,
    (node) => node.lstCategory
  );

  dataSource = new MatTreeFlatDataSource(this.treeControl, this.treeFlattener);

  hasChild = (_: number, node: ExampleFlatNode) => node.expandable;
}
