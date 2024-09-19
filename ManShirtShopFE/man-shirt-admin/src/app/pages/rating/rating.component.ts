import { Component, OnInit, Pipe, ViewChild } from "@angular/core";
import { FormBuilder, Validators, FormGroup } from "@angular/forms";
import { NgbModal, NgbModalRef } from "@ng-bootstrap/ng-bootstrap";
import { filter } from "rxjs/operators";
import { rating } from "src/app/core/models/rating.models";
import {
  ImageUploadRating,
  ImageUpload,
} from "src/app/core/models/ratingimage.models";
import { customerrequest } from "src/app/core/models/customer.models";
import { Order } from "src/app/core/models/order.models";
import { ProductRequest } from "src/app/core/models/product.model";
import { RatingService } from "src/app/core/services/rating.service";

import { UploadRatingService } from "src/app/core/services/ratingimage.service";
import Swal from "sweetalert2";
import * as XLSX from "xlsx";
import { ToastrService } from "ngx-toastr";
@Component({
  selector: "app-rating",
  templateUrl: "./rating.component.html",
  styleUrls: ["./rating.component.scss"],
})
export class RatingComponent implements OnInit {
  breadCrumbItems: Array<{}>;
  term: any;
  image: File;
  loading: boolean = false;
  Rating: rating = {
    id: null,
    rating: 0,
    content: null,
    customerId: 0,
    orderId: 0,
    productId: 0,
    status: 0,
  };

  RatingImage: ImageUploadRating = {
    id: 0,
    image: null,
    ratingId: 0,
    status: 0,
  };

  ratingData: rating[];
  ratingDataPage: rating[];
  ratingImageData: ImageUploadRating[];
  ratingImageDataPage: ImageUploadRating[];
  totalItems = 0;
  itemsPerPage = 10;
  currentPage = 1;
  formData: FormGroup;
  submitted = false;
  modalRef: NgbModalRef;
  fileName = "discount.xlsx";
  @ViewChild("content") content: any;
  constructor(
    private modalService: NgbModal,
    private formBuilder: FormBuilder,
    private RatingService: RatingService,
    private toastService: ToastrService
  ) {}

  ngOnInit(): void {
    this.breadCrumbItems = [];
    this.formData = this.formBuilder.group({
      rating: ["", [Validators.required]],
      image: ["", [Validators.required]],
    });
    this.formData = this.formBuilder.group({
      rating: ["", [Validators.required]],
      content: ["", [Validators.required]],
      orderId: ["", [Validators.required]],
      productId: ["", [Validators.required]],
      customerId: ["", [Validators.required]],
    });
    this.loadData();
  }

  loadData(): void {
    this.loading = true;
    this.RatingService.getRating().subscribe((data) => {
      this.ratingData = data;
      this.totalItems = data.length;
      this.ratingDataPage = data.slice(
        (this.currentPage - 1) * this.itemsPerPage,
        this.currentPage * this.itemsPerPage
      );
      this.loading = false;
    });

    // this.UploadRatingService.getRatingImage().subscribe((data) => {
    //   this.ratingImageData = data;
    //   console.log(data);
    //   this.totalItems = data.length;
    //   this.ratingImageDataPage =  this.ratingImageData.slice(
    //     (this.currentPage - 1) * this.itemsPerPage,
    //     this.currentPage * this.itemsPerPage
    //   );
    //   console.log(this.ratingImageData);
    //   console.log(this.ratingImageDataPage);
    //   console.log(this.totalItems);
    // });
  }

  onPageChange(event): void {
    this.currentPage = event;
    this.ratingDataPage = this.ratingData.slice(
      (this.currentPage - 1) * this.itemsPerPage,
      this.currentPage * this.itemsPerPage
    );
  }

  get form() {
    return this.formData.controls;
  }

  openModal() {
    this.Rating.id = null;
    this.modalRef = this.modalService.open(this.content);
  }

  closeModal(): void {
    this.modalRef.close();
  }

  urlImage: any;
  onFileSelected(event: any) {
    const files: File[] = event.target.files;
    for (const file of files) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const image = {
          file: file,
          url: e.target.result,
        };
        this.urlImage = image.url;
        console.log(this.urlImage);
      };
      reader.readAsDataURL(file);
      this.image = file;

      console.log(this.image);
    }
  }
  editRating(rating: rating) {
    this.formData.setValue({
      name: rating.content,
      addressName: rating.customerId,
      district: rating.orderId,
      phone: rating.productId,
      province: rating.rating,
    });
    this.openModal();
    this.Rating.id = rating.id;
  }

  // deleteRating(id: any) {
  //   Swal.fire({
  //     title: "Are you sure?",
  //     text: "You won't be able to revert this!",
  //     icon: "warning", // thay thế type bằng icon
  //     showCancelButton: true,
  //     confirmButtonColor: "#3085d6",
  //     cancelButtonColor: "#d33",
  //     confirmButtonText: "Yes, delete it!",
  //   }).then((result) => {
  //     if (result.value) {
  //       this.RatingService.deleteRating(id).subscribe({
  //         next: (res) => {
  //           Swal.fire("Deleted!", "OS Type has been deleted.", "success");
  //           this.loadData();
  //         },
  //         error: (err) => {
  //           Swal.fire("Error!", "Failed to delete OS Type.", "error");
  //         },
  //       });
  //     }
  //   });
  // }

  ExportTOExcel() {
    {
      const ws: XLSX.WorkSheet = XLSX.utils.json_to_sheet(this.ratingData);
      const wb: XLSX.WorkBook = XLSX.utils.book_new();
      XLSX.utils.book_append_sheet(wb, ws, "rating");
      XLSX.writeFile(wb, this.fileName);
    }
  }
  toggleStatus(event: any, rating: any) {
    this.loading = true;
    const isChecked = event.target.checked;
    const previousStatus = !isChecked;
    if (isChecked) {
      this.RatingService.updateStausOn(rating.id).subscribe({
        next: (value) => {
          if (value.status) {
            this.toastService.success("Thành công");
            this.loading = false;
          } else {
            rating.status = previousStatus;
            this.toastService.error("Thất bại");
            this.loading = false;
          }
        },
        error: (error) => {
          this.loading = false;
          rating.status = previousStatus;
          this.toastService.error("Thất bại");
        },
      });
    } else {
      this.RatingService.updateStausOff(rating.id).subscribe({
        next: (value) => {
          if (value.status) {
            this.toastService.success("Thành công");
            this.loading = false;
          } else {
            rating.status = previousStatus;
            this.toastService.error("Thất bại");
            this.loading = false;
          }
        },
        error: (error) => {
          this.loading = false;
          rating.status = previousStatus;
          this.toastService.error("Thất bại");
        },
      });
    }
  }
}
