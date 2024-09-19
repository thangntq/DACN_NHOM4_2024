import { Injectable } from "@angular/core";
import { sleeve } from "../models/sleeve.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class SleeveService {
  sleeVe: sleeve;
  constructor(private http: HttpClient) {}

  getSleeve(): Observable<sleeve[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<sleeve[]>(`${environment.apiUrl}/sleeve/findAll`,{
      headers: headers,
    });
  }

  createSleeve(sleeVe: sleeve): Observable<sleeve> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<sleeve>(
      `${environment.apiUrl}/sleeve/create`,
      sleeVe
      ,{
        headers: headers,
      });
  }

  updateSleeve(sleeVe: sleeve): Observable<sleeve> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<sleeve>(
      `${environment.apiUrl}/sleeve/update`,
      sleeVe
      ,{
        headers: headers,
      });
  }
  deleteSleeve(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/sleeve/delete?id=${id}`,{
      headers: headers,
    });
  }
}
