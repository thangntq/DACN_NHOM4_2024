import { Component, OnInit, Pipe, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { filter } from "rxjs/operators";
import { UploadService } from "src/app/core/services/upload.service";
import { customerrequest,customerresponse } from "src/app/core/models/customer.models";
import { adress } from "src/app/core/models/address.models";
import { CustomerService } from "src/app/core/services/customer.service";
import { AddressService } from "src/app/core/services/address.service";
import Swal from "sweetalert2";
import * as XLSX from 'xlsx';
@Component({
  selector: "app-voucher",
  templateUrl: "./customer.component.html",
  styleUrls: ["./customer.component.scss"],
})

export class customerComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  image: File;
  getDataAddress : any = [
    {
      id : 1,
      name:'hoạt động'
    },
    {
      id : 0 ,
      name:'không hoạt động'
    }
  ]
  customer: customerrequest = {
    id: null,
    email: null,
    password: null,
    fullname: null,
    birthDate: null,
    phone: null,
    role: 3,
    photo: null,
    status: 0,
  };

  adress : adress={
  id: 0,
  address: null,
  cityName: null,
  districtName:null,
  wardName:null,
  other:null,
  status: null,
  customerResponse : this.customer,
  createTime: new Date(),
  updateTime: null,
  createBy: null,
  updateBy: null,
  }

  materialData: customerresponse[];
  addresData : adress[];
  typePageDP : adress[];
  osTypeDataPage: customerresponse[];
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  formData1: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = 'discount.xlsx';
  @ViewChild("content") content: any;
  @ViewChild("content2") content2: any;
  @ViewChild("content1") content1: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private customerService: CustomerService,
    private uploadService: UploadService,
    private addresService: AddressService,

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
    });

    this.formData1 = this.formBuilder.group({
      cityName: ["", [Validators.required]],
      districtName: ["", [Validators.required]],
      wardName: ["", [Validators.required]],
      other: ["", [Validators.required]],
      customerResponse: ["", [Validators.required]],
    });
    this.loadData();
  }

  loadData(): void {
    this.customerService.getCustomer().subscribe((data) => {
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
    this.customer.id = null;
    this.modalRef = this.modalService.open(this.content);
  }

  closeModal(): void {
    this.modalRef.close();
  }

  async saveOsType() {
    this.submitted = true;
    if (this.formData.valid) {
      const uploadPromises = [];
      const email = this.formData.get("email").value;
      this.customer.email = email;
      const role = this.formData.get("role").value;
      this.customer.role = role;
      const password = this.formData.get("password").value;
      this.customer.password = password;
      const fullname = this.formData.get("fullname").value;
      this.customer.fullname = fullname;
      console.log(fullname)
      const birthDate = this.formData.get("birthDate").value;
      this.customer.birthDate = new Date(birthDate);

      const phone = this.formData.get("phone").value;
      this.customer.phone = phone;

      const photo = await this.uploadService.uploadImage(this.image).toPromise();
      this.customer.photo = photo[0];
      uploadPromises.push(photo);
      console.log(photo);
      if (this.customer.id == null) {
        Promise.all(uploadPromises)
            .then(() => {
        this.customerService.createCustomer(this.customer).subscribe({
          next: (res) => {
            this.closeModal();
            Swal.fire("Success!", " added successfully.", "success");
            this.loadData();
          },
          error: (err) => {
            Swal.fire("Error!", "Failed to add .", "error");
          },
        });
      })
      } else {
        Promise.all(uploadPromises)
            .then(() => {
        this.customerService.updateCustomer(this.customer).subscribe({
          next: (res) => {
            this.closeModal();
            Swal.fire("Success!", " updated successfully.", "success");
            this.loadData();
          },
          error: (err) => {
            Swal.fire("Error!", "Failed to update .", "error");
          },
        });
      })
      }
    }
  }

  editOsType(customer: customerresponse) {
    this.formData.setValue({
      fullname: customer.fullname,
      adress: customer.adress,
      birthDate: customer.birthDate,
      email: customer.email,
      photo: customer.photo,
      phone: customer.phone,
      password: customer.password,

    });
    this.openModal();
    this.customer.id = customer.id;
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
        this.customerService.deleteCustomer(id).subscribe({
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

  onFileSelected(event: any) {
    const files: File[] = event.target.files;
    for (const file of files) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const image = {
          file: file,
          url: e.target.result,
          //id: this.idImageRemove ? this.idImageRemove.shift() : 0,
        };
      };
      reader.readAsDataURL(file);
      this.image = file;
      console.log(file)

    }
  }

  ExportTOExcel() {
    {
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.materialData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, 'discount');
      XLSX.writeFile(wb, this.fileName);
    }
  }

  // address
  checkaddress(customer1 : customerresponse){
    const status = 0;
      this.adress.status = status;
    this.addresService.getAddressByIdCustomer(customer1.id,status).subscribe((data) => {
      {
        this.addresData = data;
        console.log(data)

      }
  });
  this.modalRef = this.modalService.open(this.content2,{size:"lg"});
  }

  saveAddress() {
    this.submitted = true;
    if (this.formData.valid) {
      const cityName = this.formData.get("cityName").value;
      this.adress.cityName = cityName;
      const districtName = this.formData.get("districtName").value;
      this.adress.districtName = districtName;
      const wardName = this.formData.get("wardName").value;
      this.adress.wardName = wardName;
      const other = this.formData.get("other").value;
      this.adress.other = other;
      const adress = this.formData.get("adress").value;
      this.adress.address = adress;
      const customerResponse = this.formData.get("customerResponse").value;
      this.adress.customerResponse.fullname = customerResponse;



      if (this.customer.id == null) {

        this.addresService.createAddress(this.adress).subscribe({
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

        this.addresService.updateAddress(this.adress).subscribe({
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

  editAddress(address: adress) {
    this.formData.setValue({
      adress: address.address,
      cityName: address.cityName,
      districtName: address.districtName,
      other: address.other,
      wardName: address.wardName,
      customerResponse: address.customerResponse,
    });
    this.openModal();
    this.adress.id = address.id;
  }



  deleteAddress(id: any) {
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
        this.addresService.deleteAddress(id).subscribe({
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
}
