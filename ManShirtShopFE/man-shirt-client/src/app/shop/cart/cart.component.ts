import { Component, OnInit } from "@angular/core";
import { Observable, of } from "rxjs";
import { ProductService } from "../../shared/services/product.service";
import { Product, ProductDetail } from "../../shared/classes/product";
import { Router } from "@angular/router";
import { ToastrService } from "ngx-toastr";
import { OrderService } from "src/app/shared/services/order.service";
import { AuthenticationService } from "src/app/pages/account/login/auth.service";

@Component({
  selector: "app-cart",
  templateUrl: "./cart.component.html",
  styleUrls: ["./cart.component.scss"],
})
export class CartComponent implements OnInit {
  public productDetails: ProductDetail[];
  public products: Product[] = [];
  isCheckout = false;
  selectedProductDetail: ProductDetail[] = [];
  selectAllChecked: boolean;
  isLogin: boolean;
  constructor(
    public productService: ProductService,
    private router: Router,
    private toastrService: ToastrService,
    private orderService: OrderService,
    private authService: AuthenticationService
  ) {
    this.isLogin = this.authService.isLogin();
    this.productService.getProducts().subscribe({
      next: (value) => {
        this.products = value;
      },
      error: (message) => {
        this.toastrService.error("Lỗi khi lấy danh sách sản phẩm");
      },
    });
    this.productService.cartItems.subscribe((response) => {
      const request = response.map((item) => ({
        id: item.id,
        quantity: item.quantity,
      }));

      if (request.length > 0) {
        this.orderService.checkCart(request).subscribe({
          next: (item) => {
            const validItemIds = item.data.map((item) => item.productDetail.id);
            const invalidCartItems = response.filter(
              (cartItem) => !validItemIds.includes(cartItem.id)
            );
            invalidCartItems.forEach((cartItem) => {
              this.productService.removeCartItem(cartItem);
            });
            this.productService.cartItems.subscribe((response) => {
              this.productDetails = response;
            });
          },
        });
      }
    });
  }

  ngOnInit(): void {
    localStorage.removeItem("returnUrl");
  }
  onProductSelected(event: any, productDetail: any) {
    const isChecked = event.target.checked;
    if (isChecked) {
      this.selectedProductDetail.push(productDetail);
    } else {
      const index = this.selectedProductDetail.indexOf(productDetail);
      if (index >= 0) {
        this.selectedProductDetail.splice(index, 1);
      }
    }
    this.productService.selectTotalAmount(
      this.selectedProductDetail,
      this.products
    );
  }

  public get getTotal(): Observable<number> {
    if (
      this.selectedProductDetail &&
      this.selectedProductDetail.length != 0 &&
      this.products &&
      this.products.length != 0
    ) {
      return this.productService.selectTotalAmount(
        this.selectedProductDetail,
        this.products
      );
    }
    return of(0);
  }

  // Increament
  increment(product, qty = 1) {
    this.productService.updateCartQuantity(product, qty);
  }

  // Decrement
  decrement(product, qty = -1) {
    this.productService.updateCartQuantity(product, qty);
  }

  public removeItem(product: any) {
    this.productService.removeCartItem(product);
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
  checkout() {
    this.isCheckout = true;
    if (this.selectedProductDetail.length > 0) {
      const select: any[] = [];
      this.selectedProductDetail.forEach((productDetail) => {
        const item: any = {};
        item.id = productDetail.id;
        item.quantity = productDetail.quantity;
        select.push(item);
      });
      if (select.length > 0) {
        localStorage.setItem("returnUrl", "shop/cart");
        this.orderService.createCheckout(select).subscribe({
          next: (value) => {
            if (value.code) {
              this.router.navigate(["checkout/" + value.code]);
            }
            this.isCheckout = false;
          },
          error: (err) => {
            this.isCheckout = false;
          },
        });
      }
    } else {
      this.toastrService.warning("Vui lòng chọn sản phẩm để thanh toán");
      this.isCheckout = false;
    }
  }
  selectAll() {
    this.selectedProductDetail = [];
    this.selectAllChecked = !this.selectAllChecked;
    if (this.selectAllChecked) {
      this.productDetails.forEach((detail) => {
        this.selectedProductDetail.push(detail);
      });
    }
    this.productService.selectTotalAmount(
      this.selectedProductDetail,
      this.products
    );
  }
}
