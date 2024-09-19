import { Injectable } from "@angular/core";
import { statictical,staticticalByTime,topProduct,topCustomer } from "../models/statictical.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class staticticalService {

  constructor(private http: HttpClient) {}

  getAllTotal(): Observable<statictical>{
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<statictical>(
      `${environment.apiUrl}/statistic/getAllTotal`,
      {
        headers: headers,
      }
    );
  }

  findCustommerByDate(staticticalByTime: staticticalByTime): Observable<staticticalByTime> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<staticticalByTime>(
      `${environment.apiUrl}/statistic/findCustomerByDate`,
      staticticalByTime,
      {
        headers: headers,
      }
    );
  }

  findProductByDate(staticticalByTime: staticticalByTime): Observable<staticticalByTime> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<staticticalByTime>(
      `${environment.apiUrl}/statistic/findProductDate`,
      staticticalByTime,
      {
        headers: headers,
      }
    );
  }


  findTotalByMonth(year: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(
      `${environment.apiUrl}/statistic/findTotalByMonth?year=`+year,

      {
        headers: headers,
      }
    );
  }

  findOrderByMonth(year: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(
      `${environment.apiUrl}/statistic/countOrderByMonth?year=`+year,

      {
        headers: headers,
      }
    );
  }

  getTopProduct(): Observable<topProduct[]>{
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<topProduct[]>(
      `${environment.apiUrl}/statistic/findtopProduct`,
      {
        headers: headers,
      }
    );
  }

  getTopCustomer(): Observable<topCustomer[]>{
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<topCustomer[]>(
      `${environment.apiUrl}/statistic/findTopCustomer`,
      {
        headers: headers,
      }
    );
  }

}
