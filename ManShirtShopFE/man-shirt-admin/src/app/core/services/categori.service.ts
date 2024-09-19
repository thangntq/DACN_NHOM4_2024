import { Injectable } from "@angular/core";
import { categorirequest, categoriresponse } from "../models/categori.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class CategoriService {
  constructor(private http: HttpClient) {}

  getCollar(): Observable<categoriresponse[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<categoriresponse[]>(
      `${environment.apiUrl}/category/findAll`,
      {
        headers: headers,
      }
    );
  }

  createCollar(colLar: categorirequest): Observable<categoriresponse> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<categoriresponse>(
      `${environment.apiUrl}/category/create`,
      colLar,
      {
        headers: headers,
      }
    );
  }

  updateCollar(colLar: categorirequest): Observable<categoriresponse> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<categoriresponse>(
      `${environment.apiUrl}/category/update`,
      colLar,
      {
        headers: headers,
      }
    );
  }
  deleteCollar(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/category/delete?id=${id}`, {
      headers: headers,
    });
  }
}
