import { Injectable } from "@angular/core";
import { sanphamSta } from "../models/sanpham.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class SanphamStacService {

  constructor(private http: HttpClient) {}



  GetSanphamsta(siZe: any): Observable<any[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any[]>(`${environment.apiUrl}/statistic/findAllProductByTime`, siZe,{
      headers: headers,
    });
  }

  GetSanphamDiscount(discountId: any): Observable<any[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any[]>(`${environment.apiUrl}/statistic/findAllProductByDiscount?id=`+ discountId,{
      headers: headers,
    });
  }


}
