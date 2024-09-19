import { Injectable } from "@angular/core";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { categoryRequest, categoryResponse } from "../models/category.models";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class CategoryService {
  constructor(private http: HttpClient) {}

  getCategory(): Observable<categoryResponse[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<categoryResponse[]>(
      `${environment.apiUrl}/category/findAll`,
      {
        headers: headers,
      }
    );
  }

  createCategory(category: categoryRequest): Observable<categoryResponse> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<categoryResponse>(
      `${environment.apiUrl}/category/create`,
      category,
      {
        headers: headers,
      }
    );
  }

  updateCategory(category: categoryRequest): Observable<categoryResponse> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<categoryResponse>(
      `${environment.apiUrl}/category/update`,
      category,
      {
        headers: headers,
      }
    );
  }
  deleteCategory(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/category/delete?id=${id}`, {
      headers: headers,
    });
  }
}
