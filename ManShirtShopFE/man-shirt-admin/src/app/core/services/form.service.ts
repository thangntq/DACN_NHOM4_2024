import { Injectable } from "@angular/core";
import { form } from "../models/form.models";
import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Observable } from "rxjs";
import { environment } from "src/environments/environment";
import { HeadersUtil } from "../util/headers.util";

@Injectable({
  providedIn: "root",
})
export class FormService {
  foRm: form;
  constructor(private http: HttpClient) {}

  getForm(): Observable<form[]> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.get<form[]>(`${environment.apiUrl}/form/findAll`, {
      headers: headers,
    });
  }

  createForm(foRm: form): Observable<form> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<form>(`${environment.apiUrl}/form/create`, foRm, {
      headers: headers,
    });
  }

  updateForm(foRm: form): Observable<form> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.post<form>(`${environment.apiUrl}/form/update`, foRm, {
      headers: headers,
    });
  }
  deleteForm(id: number): Observable<any> {
    const headers: HttpHeaders = HeadersUtil.getHeadersAuth();
    return this.http.delete(`${environment.apiUrl}/form/delete?id=${id}`, {
      headers: headers,
    });
  }
}
