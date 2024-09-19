import { Injectable } from "@angular/core";
import { design } from "../models/design.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class DesignService {
  deSign: design;
  constructor(private http: HttpClient) {}

  getDesign(): Observable<design[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<design[]>(`${environment.apiUrl}/design/findAll`, {
      headers: headers,
    });
  }

  createDesign(deSign: design): Observable<design> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<design>(
      `${environment.apiUrl}/design/create`,
      deSign,
      {
        headers: headers,
      }
    );
  }

  updateDesign(deSign: design): Observable<design> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<design>(
      `${environment.apiUrl}/design/update`,
      deSign,
      {
        headers: headers,
      }
    );
  }
  deleteDesign(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/design/delete?id=${id}`, {
      headers: headers,
    });
  }
}
