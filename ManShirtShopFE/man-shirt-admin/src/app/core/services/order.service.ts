import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Order } from "../models/order.models";
import { environment } from "src/environments/environment";
import { Observable } from "rxjs";
import { HeadersUtil } from "../util/headers.util";
import { ParamUtil } from "../util/param.util";
import { ApiUrlUtil, RequestParam } from "../util/api-url.util";

@Injectable({
  providedIn: "root",
})
export class OrderService {
  constructor(private http: HttpClient) {}

  createOrder(order: Order): Observable<Order> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<Order>(`${environment.apiUrl}/order/create`, order, {
      headers: headers,
    });
  }
  updateOrder(order: Order): Observable<Order> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<Order>(`${environment.apiUrl}/order/update`, order, {
      headers: headers,
    });
  }
  findByStatus(status: any): Observable<Order[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    const url = ApiUrlUtil.buildQueryString(
      environment.apiUrl + "/order/findByStaus?status=" + status
    );
    return this.http.get<any>(url, { headers: headers });
  }
  updateStatusConfirm(status: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(
      `${environment.apiUrl}/order/updateStatusXacNhanDon`,
      status,
      { headers: headers }
    );
  }
  updateStatusHuy(status: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(
      `${environment.apiUrl}/order/updateStatusHuy?id=` + status,
      { headers: headers }
    );
  }
  updateStatusXacGiaoHang(status: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(
      `${environment.apiUrl}/order/updateStatusXacGiaoHang?lst=` + status,
      { headers: headers }
    );
  }
  updateStatusGiaoThatBai(status: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(
      `${environment.apiUrl}/order/updateStatusGiaoThatBai?id=` + status,
      {
        headers: headers,
      }
    );
  }
  updateStatusGiaoThanhCong(status: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(
      `${environment.apiUrl}/order/updateStatusThanhCong`,
      status,
      {
        headers: headers,
      }
    );
  }
  findOrderByCode(code: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<any>(
      `${environment.apiUrl}/order/findByCode?Code=` + code,
      {
        headers: headers,
      }
    );
  }

  findReturnByStatus(code: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<any>(
      `${environment.apiUrl}/return/getAll?status=` + code,
      {
        headers: headers,
      }
    );
  }

  updateDangGiaoReturn(returnId: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(
      `${environment.apiUrl}/return/updateDangGiao/` + returnId,
      {
        headers: headers,
      }
    );
  }
  updateTuChoiReturn(returnId: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(
      `${environment.apiUrl}/return/updateRefuse/` + returnId,
      {
        headers: headers,
      }
    );
  }
  updateSuccessReturn(returnId: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(
      `${environment.apiUrl}/return/updateSuccess/` + returnId,
      {
        headers: headers,
      }
    );
  }
  findReturnByCode(code: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<any>(`${environment.apiUrl}/return/` + code, {
      headers: headers,
    });
  }
  createReturn(request: any): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<any>(`${environment.apiUrl}/return/add`, request, {
      headers: headers,
    });
  }
}
