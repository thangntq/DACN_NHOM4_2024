import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";
import { Observable, combineLatest, from, of } from "rxjs";
import { map, reduce, catchError } from "rxjs/operators";
import { ToastrService } from "ngx-toastr";
import { Product, ProductDetail, color, size } from "../classes/product";
import { environment } from "src/environments/environment";

const state = {
  wishlist: JSON.parse(localStorage["wishlistItems"] || "[]"),
  compare: JSON.parse(localStorage["compareItems"] || "[]"),
  cart: JSON.parse(localStorage["cartItems"] || "[]"),
  history: JSON.parse(localStorage["history"] || "[]"),
};

@Injectable({
  providedIn: "root",
})
export class ProductService {
  public Currency = { name: "Viet Nam Dong", currency: "VND", price: 1 }; // Default Currency
  public OpenCart: boolean = false;
  public Products;

  constructor(private http: HttpClient, private toastrService: ToastrService) {}

  /*
    ---------------------------------------------
    ---------------  Product  -------------------
    ---------------------------------------------
  */

  // Product
  private getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(
      `${environment.apiUrl}/client/api/product/getAll`
    );
  }
  getFilter(request: any): Observable<Product[]> {
    return this.http.post<Product[]>(
      `${environment.apiUrl}/client/api/product/getFillter`,
      request
    );
  }
  getProductById(id: any): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/product/getById?id=` + id
    );
  }

  // Trả về danh sách sản phẩm qua Observable
  public getProducts(): Observable<Product[]> {
    return this.getAllProducts().pipe(
      catchError((err) => {
        this.toastrService.error("Lỗi khi lấy danh sách sản phẩm");
        return of([]);
      })
    );
  }
  // Get Products By Slug
  public getProductBySlug(slug: string): Observable<Product> {
    return this.getAllProducts().pipe(
      map((products) => {
        return products.find((product) => {
          return product.name.replace(/\s/g, "-") === slug.replace(/\s/g, "-");
        });
      }),
      catchError((error) => {
        this.toastrService.error("Lỗi khi lấy sản phẩm");
        return of(null);
      })
    );
  }
  /*
    ---------------------------------------------
    ---------------  Wish List  -----------------
    ---------------------------------------------
  */

  // Get Wishlist Items
  public get wishlistItems(): Observable<Product[]> {
    const itemsStream = new Observable((observer) => {
      observer.next(state.wishlist);
      observer.complete();
    });
    return <Observable<Product[]>>itemsStream;
  }

  // Add to Wishlist
  public addToWishlist(product): any {
    const wishlistItem = state.wishlist.find((item) => item.id === product.id);
    if (!wishlistItem) {
      state.wishlist.push({
        ...product,
      });
    }
    this.toastrService.success("Đã thêm sản phẩm vào danh sách wishlist");
    localStorage.setItem("wishlistItems", JSON.stringify(state.wishlist));
    return true;
  }

  // Remove Wishlist items
  public removeWishlistItem(product: Product): any {
    const index = state.wishlist.indexOf(product);
    state.wishlist.splice(index, 1);
    localStorage.setItem("wishlistItems", JSON.stringify(state.wishlist));
    return true;
  }

  /*
    ---------------------------------------------
    -------------  Compare Product  -------------
    ---------------------------------------------
  */

  // Get Compare Items
  public get compareItems(): Observable<Product[]> {
    const itemsStream = new Observable((observer) => {
      observer.next(state.compare);
      observer.complete();
    });
    return <Observable<Product[]>>itemsStream;
  }

  // Add to Compare
  public addToCompare(product): any {
    const compareItem = state.compare.find((item) => item.id === product.id);
    if (!compareItem) {
      state.compare.push({
        ...product,
      });
    }
    this.toastrService.success("Sản phẩm đã được thêm vào danh sách so sánh");
    localStorage.setItem("compareItems", JSON.stringify(state.compare));
    return true;
  }

  // Remove Compare items
  public removeCompareItem(product: Product): any {
    const index = state.compare.indexOf(product);
    state.compare.splice(index, 1);
    localStorage.setItem("compareItems", JSON.stringify(state.compare));
    return true;
  }

  /*
    ---------------------------------------------
    -----------------  Cart  --------------------
    ---------------------------------------------
  */

  // Get Cart Items
  public get cartItems(): Observable<ProductDetail[]> {
    const itemsStream = new Observable((observer) => {
      observer.next(state.cart);
      observer.complete();
    });
    return <Observable<ProductDetail[]>>itemsStream;
  }
  public get getCartItems(): Observable<ProductDetail[]> {
    return of(state.cart);
  }
  // Add to Cart
  public addToCart(productDetail): any {
    const cartItem = state.cart.find((item) => item.id === productDetail.id);
    const qty = productDetail.quantity ? productDetail.quantity : 1;
    const items = cartItem ? cartItem : productDetail;
    const stock = this.calculateStockCounts(items, qty);

    if (!stock) return false;

    if (cartItem) {
      cartItem.quantity += qty;
    } else {
      state.cart.push({
        ...productDetail,
        quantity: qty,
      });
    }

    this.OpenCart = true; // If we use cart variation modal
    this.toastrService.success("Thêm vào giỏ hàng thành công");
    localStorage.setItem("cartItems", JSON.stringify(state.cart));
    return true;
  }

  // Update Cart Quantity
  public updateCartQuantity(
    productDetail: ProductDetail,
    quantity: number
  ): ProductDetail | boolean {
    const index = state.cart.findIndex((item) => item === productDetail);
    if (index !== -1) {
      const newQuantity = state.cart[index].quantity + quantity;
      const stock = this.calculateStockCounts(state.cart[index], quantity);
      if (newQuantity !== 0 && stock) {
        state.cart[index].quantity = newQuantity;
        localStorage.setItem("cartItems", JSON.stringify(state.cart));
        return state.cart[index];
      }
    }
    return false;
  }

  // Calculate Stock Counts
  public calculateStockCounts(product, quantity) {
    const qty = product.quantity + quantity;
    const stock = product.stock;
    if (stock < qty || stock == 0) {
      this.toastrService.error(
        "You can not add more items than available. In stock " +
          stock +
          " items."
      );
      return false;
    }
    return true;
  }

  // Remove Cart items
  public removeCartItem(productDetail: ProductDetail): any {
    const index = state.cart.indexOf(productDetail);
    state.cart.splice(index, 1);
    localStorage.setItem("cartItems", JSON.stringify(state.cart));
    return true;
  }
  public clearCart(): void {
    state.cart = [];
    localStorage.removeItem("cartItems");
  }
  public removeCartItemById(id: any): any {
    const index = state.cart.findIndex((item) => item.id === id);
    state.cart.splice(index, 1);
    localStorage.setItem("cartItems", JSON.stringify(state.cart));
    return true;
  }

  public cartTotalAmount(products: Product[]): Observable<number> {
    return this.cartItems.pipe(
      map((product: ProductDetail[]) => {
        return product.reduce((prev, curr: ProductDetail) => {
          let product = this.getProduct(products, curr);
          let price = product?.price;
          if (product?.discount) {
            price = product.price - (product.price * product.discount) / 100;
          }
          return (prev + price * curr.quantity) * this.Currency.price;
        }, 0);
      })
    );
  }
  public selectTotalAmount(
    selectedProducts: ProductDetail[],
    products: Product[]
  ): Observable<number> {
    return from(selectedProducts).pipe(
      map((productDetail: ProductDetail) => {
        const product = this.getProduct(products, productDetail);
        let price = product.price;
        if (product.discount) {
          price = product.price - (product.price * product.discount) / 100;
        }
        return price * productDetail.quantity;
      }),
      reduce((total, currentValue) => total + currentValue, 0),
      map((total) => total * this.Currency.price)
    );
  }
  private getProduct(
    productListDatas: Product[],
    productDetail: ProductDetail
  ): Product | null {
    return (
      productListDatas.find((p) => p.id === productDetail.productId) || null
    );
  }
  /*
    ---------------------------------------------
    ------------  Filter Product  ---------------
    ---------------------------------------------
  */

  // Get Product Filter
  // public filterProducts(filter: any): Observable<Product[]> {
  //   return this.products.pipe(
  //     map((product) =>
  //       product.filter((item: Product) => {
  //         if (!filter.length) return true;
  //         const Tags = filter.some((prev) => {
  //           // Match Tags
  //           if (item.tags) {
  //             if (item.tags.includes(prev)) {
  //               return prev;
  //             }
  //           }
  //         });
  //         return Tags;
  //       })
  //     )
  //   );
  // }

  // Sorting Filter
  public sortProducts(products: Product[], payload: string): any {
    if (payload === "ascending") {
      return products.sort((a, b) => {
        if (a.id > b.id) {
          return -1;
        } else if (a.id > b.id) {
          return 1;
        }
        return 0;
      });
    } else if (payload === "a-z") {
      return products.sort((a, b) => {
        if (a.name < b.name) {
          return -1;
        } else if (a.name > b.name) {
          return 1;
        }
        return 0;
      });
    } else if (payload === "z-a") {
      return products.sort((a, b) => {
        if (a.name > b.name) {
          return -1;
        } else if (a.name < b.name) {
          return 1;
        }
        return 0;
      });
    } else if (payload === "low") {
      return products.sort((a, b) => {
        if (a.price < b.price) {
          return -1;
        } else if (a.price > b.price) {
          return 1;
        }
        return 0;
      });
    } else if (payload === "high") {
      return products.sort((a, b) => {
        if (a.price > b.price) {
          return -1;
        } else if (a.price < b.price) {
          return 1;
        }
        return 0;
      });
    }
  }

  /*
    ---------------------------------------------
    ------------- Product Pagination  -----------
    ---------------------------------------------
  */
  public getPager(
    totalItems: number,
    currentPage: number = 1,
    pageSize: number = 20
  ) {
    // calculate total pages
    let totalPages = Math.ceil(totalItems / pageSize);

    // Paginate Range
    let paginateRange = 3;

    // ensure current page isn't out of range
    if (currentPage < 1) {
      currentPage = 1;
    } else if (currentPage > totalPages) {
      currentPage = totalPages;
    }

    let startPage: number, endPage: number;
    if (totalPages <= 5) {
      startPage = 1;
      endPage = totalPages;
    } else if (currentPage < paginateRange - 1) {
      startPage = 1;
      endPage = startPage + paginateRange - 1;
    } else {
      startPage = currentPage - 1;
      endPage = currentPage + 1;
    }

    // calculate start and end item indexes
    let startIndex = (currentPage - 1) * pageSize;
    let endIndex = Math.min(startIndex + pageSize - 1, totalItems - 1);

    // create an array of pages to ng-repeat in the pager control
    let pages = Array.from(Array(endPage + 1 - startPage).keys()).map(
      (i) => startPage + i
    );

    // return object with all pager properties required by the view
    return {
      totalItems: totalItems,
      currentPage: currentPage,
      pageSize: pageSize,
      totalPages: totalPages,
      startPage: startPage,
      endPage: endPage,
      startIndex: startIndex,
      endIndex: endIndex,
      pages: pages,
    };
  }

  getAllColors(product: Product): color[] {
    const colors = product.productDetail.map((detail) => detail.color);
    const uniqueColors = colors.filter(
      (color, index, self) => index === self.findIndex((c) => c.id === color.id)
    );
    return uniqueColors;
  }
  getAllSizes(product: Product): size[] {
    const sizes = product.productDetail.map((detail) => detail.size);
    const uniqueSizes = sizes.filter(
      (size, index, self) => index === self.findIndex((s) => s.id === size.id)
    );
    return uniqueSizes;
  }
  getQuantity(
    details: ProductDetail[],
    selectedColor: color,
    selectedSize: size
  ): number {
    const detail = details.find(
      (d) => d.color.id === selectedColor.id && d.size.id === selectedSize.id
    );
    return detail ? detail.quantity : 0;
  }
  getProductDetail(
    details: ProductDetail[],
    selectedColor: color,
    selectedSize: size
  ): ProductDetail {
    const detail = details.find(
      (d) => d.color.id === selectedColor.id && d.size.id === selectedSize.id
    );
    return detail ? detail : null;
  }

  public get historyItems(): Observable<Product[]> {
    const itemsStream = new Observable((observer) => {
      observer.next(state.history);
      observer.complete();
    });
    return <Observable<Product[]>>itemsStream;
  }
  // Add to Wishlist
  public addToHistory(product): any {
    const historytItem = state.history.find((item) => item.id === product.id);
    if (!historytItem) {
      state.history.push({
        ...product,
      });
    }
    localStorage.setItem("history", JSON.stringify(state.history));
    return true;
  }

  // Remove Wishlist items
  public removeHistoryItem(product: Product): any {
    const index = state.history.indexOf(product);
    state.history.splice(index, 1);
    localStorage.setItem("history", JSON.stringify(state.history));
    return true;
  }
}
