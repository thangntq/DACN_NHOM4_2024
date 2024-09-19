import { Injectable } from "@angular/core";
import { size } from "../models/size.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class SizeService {
  siZe: size;
  constructor(private http: HttpClient) {}

  getSize(): Observable<size[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<size[]>(`${environment.apiUrl}/size/findAll`,{
      headers: headers,
    });
  }

  createSize(siZe: size): Observable<size> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<size>(`${environment.apiUrl}/size/create`, siZe,{
      headers: headers,
    });
  }

  updateSize(siZe: size): Observable<size> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<size>(`${environment.apiUrl}/size/update`, siZe,{
      headers: headers,
    });
  }

  deleteSize(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/size/delete?id=${id}`,{
      headers: headers,
    });
  }
}
