import {
  Component,
  OnInit,
  OnDestroy,
  ViewChild,
  TemplateRef,
  Input,
  Injectable,
  PLATFORM_ID,
  Inject,
  EventEmitter,
  Output,
} from "@angular/core";
import { isPlatformBrowser } from "@angular/common";
import { NgbModal, ModalDismissReasons } from "@ng-bootstrap/ng-bootstrap";
import { Product } from "../../../classes/product";
import { Order } from "src/app/shared/classes/order";
import { OrderService } from "src/app/shared/services/order.service";
import { ToastrService } from "ngx-toastr";
@Component({
  selector: "app-rating",
  templateUrl: "./rating.component.html",
  styleUrls: ["./rating.component.scss"],
})
export class RatingComponent implements OnInit, OnDestroy {
  @Input() product: Product;
  @Input() order: any;
  @Input() orderDetail: any;
  @ViewChild("ratingModal", { static: false }) ratingModal: TemplateRef<any>;
  @Input() maxRating: number = 5;
  @Output() ratingChange = new EventEmitter<number>();
  content: any;
  rating: number = 5;
  hoverRating: number = 0;
  labelRating: any = ["Tuyệt vời", "Hài lòng", "Bình thường", "Tệ", "Rất tệ"];
  public closeResult: string;
  public modalOpen: boolean = false;
  uploadedFiles: any[] = [];
  isRating: boolean = false;
  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private modalService: NgbModal,
    private orderService: OrderService,
    private toastrService: ToastrService
  ) {}

  ngOnInit(): void {}

  openModal() {
    this.modalOpen = true;
    if (isPlatformBrowser(this.platformId)) {
      // For SSR
      this.modalService
        .open(this.ratingModal, {
          size: "lg",
          ariaLabelledBy: "rating-modal",
          centered: true,
          windowClass: "return-modal",
          backdrop: false,
        })
        .result.then(
          (result) => {
            `Result ${result}`;
          },
          (reason) => {
            this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
          }
        );
    }
  }
  handleFileUpload(event: any) {
    const file = event.target.files[0];
    this.orderService.uploadImage(file).subscribe({
      next: (value) => {
        this.uploadedFiles.push(value?.[0]);
      },
      error: (err) => {
        this.toastrService.error("Tải ảnh lỗi! Vui lòng thử lại");
      },
    });
  }

  removeFile(index: number) {
    this.uploadedFiles.splice(index, 1);
  }

  private getDismissReason(reason: any): string {
    if (reason === ModalDismissReasons.ESC) {
      return "by pressing ESC";
    } else if (reason === ModalDismissReasons.BACKDROP_CLICK) {
      return "by clicking on a backdrop";
    } else {
      return `with: ${reason}`;
    }
  }

  ngOnDestroy() {
    if (this.modalOpen) {
      this.modalService.dismissAll();
    }
  }
  handleMouseEnter(index: number) {
    this.hoverRating = index;
  }

  handleMouseLeave() {
    this.hoverRating = 0;
  }

  handleClick(index: number) {
    this.rating = index;
    this.ratingChange.emit(this.rating);
  }

  sendRating() {
    this.isRating = true;
    const rating: any = {
      id: 0,
      rating: 0,
      content: "string",
      orderId: 0,
      productId: 0,
      iamges: [],
    };
    if (this.content && this.content !== "") {
      rating.content = this.content;
    } else {
      this.toastrService.info("Vui lòng nhập nội dung đánh giá");
      this.isRating = false;
      return;
    }
    rating.rating = this.rating;
    rating.orderId = this.order.id;
    rating.productId = this.product.id;
    this.uploadedFiles?.forEach((file) => {
      rating.iamges.push(file);
    });
    console.log(rating);
    this.orderService.rating(rating).subscribe({
      next: (res) => {
        this.isRating = false;
        this.ngOnDestroy();
        this.order.orderDetail.find((item) => {
          if (item.productDetailId.product.id === this.product.id) {
            item.checkRating = true;
          }
        });
        this.toastrService.success("Gửi đánh giá thành công!");
      },
      error: (err) => {
        this.isRating = false;
        // this.ngOnDestroy();
        this.toastrService.error("Vui lòng thử lại sau!");
      },
    });
  }
}
