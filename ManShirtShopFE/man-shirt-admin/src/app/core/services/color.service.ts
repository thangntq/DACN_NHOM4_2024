import { Injectable } from "@angular/core";
import { color } from "../models/color.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class ColorService {
  coLor: color;
  constructor(private http: HttpClient) {}

  getColor(): Observable<color[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<color[]>(`${environment.apiUrl}/color/findAll`, {
      headers: headers,
    });
  }

  createColor(coLor: color): Observable<color> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<color>(`${environment.apiUrl}/color/create`, coLor, {
      headers: headers,
    });
  }

  updateColor(coLor: color): Observable<color> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<color>(`${environment.apiUrl}/color/update`, coLor, {
      headers: headers,
    });
  }
  deleteColor(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/color/delete?id=${id}`, {
      headers: headers,
    });
  }
}
