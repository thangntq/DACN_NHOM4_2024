import {
  Component,
  OnInit,
  Injectable,
  PLATFORM_ID,
  Inject,
} from "@angular/core";
import { isPlatformBrowser } from "@angular/common";
import { Observable, of } from "rxjs";
import { TranslateService } from "@ngx-translate/core";
import { ProductService } from "../../services/product.service";
import { Product, ProductDetail } from "../../classes/product";
import { ToastrService } from "ngx-toastr";
import { AuthenticationService } from "src/app/pages/account/login/auth.service";

@Component({
  selector: "app-settings",
  templateUrl: "./settings.component.html",
  styleUrls: ["./settings.component.scss"],
})
export class SettingsComponent implements OnInit {
  public products: Product[] = [];
  public productDetails: ProductDetail[] = [];
  public search: boolean = false;
  public updateCart: boolean = false;

  public languages = [
    {
      name: "English",
      code: "en",
    },
    {
      name: "French",
      code: "fr",
    },
  ];

  public currencies = [
    {
      name: "Euro",
      currency: "EUR",
      price: 0.9, // price of euro
    },
    {
      name: "Rupees",
      currency: "INR",
      price: 70.93, // price of inr
    },
    {
      name: "Pound",
      currency: "GBP",
      price: 0.78, // price of euro
    },
    {
      name: "Dollar",
      currency: "USD",
      price: 1, // price of usd
    },
  ];

  constructor(
    @Inject(PLATFORM_ID) private platformId: Object,
    private translate: TranslateService,
    public productService: ProductService,
    private toastrService: ToastrService,
    private authService: AuthenticationService
  ) {
    this.productService.getProducts().subscribe({
      next: (value) => {
        this.products = value;
      },
      error: (message) => {
        this.toastrService.error("Lỗi khi lấy danh sách sản phẩm");
      },
    });
    this.productService.cartItems.subscribe((response) => {
      this.productDetails = response;
      this.updateCart = !this.updateCart;
    });
  }
  isLogin: boolean;
  ngOnInit(): void {
    this.isLogin = this.authService.isLogin();
  }

  searchToggle() {
    this.search = !this.search;
  }

  changeLanguage(code) {
    if (isPlatformBrowser(this.platformId)) {
      this.translate.use(code);
    }
  }

  get getTotal(): Observable<number> {
    if (this.products && this.products.length > 0) {
      return this.productService.cartTotalAmount(this.products);
    }
    return of(0);
  }

  removeItem(product: any) {
    this.productService.removeCartItem(product);
  }

  changeCurrency(currency: any) {
    this.productService.Currency = currency;
  }
  getProduct(
    productListDatas: Product[],
    productDetail: ProductDetail
  ): Product {
    const product = productListDatas.find(
      (p) => p.id === productDetail.productId
    );
    return product ? product : null;
  }

  getImageProductDetail(
    productListDatas: Product[],
    productDetail: ProductDetail
  ) {
    const product = productListDatas.find(
      (p) => p.id === productDetail.productId
    );
    return product.productImage.filter(
      (image) => image.color.id === productDetail.color.id && image.mainImage
    );
  }
  increment(product, qty = 1) {
    this.productService.updateCartQuantity(product, qty);
  }

  // Decrement
  decrement(product, qty = -1) {
    this.productService.updateCartQuantity(product, qty);
  }
}
