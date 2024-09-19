import { Component, OnInit, Pipe, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { formatDate } from "@angular/common";
import { filter } from "rxjs/operators";
import { UploadService } from "src/app/core/services/upload.service";
import {
  employeeRequest,
  employeeRespone,
  roleEmployee,
} from "src/app/core/models/employee.models";
import { EmployeeService } from "src/app/core/services/employee.service";
import Swal from "sweetalert2";
import * as XLSX from "xlsx";
import { url } from "inspector";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-employee",
  templateUrl: "./employee.component.html",
  styleUrls: ["./employee.component.scss"],
})
export class employeeComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  loading: boolean = false;
  term1: any;
  image: File;
  imageEmployee: [];
  setRole: any = [
    {
      id: 1,
      name: "Quản lý",
      status: 1,
    },
    {
      id: 2,
      name: "Nhân viên",
      status: 1,
    },
  ];

  employEe: employeeRequest = {
    id: null,
    email: null,
    password: null,
    fullname: null,
    birthDate: new Date(),
    address: null,
    phone: null,
    note: null,
    role: null,
    photo: null,
    createTime: new Date(),
    updateTime: null,
    createBy: null,
    updateBy: null,
    status: 0,
  };

  materialData: employeeRespone[];
  osTypeDataPage: employeeRespone[];
  employeeGetData: employeeRespone;
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = "discount.xlsx";
  @ViewChild("content") content: any;
  @ViewChild("content2") content1: any;
  @ViewChild("content3") content2: any;

  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private mateRialService: EmployeeService,
    private uploadService: UploadService,
    private toastrService: ToastrService
  ) {}
  ngOnInit(): void {
    this.breadCrumbItems = [];
    this.formData = this.formBuilder.group({
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required]],
      fullname: ["", [Validators.required ]],
      birthDate: ["", [Validators.required]],
      address: ["", [Validators.required]],
      phone: [
        "",
        [
          Validators.required,
          Validators.pattern(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g),
        ],
      ],
      note: ["", [Validators.required]],
      photo: ["", [Validators.required]],
      role: ["", [Validators.required]],
    });
    this.loadData();
  }

  loadData(): void {
    this.mateRialService.getEmployee().subscribe((data) => {
      this.materialData = data;
      console.log(this.materialData);
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
    this.employEe.id = null;
    this.resetForm()
    this.modalRef = this.modalService.open(this.content, { size: "xl" });
  }

  openModal1() {
    this.employEe.id = null;

    this.modalRef = this.modalService.open(this.content2, { size: "xl" });
  }

  closeModal(): void {
    this.modalRef.close();

  }

  async saveOsType() {
    if (this.formData.get("phone").errors?.pattern) {
      this.toastrService.warning("Số điện thoại sai định dạng");
      return;
    }
    if(!this.validateDate()){
      this.toastrService.error("đủ 18 tuổi");
      return;
    }
    this.submitted = true;

    if (this.formData.valid) {
      const uploadPromises = [];
      const email = this.formData.get("email").value;
      this.employEe.email = email;
      const role = this.formData.get("role").value;
      this.employEe.role = role;
      const password = this.formData.get("password").value;
      this.employEe.password = password;
      const fullname = this.formData.get("fullname").value;
      this.employEe.fullname = fullname;
      const birthDate = this.formData.get("birthDate").value;
      this.employEe.birthDate = new Date(birthDate);
      const address = this.formData.get("address").value;
      this.employEe.address = address;
      const phone = this.formData.get("phone").value;
      this.employEe.phone = phone;
      const note = this.formData.get("note").value;
      this.employEe.note = note;
      if (this.image) {
        const photo = await this.uploadService
          .uploadImage(this.image)
          .toPromise();
        this.employEe.photo = photo[0];
        uploadPromises.push(photo);
      }
      if (!this.image && !this.employEe.photo) {
        Swal.fire("Error!", "Thiếu ảnh nhân viên", "error");
        return;
      }

      if (this.employEe.id == null) {
        Promise.all(uploadPromises).then(() => {
          this.loading = true;
          this.mateRialService.createEmployee(this.employEe).subscribe({
            next: (res) => {
              this.closeModal();
              Swal.fire("Success!", " added successfully.", "success");
              this.loadData();
              this.loading = false;
            },
            error: (err) => {
              Swal.fire("Error!", "Failed to add .", "error");
              this.loading = false;
            },
          });
        });
      } else {
        Promise.all(uploadPromises).then(() => {
          this.mateRialService.updateEmployee(this.employEe).subscribe({
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
        });
      }
    } else {
       Swal.fire("Error!", "Thiếu thông tin", "error");
    }
  }

  urlImage: any;
  onFileSelected(event: any) {
    const files: File[] = event.target.files;
    if (files) {
      for (const file of files) {
        const reader = new FileReader();
        reader.onload = (e: any) => {
          const image = {
            file: file,
            url: e.target.result,
          };
          this.urlImage = image.url;
        };
        reader.readAsDataURL(file);

        this.image = file;
        this.formData.controls["photo"].setValue(file);

        console.log(this.employEe.photo);
      }
    }
  }

  editOsType(employEe: employeeRespone) {
    const date = new Date(employEe.birthDate);
    const format = "yyyy-MM-dd";
    const locate = "en-US";
    const formatdate = formatDate(date, format, locate);
    this.formData.setValue({
      fullname: employEe.fullname,
      birthDate: formatdate,
      address: employEe.address,
      email: employEe.email,
      note: employEe.note,
      password: employEe.password,
      role: employEe.role?.id,
      phone: employEe.phone,
      photo: employEe.photo,
    });
    this.urlImage = employEe.photo.toString();
    this.openModal1();
    this.employEe.id = employEe.id;
    this.employEe.photo = employEe.photo.toString();
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
        this.mateRialService.deleteEmployee(id).subscribe({
          next: (res) => {
            Swal.fire("Deleted!", " has been deleted.", "success");
            this.loadData();
          },
          error: (err) => {
            Swal.fire("Error!", "Failed to delete.", "error");
          },
        });
      }
    });
  }

  employee: employeeRequest = {
    id: 0,
    email: "",
    password: "",
    fullname: "",
    birthDate: undefined,
    address: "",
    phone: "",
    note: "",
    role: 0,
    photo: "",
    createTime: undefined,
    updateTime: undefined,
    createBy: "",
    updateBy: "",
    status: 0,
  };

  getEmployeeById(employEe: employeeRespone) {
    this.mateRialService.getEmployeeId(employEe.id).subscribe({
      next: (res) => {
        this.employeeGetData = res;
        this.modalRef = this.modalService.open(this.content1, { size: "xl" });
      },
    });
  }


  ExportTOExcel() {
    {
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.materialData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "discount");
      XLSX.writeFile(wb, this.fileName);
    }
  }

  resetForm(){
    this.formData = this.formBuilder.group({
      email: ["", [Validators.required, Validators.email]],
      password: ["", [Validators.required]],
      fullname: ["", [Validators.required ,Validators.pattern('')]],
      birthDate: ["", [Validators.required]],
      address: ["", [Validators.required]],
      phone: [
        "",
        [
          Validators.required,
          Validators.pattern(/(84|0[3|5|7|8|9])+([0-9]{8})\b/g),
        ],
      ],
      note: ["", [Validators.required]],
      photo: ["", [Validators.required]],
      role: ["", [Validators.required]],
    });
    this.loadData();
  }

  validateDate(){
    const birthDate = this.formData.get("birthDate").value;
    const birth = new Date(birthDate).getFullYear();
    const date = new Date().getFullYear();
    if(date - birth <= 18 ){
        return false;
    }
    return true;
  }
}
