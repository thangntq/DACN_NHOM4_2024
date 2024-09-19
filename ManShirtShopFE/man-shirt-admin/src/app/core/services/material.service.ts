import { Injectable } from "@angular/core";
import { material } from "../models/material.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class MaterialService {
  mateRial: material;
  constructor(private http: HttpClient) {}

  getMaterial(): Observable<material[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<material[]>(`${environment.apiUrl}/material/findAll`, {
      headers: headers,
    });
  }

  createMaterial(mateRial: material): Observable<material> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<material>(
      `${environment.apiUrl}/material/create`,
      mateRial,
      {
        headers: headers,
      }
    );
  }

  updateMaterial(mateRial: material): Observable<material> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<material>(
      `${environment.apiUrl}/material/update`,
      mateRial,
      {
        headers: headers,
      }
    );
  }
  deleteMaterial(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/material/delete?id=${id}`, {
      headers: headers,
    });
  }
}
