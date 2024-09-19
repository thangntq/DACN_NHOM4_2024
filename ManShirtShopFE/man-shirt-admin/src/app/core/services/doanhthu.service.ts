import { Injectable } from "@angular/core";

import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class DoanhthuService {

  constructor(private http: HttpClient) {}



  GetSanphamsta(siZe: any[]): Observable<any[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any[]>(`${environment.apiUrl}/statistic/getAllTotalByDate`, siZe,{
      headers: headers,
    });
  }

  GetSanphamDiscount(discountId: any): Observable<any[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any[]>(`${environment.apiUrl}/statistic/getAllTotalByDiscount?id=`+ discountId,{
      headers: headers,
    });
  }

  GetOrder(code: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(`${environment.apiUrl}/statistic/getOrderReturnByCode?code=`+ code,{
      headers: headers,
    });
  }


}
