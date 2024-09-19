import { Injectable } from "@angular/core";
import { adress } from "../models/address.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class AddressService {
  constructor(private http: HttpClient) {}

  getAddressByIdCustomer(
    customer_id: number,
    status: number
  ): Observable<adress[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<adress[]>(
      `${environment.apiUrl}/adresses/customer/${customer_id}/status?status=` +
        status,
      {
        headers: headers,
      }
    );
  }

  createAddress(address: adress): Observable<adress> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<adress>(
      `${environment.apiUrl}/adresses/create`,
      address,
      {
        headers: headers,
      }
    );
  }

  updateAddress(adress: adress): Observable<adress> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<adress>(
      `${environment.apiUrl}/adresses/update`,
      adress,
      {
        headers: headers,
      }
    );
  }
  deleteAddress(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/adresses/delete?id=${id}`, {
      headers: headers,
    });
  }
}
