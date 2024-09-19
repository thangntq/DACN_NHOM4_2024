import { Injectable } from "@angular/core";
import { discount } from "../models/discount.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class DiscountService {
  constructor(private http: HttpClient) {}

  getDiscount(): Observable<discount[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<discount[]>(`${environment.apiUrl}/discount/findAll`, {
      headers: headers,
    });
  }

  createDiscount(disCount: discount): Observable<discount> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<discount>(
      `${environment.apiUrl}/discount/create`,
      disCount,
      {
        headers: headers,
      }
    );
  }

  updateDiscount(disCount: discount): Observable<discount> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<discount>(
      `${environment.apiUrl}/discount/update`,
      disCount,
      {
        headers: headers,
      }
    );
  }

  deleteDiscount(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/discount/delete?id=${id}`, {
      headers: headers,
    });
  }

  getdiscountId(discountID: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<any>(
      `${environment.apiUrl}/discount/findById?discountID=` + discountID,
      {
        headers: headers,
      }
    );
  }
}
