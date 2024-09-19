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
} from "@angular/core";
import { isPlatformBrowser } from "@angular/common";
import { NgbModal, ModalDismissReasons } from "@ng-bootstrap/ng-bootstrap";
import { Router } from "@angular/router";
import { Product, ProductDetail, color, size } from "../../../classes/product";
import { ProductService } from "../../../../shared/services/product.service";
import { ToastrService } from "ngx-toastr";

@Component({
  selector: "app-quick-view",
  templateUrl: "./quick-view.component.html",
  styleUrls: ["./quick-view.component.scss"],
})
export class QuickViewComponent implements OnInit, OnDestroy {
  @Input() product: Product;
  @Input() currency: any;
  @ViewChild("quickView", { static: false }) QuickView: TemplateRef<any>;
  public selectedColor: color;
  public selectedSize: size;
  public closeResult: string;
  public ImageSrc: string;
  public ImageSrc1: string;
  public counter: number = 1;
  public modalOpen: boolean = false;

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private router: Router,
    private modalService: NgbModal,
    public productService: ProductService,
    public toastrService: ToastrService
  ) {}

  ngOnInit(): void {
    this.selectedColor = this.product?.productDetail[0]?.color;
    // this.selectedSize = this.product?.productDetail[0]?.size;
  }

  openModal(ImageSrc, ImageSrc1, selectedColor) {
    this.modalOpen = true;
    this.ImageSrc = ImageSrc;
    this.ImageSrc1 = ImageSrc1;
    if (selectedColor) {
      this.selectedColor = selectedColor;
    }
    if (isPlatformBrowser(this.platformId)) {
      // For SSR
      this.modalService
        .open(this.QuickView, {
          size: "lg",
          ariaLabelledBy: "modal-basic-title",
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
  changeColor(color: any) {
    this.selectedColor = color;
    this.selectedSize = null;
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
  filterImagesByColor(color, product) {
    return product?.productImage?.filter(
      (image) => image.color.id === color.id
    );
  }
  filterImagesMainByColor(color, product) {
    return product?.productImage?.filter(
      (image) => image.color.id === color.id && image.mainImage
    );
  }
  // Get Product Color

  // Change Variants
  ChangeVariants(color, product) {
    for (const item of product.productImage) {
      if (item?.color?.id === color.id && item?.mainImage === true) {
        this.ImageSrc = item.urlImage;
        break;
      }
    }
    for (const item of product.productImage) {
      if (item?.color?.id === color.id && item?.mainImage === false) {
        this.ImageSrc1 = item.urlImage;
        break;
      }
    }
    this.selectedColor = color;
  }
  getAllColors(product: Product): color[] {
    const colors = product.productDetail.map((detail) => detail?.color);
    const uniqueColors = colors?.filter(
      (color, index, self) => index === self.findIndex((c) => c.id === color.id)
    );
    return uniqueColors;
  }
  getAllSizes(product: Product): size[] {
    const sizes = product.productDetail.map((detail) => detail?.size);
    const uniqueSizes = sizes?.filter(
      (size, index, self) => index === self.findIndex((s) => s.id === size.id)
    );
    return uniqueSizes;
  }

  // Increament
  increment() {
    this.counter++;
  }

  // Decrement
  decrement() {
    if (this.counter > 1) this.counter--;
  }

  // Add to cart
  async addToCart(productDetail: any) {
    if (!this.selectedSize) {
      this.toastrService.warning("Vui lòng chọn size!");
      return;
    }
    productDetail.quantity = this.counter || 1;
    const status = await this.productService.addToCart(productDetail);
    // if (status) this.router.navigate(["/shop/cart"]);
  }

  ngOnDestroy() {
    if (this.modalOpen) {
      this.modalService.dismissAll();
    }
  }
  ChangeVariantsImage(src) {
    this.ImageSrc = src;
  }
  getProductDetail(
    details: ProductDetail[],
    selectedColor: color,
    selectedSize: size
  ): ProductDetail {
    const detail = details.find(
      (d) => d.color.id === selectedColor?.id && d.size.id === selectedSize?.id
    );
    return detail ? detail : null;
  }
  selectSize(size) {
    this.selectedSize = size;
  }
  checkSize(color: any, size: any) {
    const detail = this.product.productDetail.find(
      (d) => d.color.id === color?.id && d.size.id === size?.id
    );
    if (detail?.quantity > 0 && detail?.status == 0) {
      return false;
    }
    return true;
  }
}
