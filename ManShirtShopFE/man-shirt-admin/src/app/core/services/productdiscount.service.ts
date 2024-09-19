import { Injectable } from "@angular/core";
import { discountproduct } from "../models/discountproduct.models";
import { ProductRespone } from "../models/product.model";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class ProductdiscountService {

  constructor(private http: HttpClient) {}

  getDiscountProduct(): Observable<discountproduct[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<discountproduct[]>(`${environment.apiUrl}/productDiscount/findAll`,{
      headers: headers,
    });
  }
  getDiscountProduct1(): Observable<ProductRespone[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<ProductRespone[]>(`${environment.apiUrl}/product/getAllByDiscount`,{
      headers: headers,
    });
  }

  createDiscountProduct(discountproduct: discountproduct): Observable<discountproduct> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<discountproduct>(
      `${environment.apiUrl}/productDiscount/create`,
      discountproduct
      ,{
        headers: headers,
      });
  }

  updateDiscountProduct(discountproduct: discountproduct): Observable<discountproduct> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<discountproduct>(
      `${environment.apiUrl}/productDiscount/update`,
      discountproduct
      ,{
        headers: headers,
      });
  }
  deleteDiscountProduct(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/productDiscount/delete?id=${id}`,{
      headers: headers,
    });
  }
  getdiscountId(discountId: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<any>(`${environment.apiUrl}/productDiscount/findAllByDiscountId?discountId=` + discountId,{
      headers: headers,
    });
  }
}
