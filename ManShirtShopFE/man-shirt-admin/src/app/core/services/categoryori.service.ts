import { Injectable } from "@angular/core";
import {  categoryori } from "../models/categoryorigin.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class CategoryoriService {
  colLar: categoryori;
  constructor(private http: HttpClient) {}

  getCollar(): Observable<categoryori[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<categoryori[]>(`${environment.apiUrl}/originalCategory/findAll`,{
      headers: headers,
    });

  }

  createCollar(colLar: categoryori): Observable<categoryori> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<categoryori>(
      `${environment.apiUrl}/originalCategory/create`,
      colLar
    );
  }

  updateCollar(colLar: categoryori): Observable<categoryori> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<categoryori>(
      `${environment.apiUrl}/originalCategory/update`,
      colLar,{
        headers: headers,
      });
  }
  deleteCollar(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(
      `${environment.apiUrl}/originalCategory/delete?id=${id}`,{
        headers: headers,
      });
  }
}
