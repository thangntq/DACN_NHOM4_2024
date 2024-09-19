import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { voucherRequest, voucherResponse } from "../models/voucher.models";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class VoucherService {
  constructor(private http: HttpClient) {}

  getVoucher(): Observable<voucherResponse[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<voucherResponse[]>(
      `${environment.apiUrl}/voucher/findAll`,
      {
        headers: headers,
      }
    );
  }

  createVoucher(voucher: voucherRequest): Observable<voucherResponse> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<voucherResponse>(
      `${environment.apiUrl}/voucher/create`,
      voucher,
      {
        headers: headers,
      }
    );
  }

  updateVoucher(voucher: voucherRequest): Observable<voucherResponse> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<voucherResponse>(
      `${environment.apiUrl}/voucher/update`,
      voucher,
      {
        headers: headers,
      }
    );
  }
  deleteVoucher(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/voucher/delete?id=${id}`, {
      headers: headers,
    });
  }

  getActiveVouchers(): Observable<voucherResponse[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<voucherResponse[]>(
      `${environment.apiUrl}/voucher/getActiveVouchers`,
      {
        headers: headers,
      }
    );
  }
}
