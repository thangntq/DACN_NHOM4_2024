import { Injectable } from "@angular/core";
import { customerrequest, customerresponse } from "../models/customer.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class CustomerService {
  constructor(private http: HttpClient) {}

  getCustomer(): Observable<customerresponse[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<customerresponse[]>(
      `${environment.apiUrl}/customer/findAllActive`,
      {
        headers: headers,
      }
    );
  }

  createCustomer(
    customerrequest: customerrequest
  ): Observable<customerresponse> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<customerresponse>(
      `${environment.apiUrl}/customer/create`,
      customerrequest,

      {
        headers: headers,
      }
    );
  }

  updateCustomer(
    customerrequest: customerrequest
  ): Observable<customerresponse> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<customerresponse>(
      `${environment.apiUrl}/customer/update`,
      customerrequest,
      {
        headers: headers,
      }
    );
  }

  deleteCustomer(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/customer/delete?id=${id}`, {
      headers: headers,
    });
  }

  CustomerIdProductFind(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post(`${environment.apiUrl}/statistic/findAllProductDetailByCustomer?id=`+id, {
      headers: headers,
    });
  }


}
