import { Injectable } from "@angular/core";
import { collar } from "../models/collar.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class CollarService {
  colLar: collar;
  constructor(private http: HttpClient) {}

  getCollar(): Observable<collar[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<collar[]>(`${environment.apiUrl}/collar/findAll`, {
      headers: headers,
    });
  }

  createCollar(colLar: collar): Observable<collar> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<collar>(
      `${environment.apiUrl}/collar/create`,
      colLar,
      {
        headers: headers,
      }
    );
  }

  updateCollar(colLar: collar): Observable<collar> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<collar>(
      `${environment.apiUrl}/collar/update`,
      colLar,
      {
        headers: headers,
      }
    );
  }
  deleteCollar(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/collar/delete?id=${id}`, {
      headers: headers,
    });
  }
}
