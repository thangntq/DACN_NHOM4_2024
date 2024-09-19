import { Component, OnInit, ViewChild } from "@angular/core";
import { ActivatedRoute, Router } from "@angular/router";
import {
  ProductDetailsMainSlider,
  ProductDetailsThumbSlider,
} from "../../../../shared/data/slider";
import {
  Product,
  ProductDetail,
  color,
  size,
} from "../../../../shared/classes/product";
import { ProductService } from "../../../../shared/services/product.service";
import { SizeModalComponent } from "../../../../shared/components/modal/size-modal/size-modal.component";
import { ProductSlider } from "src/app/shared/components/product/product-box-one/product-box-one.component";
import { RatingComponent } from "src/app/shared/components/modal/rating/rating.component";
import { ToastrService } from "ngx-toastr";
import { OrderService } from "src/app/shared/services/order.service";
import { Lightbox } from "ngx-lightbox";

@Component({
  selector: "app-product-no-sidebar",
  templateUrl: "./product-no-sidebar.component.html",
  styleUrls: ["./product-no-sidebar.component.scss"],
})
export class ProductNoSidebarComponent implements OnInit {
  [x: string]: any;
  public product: any;
  public counter: number = 1;
  public activeSlide: any = 0;
  public selectedSize: any;
  public selectedColor: color;
  public ProductSliderConfig: any = ProductSlider;
  lstRating: any;
  @ViewChild("sizeChart") SizeChart: SizeModalComponent;
  @ViewChild("ratingModal") ratingModal: RatingComponent;

  public ProductDetailsMainSliderConfig: any = ProductDetailsMainSlider;
  public ProductDetailsThumbConfig: any = ProductDetailsThumbSlider;

  constructor(
    private route: ActivatedRoute,
    private lightbox: Lightbox,
    private router: Router,
    public productService: ProductService,
    public toastrService: ToastrService,
    private orderService: OrderService
  ) {
    this.route.data.subscribe({
      next: (value) => {
        this.productService.addToHistory(value.data);
        // this.product = value.data;
        this.productService.getProductById(value.data.id).subscribe({
          next: (res) => {
            this.product = res;
            console.log(res);
            this.selectedColor = this.product?.productImage[0]?.color;
            // this.selectedSize = this.product?.productDetail[0]?.size;
          },
          error: (err) => {
            this.toastrService.error("Lỗi khi lấy thông tin sản phẩm");
          },
        });
        orderService.getRatingByProduct(value.data.id).subscribe({
          next: (value) => {
            console.log(value);
            this.lstRating = value;
          },
        });
      },
    });

    // this.productService.getProducts.subscribe(response =>{
    //   this.product = response[0];
    // })
  }

  ngOnInit(): void {}
  checkSize(color: any, size: any) {
    const detail = this.product.productDetail.find(
      (d) => d.color.id === color?.id && d.size.id === size?.id
    );
    if (detail?.quantity > 0 && detail?.status == 0) {
      return false;
    }
    return true;
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
  changeColor(color: any) {
    this.selectedColor = color;
    this.selectedSize = null;
  }
  getAllColors(product: Product): color[] {
    const colors = product?.productDetail?.map((detail) => detail.color);
    const uniqueColors = colors?.filter(
      (color, index, self) =>
        index === self.findIndex((c) => c.id === color?.id)
    );
    return uniqueColors;
  }
  getAllSizes(product: Product): size[] {
    const sizes = product?.productDetail?.map((detail) => detail.size);
    const uniqueSizes = sizes?.filter(
      (size, index, self) => index === self.findIndex((s) => s.id === size?.id)
    );
    return uniqueSizes;
  }
  selectSize(size) {
    this.selectedSize = size;
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
  async addToCart(product: any) {
    if (!this.selectedSize) {
      this.toastrService.warning("Vui lòng chọn size!");
      return;
    }
    product.quantity = this.counter || 1;
    const status = await this.productService.addToCart(product);
    // if (status)
    //   this.router.navigate(['/shop/cart']);
  }

  // Buy Now
  async buyNow(product: any) {
    product.quantity = this.counter || 1;
    const status = await this.productService.addToCart(product);
    if (status) this.router.navigate(["/shop/checkout"]);
  }

  // Add to Wishlist
  addToWishlist(product: any) {
    this.productService.addToWishlist(product);
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
  getStars(rating: number): number[] {
    return Array(rating)
      .fill(0)
      .map((_, index) => index + 1);
  }
  convertTimeAgo(createTime: string): string {
    const currentTime = new Date();
    const ratingTime = new Date(createTime);
    const timeDiff = currentTime.getTime() - ratingTime.getTime();

    if (timeDiff < 60000) {
      return Math.floor(timeDiff / 1000) + " giây trước";
    } else if (timeDiff < 3600000) {
      const minutesAgo = Math.floor(timeDiff / 60000);
      return minutesAgo + " phút trước";
    } else if (timeDiff < 86400000) {
      const hoursAgo = Math.floor(timeDiff / 3600000);
      return hoursAgo + " giờ trước";
    } else {
      const daysAgo = Math.floor(timeDiff / 86400000);
      return daysAgo + " ngày trước";
    }
  }
  getInitials(name: string): string {
    const words = name.split(" ");
    let initials = words[0].charAt(0).toUpperCase();
    if (words.length > 2) {
      initials += words[words.length - 1].charAt(0).toUpperCase();
    }

    return initials;
  }
  openImage(images: any[], index: number): void {
    const albums = [];
    for (let i = 0; i < images.length; i++) {
      const src = images[i].image;
      const caption = "Image " + i;
      const thumb = images[i].image;
      const album = {
        src,
        caption,
        thumb,
      };
      albums.push(album);
    }
    this.lightbox.open(albums, index);
  }
}
