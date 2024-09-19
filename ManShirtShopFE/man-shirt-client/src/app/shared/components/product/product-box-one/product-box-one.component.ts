import { Component, OnInit, Input, ViewChild } from "@angular/core";
import { QuickViewComponent } from "../../modal/quick-view/quick-view.component";
import { CartModalComponent } from "../../modal/cart-modal/cart-modal.component";
import { Product, color } from "../../../classes/product";
import { ProductService } from "../../../services/product.service";

@Component({
  selector: "app-product-box-one",
  templateUrl: "./product-box-one.component.html",
  styleUrls: ["./product-box-one.component.scss"],
})
export class ProductBoxOneComponent implements OnInit {
  @Input() product: Product;
  @Input() currency: any = this.productService.Currency; // Default Currency
  @Input() thumbnail: boolean = false; // Default False
  @Input() onHowerChangeImage: boolean = false; // Default False
  @Input() cartModal: boolean = false; // Default False
  @Input() loader: boolean = false;
  @Input() flashSale: boolean = false;

  @ViewChild("quickView") QuickView: QuickViewComponent;
  @ViewChild("cartModal") CartModal: CartModalComponent;

  public ImageSrc: string;
  public ImageSrc1: string;
  public color: string;
  public selectedColor: color;

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    if (this.loader) {
      setTimeout(() => {
        this.loader = false;
      }, 2000); // Skeleton Loader
    }
  }

  // Get Product Color
  getAllColors(product: Product): color[] {
    const colors = product.productDetail.map((detail) => detail?.color);
    const uniqueColors = colors?.filter(
      (color, index, self) => index === self.findIndex((c) => c.id === color.id)
    );
    return uniqueColors;
  }

  // Change Variants
  ChangeVariants(color, product) {
    for (const item of product.productImage) {
      if (item.color.id === color.id && item.mainImage === true) {
        this.ImageSrc = item.urlImage;
        break;
      }
    }
    for (const item of product.productImage) {
      if (item.color.id === color.id && item.mainImage === false) {
        this.ImageSrc1 = item.urlImage;
        break;
      }
    }
    this.color = color;
  }
  filterImagesByColor(color, product) {
    return product.productImage.filter((image) => image.color.id === color.id);
  }
  filterImagesMainByColor(color, product) {
    return product.productImage.filter(
      (image) => image.color.id === color.id && image.mainImage
    );
  }
  // Change Variants Image
  ChangeVariantsImage(src) {
    this.ImageSrc = src;
  }

  getMainImage(product) {
    for (const item of product.productImage) {
      if (item.mainImage === true) {
        return item.urlImage;
      }
    }
  }
  getSubImage(product) {
    for (const item of product.productImage) {
      if (item.mainImage === false) {
        return item.urlImage;
      }
    }
  }
  addToCart(product: any) {
    console.log(product);
    this.productService.addToCart(product);
  }

  addToWishlist(product: any) {
    this.productService.addToWishlist(product);
  }

  addToCompare(product: any) {
    this.productService.addToCompare(product);
  }

  public ProductSliderConfig: any = ProductSlider;
}
export let ProductSlider: any = {
  loop: false,
  dots: false,
  navSpeed: 300,
  preventScrollOnTouch: true,
  // responsive: {
  //   991: {
  //     items: 7,
  //   },
  //   767: {
  //     items: 6,
  //   },
  //   420: {
  //     items: 5,
  //   },
  //   0: {
  //     items: 4,
  //   },
  // },
};
