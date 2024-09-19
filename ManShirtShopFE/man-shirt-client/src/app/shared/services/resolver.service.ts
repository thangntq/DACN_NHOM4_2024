import { Injectable } from "@angular/core";
import { ActivatedRouteSnapshot, Resolve, Router } from "@angular/router";
import {
  Product,
  category,
  collar,
  design,
  form,
  material,
  sleeve,
} from "../classes/product";
import { ProductService } from "./product.service";
import { Observable, of } from "rxjs";
import { catchError, map } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class Resolver implements Resolve<Product> {
  constructor(private router: Router, private productService: ProductService) {}

  // Resolver
  resolve(route: ActivatedRouteSnapshot): Observable<Product> {
    return this.productService.getProductBySlug(route.params.slug).pipe(
      map((product) => {
        if (!product) {
          // When product is empty redirect 404
          this.router.navigateByUrl("/pages/404", { skipLocationChange: true });
        }
        return product;
      }),
      catchError((error) => {
        this.router.navigateByUrl("/pages/404", { skipLocationChange: true });
        return of(null);
      })
    );
  }
}
