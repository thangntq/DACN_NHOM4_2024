import { Injectable } from "@angular/core";
import { cart, cartDetail } from "../models/cart.model";
import { ProductDetailRespone } from "../models/productDetail.model";

@Injectable({
  providedIn: "root",
})
export class CartService {
  private carts: cart[] = [];

  constructor() {
    const savedCarts = localStorage.getItem("carts");
    if (savedCarts) {
      this.carts = JSON.parse(savedCarts);
    }
  }
  addCart(cart: cart): void {
    this.carts.push(cart);
    this.saveCarts();
  }
  addToCart(cart: cart, item: ProductDetailRespone, quantity: number): void {
    const existingItemIndex = cart.cartDetail.findIndex(
      (x) => x.detail.id === item.id
    );

    if (existingItemIndex !== -1) {
      cart.cartDetail[existingItemIndex].quantity += quantity;
    } else {
      cart.cartDetail.push({ quantity: quantity, detail: item });
    }
    this.saveCarts();
  }

  getCarts(): cart[] {
    return this.carts;
  }

  removeCart(cart: cart): void {
    const index = this.carts.findIndex((x) => x.id === cart.id);
    if (index !== -1) {
      this.carts.splice(index, 1);
      this.saveCarts();
    }
  }
  clearCart(cart: cart): void {
    cart.cartDetail = [];
    this.saveCarts();
  }

  //   getTotalPrice(cart: cart): number {
  //     let totalPrice = 0;
  //     cart.cartDetail.forEach((item) => {
  //       totalPrice += item.detail.price * item.quantity;
  //     });
  //     return totalPrice;
  //   }

  private saveCarts(): void {
    localStorage.setItem("carts", JSON.stringify(this.carts));
  }

  removeItemFromCart(cart: cart, item: ProductDetailRespone): void {
    const index = cart.cartDetail.findIndex((x) => x.detail.id === item.id);
    if (index !== -1) {
      cart.cartDetail.splice(index, 1);
    }
    this.saveCarts();
  }
  increaseItemQuantity(cart: cart, item: ProductDetailRespone): void {
    const index = cart.cartDetail.findIndex((x) => x.detail.id === item.id);
    if (index !== -1) {
      cart.cartDetail[index].quantity++;
    }
    this.saveCarts();
  }

  setQuantity(cart: cart, item: ProductDetailRespone, quantity: number): void {
    const index = cart.cartDetail.findIndex((x) => x.detail.id === item.id);
    if (index !== -1) {
      cart.cartDetail[index].quantity = quantity;
    }
    this.saveCarts();
  }

  decreaseItemQuantity(cart: cart, item: ProductDetailRespone): void {
    const cartDetail = cart.cartDetail.find((x) => x.detail.id === item.id);
    if (cartDetail.quantity > 1) {
      cartDetail.quantity--;
    }
    this.saveCarts();
  }
}
