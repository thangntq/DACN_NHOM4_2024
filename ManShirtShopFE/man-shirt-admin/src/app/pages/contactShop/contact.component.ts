import { Component, OnInit, Pipe, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { filter } from "rxjs/operators";
import { contactRequest,contactResponse } from "src/app/core/models/contact.models";
import { ContactService } from "src/app/core/services/contact.service";
import Swal from "sweetalert2";
import * as XLSX from "xlsx";
@Component({
  selector: "app-discount",
  templateUrl: "./contact.component.html",
  styleUrls: ["./contact.component.scss"],
})
export class ContactComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  contact: contactRequest = {
    id: null,
    name: null,
    addressName : null,
    district : null,
    phone: null,
    province: null,
    ward : null,
    createTime: new Date(),
    updateTime: null,
    createBy: null,
    updateBy: null,
    status: 0,
  };
  contactData: contactResponse[];

  osTypeDataPage: contactResponse[];
  totalItems = 0;
  itemsPerPage = 3;
  currentPage = 1;
  formData: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = "discount.xlsx";
  @ViewChild("content") content: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private contactService: ContactService
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [];
    this.formData = this.formBuilder.group({
      name: ["", [Validators.required]],
      addressName: ["", [Validators.required]],
      district: ["", [Validators.required]],
      phone: ["", [Validators.required]],
      province: ["", [Validators.required]],
      ward: ["", [Validators.required]],
    });
    this.loadData();
  }
  loadData(): void {
    this.contactService.getContact().subscribe((data) => {
      this.contactData = data;
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
    this.osTypeDataPage = this.contactData.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }
  get form() {
    return this.formData.controls;
  }
  openModal() {
    this.contact.id = null;
    this.modalRef = this.modalService.open(this.content);
  }
  closeModal(): void {
    this.modalRef.close();
  }
  saveOsType() {
    this.submitted = true;
    if (this.formData.valid) {
      const name = this.formData.get("name").value;
      this.contact.name = name;
      const addressName = this.formData.get("addressName").value;
      this.contact.addressName = addressName;
      const district = this.formData.get("district").value;
      this.contact.district = district;
      console.log(district);
      const phone = this.formData.get("phone").value;
      this.contact.phone = phone;
      const province = this.formData.get("province").value;
      this.contact.province = province;
      const ward = this.formData.get("ward").value;
      this.contact.ward = ward;


      if (this.contact.id == null) {
        this.contactService.createContact(this.contact).subscribe({
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
        this.contactService.updateContact(this.contact).subscribe({
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
  editOsType(contact: contactResponse) {
    this.formData.setValue({
      name: contact.name,
      addressName: contact.addressName,
      district: contact.district,
      phone: contact.phone,
      province: contact.province,
      ward: contact.ward,
    });
    this.openModal();
    this.contact.id = contact.id;
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
        this.contactService.deleteContact(id).subscribe({
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
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.contactData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "contact");
      XLSX.writeFile(wb, this.fileName);
    }
  }
}
