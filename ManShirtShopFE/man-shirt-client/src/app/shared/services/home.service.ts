import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";

@Injectable({
  providedIn: "root",
})
export class HomeService {
  constructor(private http: HttpClient) {}
  getTopNewProduct(): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/product/getTopNewProduct`
    );
  }
  getTopProduct(): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/product/getTopProduct`
    );
  }
  getTopDiscountProduct(): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/product/getTopDiscountProduct`
    );
  }
  findAllDiscount(): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/properties/findAllDiscount`
    );
  }
  findByNamesDiscount(name: any): Observable<any> {
    return this.http.get<any>(
      `${environment.apiUrl}/client/api/properties/findByNamesDiscount?name=` +
        name
    );
  }
}
