import { Injectable } from "@angular/core";
import { discount } from "../classes/product";
import { ActivatedRouteSnapshot, Resolve, Router } from "@angular/router";
import { HomeService } from "./home.service";
import { Observable, of } from "rxjs";
import { catchError, map } from "rxjs/operators";

@Injectable({
  providedIn: "root",
})
export class DiscountResolver implements Resolve<discount> {
  constructor(private router: Router, private homeService: HomeService) {}

  // Resolver
  resolve(route: ActivatedRouteSnapshot): Observable<discount> {
    return this.homeService
      .findByNamesDiscount(route.params.discount.replace("-", " "))
      .pipe(
        map((product) => {
          if (!product) {
            // When product is empty redirect 404
            this.router.navigateByUrl("/pages/404", {
              skipLocationChange: true,
            });
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
